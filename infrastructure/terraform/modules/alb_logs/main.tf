# ap-northeast-1 の ELBサービスアカウントID。
# aws_elb_service_account データソースは provider v5 以降 deprecated のため、
# AWS公式ドキュメントのリージョン別固定値をそのまま使用する。
# 参照: https://docs.aws.amazon.com/elasticloadbalancing/latest/application/enable-access-logging.html
locals {
  elb_account_id = "582318560864"

  # ALBアクセスログのカラム定義。順序は input.regex のキャプチャ順と一致させること。
  alb_log_columns = [
    { name = "type", type = "string" },
    { name = "time", type = "string" },
    { name = "elb", type = "string" },
    { name = "client_ip", type = "string" },
    { name = "client_port", type = "int" },
    { name = "target_ip", type = "string" },
    { name = "target_port", type = "int" },
    { name = "request_processing_time", type = "double" },
    { name = "target_processing_time", type = "double" },
    { name = "response_processing_time", type = "double" },
    { name = "elb_status_code", type = "int" },
    { name = "target_status_code", type = "string" },
    { name = "received_bytes", type = "bigint" },
    { name = "sent_bytes", type = "bigint" },
    { name = "request_verb", type = "string" },
    { name = "request_url", type = "string" },
    { name = "request_proto", type = "string" },
    { name = "user_agent", type = "string" },
    { name = "ssl_cipher", type = "string" },
    { name = "ssl_protocol", type = "string" },
    { name = "target_group_arn", type = "string" },
    { name = "trace_id", type = "string" },
    { name = "domain_name", type = "string" },
    { name = "chosen_cert_arn", type = "string" },
    { name = "matched_rule_priority", type = "string" },
    { name = "request_creation_time", type = "string" },
    { name = "actions_executed", type = "string" },
    { name = "redirect_url", type = "string" },
    { name = "error_reason", type = "string" },
    { name = "target_port_list", type = "string" },
    { name = "target_status_code_list", type = "string" },
    { name = "classification", type = "string" },
    { name = "classification_reason", type = "string" },
  ]
}

data "aws_region" "current" {}

# ============================================
# S3: ALBアクセスログ保存用
# ============================================

resource "aws_s3_bucket" "logs" {
  bucket        = "mametosho-alb-access-logs-${var.env}"
  force_destroy = false
}

resource "aws_s3_bucket_public_access_block" "logs" {
  bucket = aws_s3_bucket.logs.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_bucket_lifecycle_configuration" "logs" {
  bucket = aws_s3_bucket.logs.id

  rule {
    id     = "expire-logs"
    status = "Enabled"

    filter {}

    expiration {
      days = 90
    }
  }
}

# ALBがログを書き込むために必要なバケットポリシー
resource "aws_s3_bucket_policy" "logs" {
  bucket = aws_s3_bucket.logs.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect    = "Allow"
        Principal = { AWS = "arn:aws:iam::${local.elb_account_id}:root" }
        Action    = "s3:PutObject"
        Resource  = "${aws_s3_bucket.logs.arn}/AWSLogs/${var.account_id}/*"
      },
      {
        Effect    = "Allow"
        Principal = { Service = "delivery.logs.amazonaws.com" }
        Action    = "s3:PutObject"
        Resource  = "${aws_s3_bucket.logs.arn}/AWSLogs/${var.account_id}/*"
        Condition = {
          StringEquals = { "s3:x-amz-acl" = "bucket-owner-full-control" }
        }
      },
      {
        Effect    = "Allow"
        Principal = { Service = "delivery.logs.amazonaws.com" }
        Action    = "s3:GetBucketAcl"
        Resource  = aws_s3_bucket.logs.arn
      }
    ]
  })

  depends_on = [aws_s3_bucket_public_access_block.logs]
}

# ============================================
# S3: Athenaクエリ結果保存用
# ============================================

resource "aws_s3_bucket" "athena_results" {
  bucket        = "mametosho-alb-athena-results-${var.env}"
  force_destroy = false
}

resource "aws_s3_bucket_public_access_block" "athena_results" {
  bucket = aws_s3_bucket.athena_results.id

  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

resource "aws_s3_bucket_lifecycle_configuration" "athena_results" {
  bucket = aws_s3_bucket.athena_results.id

  rule {
    id     = "expire-results"
    status = "Enabled"

    filter {}

    expiration {
      days = 30
    }
  }
}

# ============================================
# Athena
# ============================================

resource "aws_athena_workgroup" "this" {
  name = "${var.env}-alb-logs"

  configuration {
    result_configuration {
      output_location = "s3://${aws_s3_bucket.athena_results.id}/query-results/"
    }
  }
}

resource "aws_glue_catalog_database" "this" {
  name = "${var.env}_alb_logs"
}

resource "aws_glue_catalog_table" "alb_logs" {
  name          = "access_logs"
  database_name = aws_glue_catalog_database.this.name
  table_type    = "EXTERNAL_TABLE"

  parameters = {
    "EXTERNAL"                = "TRUE"
    "projection.enabled"      = "true"
    "projection.year.type"    = "integer"
    "projection.year.range"   = "2024,2030"
    "projection.month.type"   = "integer"
    "projection.month.range"  = "1,12"
    "projection.month.digits" = "2"
    "projection.day.type"     = "integer"
    "projection.day.range"    = "1,31"
    "projection.day.digits"   = "2"
    # $${year} はTerraform補間を回避して Athena に ${year} として渡すためのエスケープ
    "storage.location.template" = "s3://${aws_s3_bucket.logs.id}/AWSLogs/${var.account_id}/elasticloadbalancing/${data.aws_region.current.region}/$${year}/$${month}/$${day}"
  }

  partition_keys {
    name = "year"
    type = "string"
  }
  partition_keys {
    name = "month"
    type = "string"
  }
  partition_keys {
    name = "day"
    type = "string"
  }

  storage_descriptor {
    location      = "s3://${aws_s3_bucket.logs.id}/AWSLogs/${var.account_id}/elasticloadbalancing/${data.aws_region.current.region}/"
    input_format  = "org.apache.hadoop.mapred.TextInputFormat"
    output_format = "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat"

    ser_de_info {
      serialization_library = "org.apache.hadoop.hive.serde2.RegexSerDe"
      parameters = {
        "input.regex" = "([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*):([0-9]*) ([^ ]*)[:-]([0-9]*) ([-.0-9]*) ([-.0-9]*) ([-.0-9]*) (|[-0-9]*) (-|[-0-9]*) ([-0-9]*) ([-0-9]*) \"([^ ]*) (.*) (- |[^ ]*)\" \"([^\"]*)\" ([A-Z0-9-_]+) ([A-Za-z0-9.-]*) ([^ ]*) \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" ([-.0-9]*) ([^ ]*) \"([^\"]*)\" \"([^\"]*)\" \"([^ ]*)\" \"([^\\s]+?)\" \"([^\\s]+)\" \"([^ ]*)\" \"([^ ]*)\""
      }
    }

    dynamic "columns" {
      for_each = local.alb_log_columns
      content {
        name = columns.value.name
        type = columns.value.type
      }
    }
  }
}

resource "aws_secretsmanager_secret" "db_credentials" {
  name                    = "${var.account_id}-${var.env}-${var.prefix}-db"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret" "jwt_secret" {
  name                    = "${var.account_id}-${var.env}-${var.prefix}-jwt-secret"
  recovery_window_in_days = 0
}

resource "aws_ssm_parameter" "db_secret_arn" {
  name  = "/${var.env}/${var.prefix}/db-secret-arn"
  type  = "String"
  value = aws_secretsmanager_secret.db_credentials.arn
}

resource "aws_ssm_parameter" "jwt_secret_arn" {
  name  = "/${var.env}/${var.prefix}/jwt-secret-arn"
  type  = "String"
  value = aws_secretsmanager_secret.jwt_secret.arn
}

resource "aws_ssm_parameter" "rds_endpoint" {
  name  = "/${var.env}/${var.prefix}/rds-endpoint"
  type  = "String"
  value = var.rds_endpoint
}

resource "aws_ssm_parameter" "s3_bucket_name" {
  name  = "/${var.env}/${var.prefix}/s3-bucket-name"
  type  = "String"
  value = var.s3_bucket_name
}

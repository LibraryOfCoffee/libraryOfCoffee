# ============================================
# Secrets - Admin API
# ============================================

resource "aws_secretsmanager_secret" "admin_api_db" {
  name                    = "${local.account_id}-${local.env}-admin-api-db"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret" "admin_api_jwt" {
  name                    = "${local.account_id}-${local.env}-admin-api-jwt-secret"
  recovery_window_in_days = 0
}

resource "aws_ssm_parameter" "admin_api_db_secret_arn" {
  name  = "/${local.env}/admin-api/db-secret-arn"
  type  = "String"
  value = aws_secretsmanager_secret.admin_api_db.arn
}

resource "aws_ssm_parameter" "admin_api_jwt_secret_arn" {
  name  = "/${local.env}/admin-api/jwt-secret-arn"
  type  = "String"
  value = aws_secretsmanager_secret.admin_api_jwt.arn
}

resource "aws_ssm_parameter" "admin_api_rds_endpoint" {
  name  = "/${local.env}/admin-api/rds-endpoint"
  type  = "String"
  value = module.rds.endpoint
}

resource "aws_ssm_parameter" "admin_api_s3_bucket_name" {
  name  = "/${local.env}/admin-api/s3-bucket-name"
  type  = "String"
  value = module.s3_images.bucket_name
}

# ============================================
# Secrets - CS API
# ============================================

resource "aws_secretsmanager_secret" "cs_api_db" {
  name                    = "${local.account_id}-${local.env}-cs-api-db"
  recovery_window_in_days = 0
}

resource "aws_ssm_parameter" "cs_api_db_secret_arn" {
  name  = "/${local.env}/cs-api/db-secret-arn"
  type  = "String"
  value = aws_secretsmanager_secret.cs_api_db.arn
}

resource "aws_ssm_parameter" "cs_api_rds_endpoint" {
  name  = "/${local.env}/cs-api/rds-endpoint"
  type  = "String"
  value = module.rds.endpoint
}

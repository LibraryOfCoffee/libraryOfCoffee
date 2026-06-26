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

# HMAC秘密鍵を封筒暗号化するマスター鍵(AES-256, hex64)。値はTerraform管理外で投入する。
resource "aws_secretsmanager_secret" "cs_api_hmac_master_key" {
  name                    = "${local.account_id}-${local.env}-cs-api-hmac-master-key"
  recovery_window_in_days = 0
}

resource "aws_ssm_parameter" "cs_api_hmac_master_key_secret_arn" {
  name  = "/${local.env}/cs-api/hmac-master-key-secret-arn"
  type  = "String"
  value = aws_secretsmanager_secret.cs_api_hmac_master_key.arn
}

# ============================================
# Secrets - CS Frontend
# ============================================

# SSRがCS APIへHMAC署名するための平文クライアント秘密鍵。
# api_clients.encrypted_secret(ssr-frontend)の復号結果と一致させること。
resource "aws_secretsmanager_secret" "cs_frontend_hmac_ssr_key" {
  name                    = "${local.account_id}-${local.env}-cs-frontend-hmac-ssr-key"
  recovery_window_in_days = 0
}

resource "aws_ssm_parameter" "cs_frontend_hmac_ssr_key_secret_arn" {
  name  = "/${local.env}/cs-frontend/hmac-ssr-key-secret-arn"
  type  = "String"
  value = aws_secretsmanager_secret.cs_frontend_hmac_ssr_key.arn
}

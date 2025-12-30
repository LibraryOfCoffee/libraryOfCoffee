module "vpc" {
  source = "../../modules/vpc"
}

module "acm" {
  source = "../../modules/acm"

  domain_name               = "dev.mametosho.com"
  subject_alternative_names = []
}

module "cd" {
  source = "../../modules/cd"

  account_id  = local.account_id
  env         = local.env
  github_org  = "LibraryOfCoffee"
  github_repo = "libraryOfCoffee"
}

module "ecr_frontend" {
  source = "../../modules/ecr"

  repository_name      = "frontend"
  env                  = local.env
  image_retention_days = 90
  image_tag_mutability = "MUTABLE"
}

module "frontend" {
  source = "../../modules/frontend"

  account_id         = local.account_id
  env                = local.env
  vpc_id             = module.vpc.vpc_id
  public_subnet_ids  = module.vpc.public_subnet_ids
  private_subnet_ids = module.vpc.private_subnet_ids
  ecr_repository_url = module.ecr_frontend.repository_url
  certificate_arn    = module.acm.certificate_arn  # ACM検証完了まで待機
}

# ============================================
# Backend API
# ============================================

module "ecr_backend" {
  source = "../../modules/ecr"

  repository_name      = "backend"
  env                  = local.env
  image_retention_days = 90
  image_tag_mutability = "MUTABLE"
}

module "aurora" {
  source = "../../modules/aurora"

  account_id         = local.account_id
  env                = local.env
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnet_ids

  instance_class = "db.t3.medium"
  instance_count = 1

  # dev環境では削除保護を無効化
  deletion_protection = false
  skip_final_snapshot = true

  # Backendからのアクセスを許可
  allowed_security_group_ids = [module.backend.ecs_security_group_id]
}

module "backend" {
  source = "../../modules/backend"

  account_id         = local.account_id
  env                = local.env
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnet_ids
  ecr_repository_url = module.ecr_backend.repository_url

  # Frontendからのアクセスを許可
  allowed_security_group_ids = [module.frontend.ecs_security_group_id]

  # Aurora接続設定
  db_host        = module.aurora.cluster_endpoint
  db_port        = module.aurora.cluster_port
  db_name        = module.aurora.database_name
  db_secrets_arn = module.aurora.master_user_secret_arn
}

# ============================================
# Outputs
# ============================================

# ACM DNS検証用レコード（お名前.comで手動設定が必要）
output "acm_validation_records" {
  description = "Add these CNAME records to your DNS provider"
  value       = module.acm.domain_validation_options
}

output "backend_service_discovery_endpoint" {
  description = "Backend API Service Discovery endpoint"
  value       = module.backend.service_discovery_endpoint
}

output "aurora_endpoint" {
  description = "Aurora cluster endpoint"
  value       = module.aurora.cluster_endpoint
}

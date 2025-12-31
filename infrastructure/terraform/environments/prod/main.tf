module "vpc" {
  source = "../../modules/vpc"
}

module "acm" {
  source = "../../modules/acm"

  domain_name = "mametosho.com"
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
  image_tag_mutability = "IMMUTABLE"
}

module "frontend" {
  source = "../../modules/frontend"

  account_id         = local.account_id
  env                = local.env
  vpc_id             = module.vpc.vpc_id
  public_subnet_ids  = module.vpc.public_subnet_ids
  private_subnet_ids = module.vpc.private_subnet_ids
  ecr_repository_url = module.ecr_frontend.repository_url

  image_tag       = "latest"
  container_port  = 3000
  cpu             = 512
  memory          = 1024
  desired_count   = 2
  enable_https    = true
  certificate_arn = module.acm.certificate_arn
  api_url         = "http://${module.backend.service_discovery_endpoint}:8080"
}

# ============================================
# Backend API
# ============================================

module "ecr_backend" {
  source = "../../modules/ecr"

  repository_name      = "backend"
  env                  = local.env
  image_retention_days = 90
  image_tag_mutability = "IMMUTABLE"
}

module "aurora" {
  source = "../../modules/aurora"

  account_id         = local.account_id
  env                = local.env
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnet_ids

  instance_class = "db.r6g.large"
  instance_count = 2
  engine_version = "8.0.mysql_aurora.3.11.1"

  backup_retention_period = 14
  skip_final_snapshot     = false
  deletion_protection     = true

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

  image_tag      = "latest"
  container_port = 8080
  cpu            = 512
  memory         = 1024
  desired_count  = 2

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

module "vpc" {
  source = "../../modules/vpc"
}

module "acm" {
  source = "../../modules/acm"

  domain_name = "admin.mametosho.com"
}

module "acm_cs" {
  source = "../../modules/acm"

  domain_name = "api.mametosho.com"
}

module "cd" {
  source = "../../modules/cd"

  account_id  = local.account_id
  env         = local.env
  github_org  = "LibraryOfCoffee"
  github_repo = "libraryOfCoffee"
}

# ============================================
# ECR
# ============================================

module "ecr_admin_frontend" {
  source = "../../modules/ecr"

  repository_name      = "admin-frontend"
  env                  = local.env
  image_retention_days = 30
  image_tag_mutability = "IMMUTABLE"
}

module "ecr_admin_api" {
  source = "../../modules/ecr"

  repository_name      = "admin-api"
  env                  = local.env
  image_retention_days = 30
  image_tag_mutability = "IMMUTABLE"
}

module "ecr_cs_api" {
  source = "../../modules/ecr"

  repository_name      = "cs-api"
  env                  = local.env
  image_retention_days = 30
  image_tag_mutability = "IMMUTABLE"
}

# ============================================
# S3 (Image Storage)
# ============================================

module "s3_images" {
  source = "../../modules/s3"

  bucket_name   = "mametosho-images-${local.env}"
  force_destroy = false
}

# ============================================
# Bastion (DB接続用踏み台サーバー)
# ============================================

module "bastion" {
  source = "../../modules/bastion"

  env       = local.env
  vpc_id    = module.vpc.vpc_id
  subnet_id = module.vpc.public_subnet_ids[0]
}

# ============================================
# RDS (MySQL)
# ============================================

module "rds" {
  source = "../../modules/rds"

  account_id         = local.account_id
  env                = local.env
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnet_ids

  allowed_security_group_ids = [
    module.bastion.security_group_id,
  ]

  instance_class          = "db.t4g.micro"
  db_name                 = "mametosho"
  backup_retention_period = 14
  skip_final_snapshot     = false
  deletion_protection     = true
}

# ============================================
# ALB
# ============================================

module "alb_admin" {
  source = "../../modules/alb"

  account_id          = local.account_id
  env                 = local.env
  name                = "admin"
  vpc_id              = module.vpc.vpc_id
  public_subnet_ids   = module.vpc.public_subnet_ids
  allowed_cidr_blocks = local.allowed_cidr_blocks
  certificate_arn     = module.acm.certificate_arn
  host_header         = "admin.mametosho.com"
  container_port      = 3001
}

module "alb_cs" {
  source = "../../modules/alb"

  account_id          = local.account_id
  env                 = local.env
  name                = "cs"
  vpc_id              = module.vpc.vpc_id
  public_subnet_ids   = module.vpc.public_subnet_ids
  allowed_cidr_blocks = local.allowed_cidr_blocks
  certificate_arn     = module.acm_cs.certificate_arn
  host_header         = "api.mametosho.com"
  container_port      = 8080
  health_check_path   = "/actuator/health"
}

# ============================================
# Service Discovery
# ============================================

module "service_discovery" {
  source = "../../modules/service_discovery"

  env           = local.env
  vpc_id        = module.vpc.vpc_id
  service_names = ["admin-api"]
}

# ============================================
# ECS - CS API
# ============================================

module "ecs_cs_api" {
  source = "../../modules/ecs_fargate"

  account_id            = local.account_id
  env                   = local.env
  service_name          = "cs-api"
  container_name        = "cs-api"
  vpc_id                = module.vpc.vpc_id
  private_subnet_ids    = module.vpc.private_subnet_ids
  ingress_from_sg_id    = module.alb_cs.alb_security_group_id
  ingress_description   = "Allow access from cs ALB"
  container_port        = 8080
  cpu                   = 512
  memory                = 1024
  image_url             = "${module.ecr_cs_api.repository_url}:latest"
  target_group_arn      = module.alb_cs.target_group_arn
  enable_rds_access     = true
  rds_security_group_id = module.rds.security_group_id
  secret_arns = [
    aws_secretsmanager_secret.cs_api_db.arn,
  ]

  environment = [
    { name = "DB_URL", value = "jdbc:mysql://${module.rds.endpoint}:${module.rds.port}/${module.rds.db_name}" },
  ]

  secrets = [
    { name = "DB_USERNAME", valueFrom = "${aws_secretsmanager_secret.cs_api_db.arn}:username::" },
    { name = "DB_PASSWORD", valueFrom = "${aws_secretsmanager_secret.cs_api_db.arn}:password::" },
  ]
}

# ============================================
# ECS - Admin Frontend
# ============================================

module "ecs_admin_frontend" {
  source = "../../modules/ecs_fargate"

  account_id          = local.account_id
  env                 = local.env
  service_name        = "admin-frontend"
  container_name      = "admin-frontend"
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  ingress_from_sg_id  = module.alb_admin.alb_security_group_id
  ingress_description = "Allow access from admin ALB"
  container_port      = 3001
  cpu                 = 256
  memory              = 512
  image_url           = "${module.ecr_admin_frontend.repository_url}:latest"
  target_group_arn    = module.alb_admin.target_group_arn

  environment = [
    { name = "API_BASE_URL", value = "http://admin-api.${local.env}.local:8080" },
  ]
}

# ============================================
# ECS - Admin API
# ============================================

module "ecs_admin_api" {
  source = "../../modules/ecs_fargate"

  account_id            = local.account_id
  env                   = local.env
  service_name          = "admin-api"
  container_name        = "admin-api"
  vpc_id                = module.vpc.vpc_id
  private_subnet_ids    = module.vpc.private_subnet_ids
  ingress_from_sg_id    = module.ecs_admin_frontend.ecs_sg_id
  ingress_description   = "Allow access from admin-frontend"
  container_port        = 8080
  cpu                   = 512
  memory                = 1024
  image_url             = "${module.ecr_admin_api.repository_url}:latest"
  service_registry_arn  = module.service_discovery.service_arns["admin-api"]
  enable_rds_access     = true
  rds_security_group_id = module.rds.security_group_id
  enable_s3_access      = true
  secret_arns = [
    aws_secretsmanager_secret.admin_api_db.arn,
    aws_secretsmanager_secret.admin_api_jwt.arn,
  ]
  s3_bucket_arn = module.s3_images.bucket_arn

  environment = [
    { name = "DB_URL", value = "jdbc:mysql://${module.rds.endpoint}:${module.rds.port}/${module.rds.db_name}" },
    { name = "S3_BUCKET_NAME", value = module.s3_images.bucket_name },
    { name = "S3_REGION", value = "ap-northeast-1" },
    { name = "S3_BASE_URL", value = module.s3_images.base_url },
  ]

  secrets = [
    { name = "DB_USERNAME", valueFrom = "${aws_secretsmanager_secret.admin_api_db.arn}:username::" },
    { name = "DB_PASSWORD", valueFrom = "${aws_secretsmanager_secret.admin_api_db.arn}:password::" },
    { name = "JWT_SECRET_KEY", valueFrom = aws_secretsmanager_secret.admin_api_jwt.arn },
  ]
}

# ============================================
# Outputs
# ============================================

output "acm_validation_records" {
  description = "Add these CNAME records to your DNS provider"
  value       = module.acm.domain_validation_options
}

output "admin_alb_dns" {
  description = "Admin ALB DNS name (set admin.mametosho.com CNAME to this)"
  value       = module.alb_admin.alb_dns_name
}

output "cs_acm_validation_records" {
  description = "Add these CNAME records to your DNS provider"
  value       = module.acm_cs.domain_validation_options
}

output "cs_alb_dns" {
  description = "CS ALB DNS name (set api.mametosho.com CNAME to this)"
  value       = module.alb_cs.alb_dns_name
}

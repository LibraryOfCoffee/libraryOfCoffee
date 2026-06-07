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
# デフォルトは停止状態。必要時は running = true にして apply する。
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

  allowed_security_group_ids = {
    bastion = module.bastion.security_group_id
  }

  instance_class          = "db.t4g.micro"
  db_name                 = "mametosho"
  backup_retention_period = 14
  skip_final_snapshot     = false
  deletion_protection     = true
}

# ============================================
# ALB (admin + cs-api を1台に統合)
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

# cs-api用ターゲットグループ (admin ALBに統合)
resource "aws_lb_target_group" "cs_api" {
  name        = "prod-cs-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = module.vpc.vpc_id
  target_type = "ip"

  health_check {
    enabled             = true
    healthy_threshold   = 2
    interval            = 30
    matcher             = "200"
    path                = "/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    unhealthy_threshold = 2
  }
}

# cs-api用証明書をadmin ALBのHTTPSリスナーに追加 (SNI)
resource "aws_lb_listener_certificate" "cs" {
  listener_arn    = module.alb_admin.https_listener_arn
  certificate_arn = module.acm_cs.certificate_arn
}

# cs-api用リスナールール (api.mametosho.com → cs-api TG)
resource "aws_lb_listener_rule" "cs_api" {
  listener_arn = module.alb_admin.https_listener_arn
  priority     = 20

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.cs_api.arn
  }

  condition {
    host_header {
      values = ["api.mametosho.com"]
    }
  }
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
  subnet_ids            = module.vpc.public_subnet_ids
  ingress_from_sg_id    = module.alb_admin.alb_security_group_id
  ingress_description   = "Allow access from ALB"
  container_port        = 8080
  cpu                   = 512
  memory                = 1024
  image_url             = "${module.ecr_cs_api.repository_url}:latest"
  target_group_arn      = aws_lb_target_group.cs_api.arn
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
  subnet_ids          = module.vpc.public_subnet_ids
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
  subnet_ids            = module.vpc.public_subnet_ids
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

output "alb_dns" {
  description = "ALB DNS name (admin.mametosho.com と api.mametosho.com の両方をこのALBに向ける)"
  value       = module.alb_admin.alb_dns_name
}

output "cs_acm_validation_records" {
  description = "Add these CNAME records to your DNS provider"
  value       = module.acm_cs.domain_validation_options
}

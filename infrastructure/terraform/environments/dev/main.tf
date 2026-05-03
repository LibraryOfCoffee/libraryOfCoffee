module "vpc" {
  source = "../../modules/vpc"
}

module "acm" {
  source = "../../modules/acm"

  domain_name = "dev.admin.mametosho.com"
}

module "cd" {
  source = "../../modules/cd"

  account_id  = local.account_id
  env         = local.env
  github_org  = "LibraryOfCoffee"
  github_repo = "libraryOfCoffee"
}

# ============================================
# Admin Frontend - ECR
# ============================================

module "ecr_admin_frontend" {
  source = "../../modules/ecr"

  repository_name      = "admin-frontend"
  env                  = local.env
  image_retention_days = 30
  image_tag_mutability = "MUTABLE"
}

# ============================================
# Admin Frontend - Security Groups
# ============================================

resource "aws_security_group" "admin_frontend_alb" {
  name        = "${local.account_id}-${local.env}-admin-alb"
  description = "Security group for admin ALB"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = local.allowed_cidr_blocks
    description = "HTTP from allowed IPs"
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = local.allowed_cidr_blocks
    description = "HTTPS from allowed IPs"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "admin_frontend_ecs" {
  name        = "${local.account_id}-${local.env}-admin-frontend-ecs"
  description = "Security group for admin-frontend ECS tasks"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port       = 3001
    to_port         = 3001
    protocol        = "tcp"
    security_groups = [aws_security_group.admin_frontend_alb.id]
    description     = "Allow access from admin ALB"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ============================================
# Admin Frontend - ALB
# ============================================

resource "aws_lb" "admin_frontend" {
  name               = "${local.env}-admin-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.admin_frontend_alb.id]
  subnets            = module.vpc.public_subnet_ids
}

resource "aws_lb_target_group" "admin_frontend" {
  name        = "${local.env}-admin-frontend-tg"
  port        = 3001
  protocol    = "HTTP"
  vpc_id      = module.vpc.vpc_id
  target_type = "ip"

  health_check {
    enabled             = true
    healthy_threshold   = 2
    interval            = 30
    matcher             = "200"
    path                = "/api/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    unhealthy_threshold = 2
  }
}

resource "aws_lb_listener" "admin_frontend_http" {
  load_balancer_arn = aws_lb.admin_frontend.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"
    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

resource "aws_lb_listener" "admin_frontend_https" {
  load_balancer_arn = aws_lb.admin_frontend.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = module.acm.certificate_arn

  default_action {
    type = "fixed-response"
    fixed_response {
      content_type = "text/plain"
      message_body = "Not Found"
      status_code  = "404"
    }
  }
}

resource "aws_lb_listener_rule" "admin_frontend" {
  listener_arn = aws_lb_listener.admin_frontend_https.arn
  priority     = 10

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.admin_frontend.arn
  }

  condition {
    host_header {
      values = ["${local.env}.admin.mametosho.com"]
    }
  }
}

# ============================================
# Admin Frontend - ECS
# ============================================

resource "aws_cloudwatch_log_group" "admin_frontend" {
  name              = "/ecs/${local.account_id}-${local.env}-admin-frontend"
  retention_in_days = 30
}

resource "aws_iam_role" "admin_frontend_ecs_execution" {
  name = "${local.account_id}-${local.env}-admin-frontend-ecs-execution"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "admin_frontend_ecs_execution" {
  role       = aws_iam_role.admin_frontend_ecs_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_ecs_cluster" "admin_frontend" {
  name = "${local.account_id}-${local.env}-admin-frontend"
}

resource "aws_ecs_cluster_capacity_providers" "admin_frontend" {
  cluster_name       = aws_ecs_cluster.admin_frontend.name
  capacity_providers = ["FARGATE", "FARGATE_SPOT"]
}

resource "aws_ecs_task_definition" "admin_frontend" {
  family                   = "${local.account_id}-${local.env}-admin-frontend"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 256
  memory                   = 512
  execution_role_arn       = aws_iam_role.admin_frontend_ecs_execution.arn

  container_definitions = jsonencode([
    {
      name  = "admin-frontend"
      image = "${module.ecr_admin_frontend.repository_url}:latest"

      portMappings = [
        {
          containerPort = 3001
          hostPort      = 3001
          protocol      = "tcp"
        }
      ]

      environment = [
        {
          name  = "ADMIN_API_URL"
          value = "http://admin-api.${local.env}.local:8080"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.admin_frontend.name
          "awslogs-region"        = "ap-northeast-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }

      essential = true
    }
  ])
}

resource "aws_ecs_service" "admin_frontend" {
  name            = "${local.account_id}-${local.env}-admin-frontend"
  cluster         = aws_ecs_cluster.admin_frontend.id
  task_definition = aws_ecs_task_definition.admin_frontend.arn
  desired_count   = 1

  capacity_provider_strategy {
    capacity_provider = "FARGATE_SPOT"
    weight            = 100
    base              = 0
  }

  network_configuration {
    subnets          = module.vpc.private_subnet_ids
    security_groups  = [aws_security_group.admin_frontend_ecs.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.admin_frontend.arn
    container_name   = "admin-frontend"
    container_port   = 3001
  }

  depends_on = [aws_lb_listener.admin_frontend_https]
}

# ============================================
# S3 (Image Storage)
# ============================================

module "s3_images" {
  source = "../../modules/s3"

  bucket_name   = "mametosho-images-${local.env}"
  force_destroy = true
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

  allowed_security_group_ids = [aws_security_group.admin_api_ecs.id]

  instance_class          = "db.t4g.micro"
  db_name                 = "mametosho"
  backup_retention_period = 7
  skip_final_snapshot     = true
  deletion_protection     = false
}

# ============================================
# JWT Secret
# ============================================

resource "aws_secretsmanager_secret" "jwt_secret" {
  name = "${local.account_id}-${local.env}-admin-api-jwt-secret"
}

# ============================================
# Service Discovery
# ============================================

resource "aws_service_discovery_private_dns_namespace" "main" {
  name = "${local.env}.local"
  vpc  = module.vpc.vpc_id
}

resource "aws_service_discovery_service" "admin_api" {
  name = "admin-api"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.main.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  health_check_custom_config {}
}

# ============================================
# Admin API - ECR
# ============================================

module "ecr_admin_api" {
  source = "../../modules/ecr"

  repository_name      = "admin-api"
  env                  = local.env
  image_retention_days = 30
  image_tag_mutability = "MUTABLE"
}

# ============================================
# Admin API - Security Group
# ============================================

resource "aws_security_group" "admin_api_ecs" {
  name        = "${local.account_id}-${local.env}-admin-api-ecs"
  description = "Security group for admin-api ECS tasks"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.admin_frontend_ecs.id]
    description     = "Allow access from admin-frontend"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ============================================
# Admin API - ECS
# ============================================

resource "aws_cloudwatch_log_group" "admin_api" {
  name              = "/ecs/${local.account_id}-${local.env}-admin-api"
  retention_in_days = 30
}

resource "aws_iam_role" "admin_api_ecs_execution" {
  name = "${local.account_id}-${local.env}-admin-api-ecs-execution"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action    = "sts:AssumeRole"
      Effect    = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "admin_api_ecs_execution" {
  role       = aws_iam_role.admin_api_ecs_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy" "admin_api_ecs_execution_secrets" {
  name = "secrets-access"
  role = aws_iam_role.admin_api_ecs_execution.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect   = "Allow"
      Action   = ["secretsmanager:GetSecretValue"]
      Resource = [
        module.rds.master_user_secret_arn,
        aws_secretsmanager_secret.jwt_secret.arn,
      ]
    }]
  })
}

resource "aws_iam_role" "admin_api_ecs_task" {
  name = "${local.account_id}-${local.env}-admin-api-ecs-task"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action    = "sts:AssumeRole"
      Effect    = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy" "admin_api_ecs_task_s3" {
  name = "s3-images-access"
  role = aws_iam_role.admin_api_ecs_task.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect   = "Allow"
      Action   = ["s3:PutObject", "s3:DeleteObject", "s3:GetObject"]
      Resource = "${module.s3_images.bucket_arn}/*"
    }]
  })
}

resource "aws_ecs_cluster" "admin_api" {
  name = "${local.account_id}-${local.env}-admin-api"
}

resource "aws_ecs_cluster_capacity_providers" "admin_api" {
  cluster_name       = aws_ecs_cluster.admin_api.name
  capacity_providers = ["FARGATE", "FARGATE_SPOT"]
}

resource "aws_ecs_task_definition" "admin_api" {
  family                   = "${local.account_id}-${local.env}-admin-api"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 512
  memory                   = 1024
  execution_role_arn       = aws_iam_role.admin_api_ecs_execution.arn
  task_role_arn            = aws_iam_role.admin_api_ecs_task.arn

  container_definitions = jsonencode([
    {
      name  = "admin-api"
      image = "${module.ecr_admin_api.repository_url}:latest"

      portMappings = [{
        containerPort = 8080
        hostPort      = 8080
        protocol      = "tcp"
      }]

      environment = [
        {
          name  = "DB_URL"
          value = "jdbc:mysql://${module.rds.endpoint}:${module.rds.port}/${module.rds.db_name}"
        },
        {
          name  = "S3_BUCKET_NAME"
          value = module.s3_images.bucket_name
        },
        {
          name  = "S3_REGION"
          value = "ap-northeast-1"
        },
        {
          name  = "S3_BASE_URL"
          value = module.s3_images.base_url
        },
      ]

      secrets = [
        {
          name      = "DB_USERNAME"
          valueFrom = "${module.rds.master_user_secret_arn}:username::"
        },
        {
          name      = "DB_PASSWORD"
          valueFrom = "${module.rds.master_user_secret_arn}:password::"
        },
        {
          name      = "JWT_SECRET_KEY"
          valueFrom = aws_secretsmanager_secret.jwt_secret.arn
        },
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.admin_api.name
          "awslogs-region"        = "ap-northeast-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }

      essential = true
    }
  ])
}

resource "aws_ecs_service" "admin_api" {
  name            = "${local.account_id}-${local.env}-admin-api"
  cluster         = aws_ecs_cluster.admin_api.id
  task_definition = aws_ecs_task_definition.admin_api.arn
  desired_count   = 1

  capacity_provider_strategy {
    capacity_provider = "FARGATE_SPOT"
    weight            = 100
    base              = 0
  }

  network_configuration {
    subnets          = module.vpc.private_subnet_ids
    security_groups  = [aws_security_group.admin_api_ecs.id]
    assign_public_ip = false
  }

  service_registries {
    registry_arn = aws_service_discovery_service.admin_api.arn
  }
}

# ============================================
# Outputs
# ============================================

output "acm_validation_records" {
  description = "Add these CNAME records to your DNS provider"
  value       = module.acm.domain_validation_options
}

output "admin_alb_dns" {
  description = "Admin ALB DNS name (set dev.admin.mametosho.com CNAME to this)"
  value       = aws_lb.admin_frontend.dns_name
}

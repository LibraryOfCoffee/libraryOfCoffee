# ECS Cluster
resource "aws_ecs_cluster" "main" {
  name = "${var.account_id}-${var.env}-backend"
}

# Task Definition（コンテナの設定）
resource "aws_ecs_task_definition" "main" {
  family                   = "${var.account_id}-${var.env}-backend"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.cpu
  memory                   = var.memory
  execution_role_arn       = aws_iam_role.execution.arn
  task_role_arn            = aws_iam_role.task.arn

  container_definitions = jsonencode([
    {
      name  = "backend"
      image = "${var.ecr_repository_url}:${var.image_tag}"

      portMappings = [
        {
          containerPort = var.container_port
          hostPort      = var.container_port
          protocol      = "tcp"
        }
      ]

      environment = [
        {
          name  = "DB_HOST"
          value = var.db_host
        },
        {
          name  = "DB_PORT"
          value = tostring(var.db_port)
        },
        {
          name  = "DB_NAME"
          value = var.db_name
        }
      ]

      secrets = var.db_secrets_arn != "" ? [
        {
          name      = "DB_USER"
          valueFrom = "${var.db_secrets_arn}:username::"
        },
        {
          name      = "DB_PASSWORD"
          valueFrom = "${var.db_secrets_arn}:password::"
        }
      ] : []

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.main.name
          "awslogs-region"        = "ap-northeast-1"
          "awslogs-stream-prefix" = "ecs"
        }
      }

      essential = true
    }
  ])
}

# ECS Service
resource "aws_ecs_service" "main" {
  name            = "${var.account_id}-${var.env}-backend"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.main.arn
  desired_count   = var.desired_count
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.private_subnet_ids
    security_groups  = [aws_security_group.ecs.id]
    assign_public_ip = false
  }

  # Service Discovery登録
  service_registries {
    registry_arn = aws_service_discovery_service.main.arn
  }
}

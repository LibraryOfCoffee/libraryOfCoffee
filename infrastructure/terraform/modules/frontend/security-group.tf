# ALB用セキュリティグループ
resource "aws_security_group" "alb" {
  name        = "${var.account_id}-${var.env}-frontend-alb"
  description = "Security group for ALB"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# ECSタスク用セキュリティグループ
resource "aws_security_group" "ecs" {
  name        = "${var.account_id}-${var.env}-frontend-ecs"
  description = "Security group for ECS tasks"
  vpc_id      = var.vpc_id

  # ALBからのみアクセス許可
  ingress {
    from_port       = var.container_port
    to_port         = var.container_port
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

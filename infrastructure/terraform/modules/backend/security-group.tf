# ECSタスク用セキュリティグループ
resource "aws_security_group" "ecs" {
  name        = "${var.account_id}-${var.env}-backend-ecs"
  description = "Security group for Backend ECS tasks"
  vpc_id      = var.vpc_id

  # Frontendからのアクセスを許可
  ingress {
    from_port       = var.container_port
    to_port         = var.container_port
    protocol        = "tcp"
    security_groups = var.allowed_security_group_ids
    description     = "Allow access from frontend ECS"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

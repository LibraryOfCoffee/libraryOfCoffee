resource "aws_security_group" "ecs" {
  name        = "${var.account_id}-${var.env}-${var.service_name}-ecs"
  description = "Security group for ${var.service_name} ECS tasks"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = var.container_port
    to_port         = var.container_port
    protocol        = "tcp"
    security_groups = [var.ingress_from_sg_id]
    description     = var.ingress_description
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group_rule" "to_rds" {
  count = var.rds_security_group_id != null ? 1 : 0

  type                     = "ingress"
  from_port                = 3306
  to_port                  = 3306
  protocol                 = "tcp"
  security_group_id        = var.rds_security_group_id
  source_security_group_id = aws_security_group.ecs.id
  description              = "Allow access from ${var.service_name}"
}

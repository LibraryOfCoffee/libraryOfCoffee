# Aurora用セキュリティグループ
resource "aws_security_group" "main" {
  name        = "${var.account_id}-${var.env}-aurora"
  description = "Security group for Aurora MySQL"
  vpc_id      = var.vpc_id

  tags = {
    Name = "${var.account_id}-${var.env}-aurora"
    Env  = var.env
  }
}

# 許可されたセキュリティグループからのアクセス（backend ECS等）
resource "aws_security_group_rule" "ingress" {
  count = length(var.allowed_security_group_ids)

  type                     = "ingress"
  from_port                = 3306
  to_port                  = 3306
  protocol                 = "tcp"
  source_security_group_id = var.allowed_security_group_ids[count.index]
  security_group_id        = aws_security_group.main.id
}

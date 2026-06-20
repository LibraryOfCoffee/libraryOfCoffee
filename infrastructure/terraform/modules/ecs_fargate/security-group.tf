resource "aws_security_group" "ecs" {
  name        = "${var.account_id}-${var.env}-${var.service_name}-ecs"
  description = "Security group for ${var.service_name} ECS tasks"
  vpc_id      = var.vpc_id

  # ingressは全てこのSGリソースのinlineブロックで定義する。
  # inlineの`ingress`と独立した`aws_security_group_rule`を同一SGに混在させると、
  # applyのたびにinline側が「あるべき全ingress」とみなされ、独立ルールで足した分が
  # revokeされてSGが空になる競合が起きる(Terraformが公式に禁止している組み合わせ)。
  # そのため独立ルールは一切併用しない。
  # 主たる許可元(ingress_from_sg_id)と追加の許可元(additional_ingress_sgs)を1つのリストに
  # まとめ、単一のdynamicブロックで container_port への ingress を定義する。
  dynamic "ingress" {
    for_each = concat(
      [{ sg_id = var.ingress_from_sg_id, description = var.ingress_description }],
      var.additional_ingress_sgs,
    )
    content {
      from_port       = var.container_port
      to_port         = var.container_port
      protocol        = "tcp"
      security_groups = [ingress.value.sg_id]
      description     = ingress.value.description
    }
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# RDSのSGへのingress(source = このECSのSG)。これは別SG(rds)に対する独立ルールであり、
# ECSのSGのinline ingressとは別SGなので競合しない。
resource "aws_security_group_rule" "to_rds" {
  count = var.enable_rds_access ? 1 : 0

  type                     = "ingress"
  from_port                = 3306
  to_port                  = 3306
  protocol                 = "tcp"
  security_group_id        = var.rds_security_group_id
  source_security_group_id = aws_security_group.ecs.id
  description              = "Allow access from ${var.service_name}"
}

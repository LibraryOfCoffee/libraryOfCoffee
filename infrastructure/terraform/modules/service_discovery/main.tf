resource "aws_service_discovery_private_dns_namespace" "this" {
  name = "${var.env}.local"
  vpc  = var.vpc_id
}

resource "aws_service_discovery_service" "services" {
  for_each = toset(var.service_names)

  name = each.value

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.this.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }
}

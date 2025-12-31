# Cloud Map Namespace (Private DNS)
resource "aws_service_discovery_private_dns_namespace" "main" {
  name        = "${var.env}.local"
  description = "Private DNS namespace for ${var.env} environment"
  vpc         = var.vpc_id
}

# Service Discovery Service
resource "aws_service_discovery_service" "main" {
  name = "backend"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.main.id

    dns_records {
      ttl  = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  health_check_custom_config {
    # failure_threshold is always 1 (deprecated parameter)
  }
}

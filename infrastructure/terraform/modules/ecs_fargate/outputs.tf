output "cluster_id" {
  value = aws_ecs_cluster.this.id
}

output "service_id" {
  value = aws_ecs_service.this.id
}

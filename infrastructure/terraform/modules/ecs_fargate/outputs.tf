output "cluster_id" {
  value = aws_ecs_cluster.this.id
}

output "service_id" {
  value = aws_ecs_service.this.id
}

output "ecs_sg_id" {
  value = aws_security_group.ecs.id
}

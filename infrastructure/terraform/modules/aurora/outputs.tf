output "cluster_endpoint" {
  description = "Aurora cluster endpoint (writer)"
  value       = aws_rds_cluster.main.endpoint
}

output "cluster_reader_endpoint" {
  description = "Aurora cluster reader endpoint"
  value       = aws_rds_cluster.main.reader_endpoint
}

output "cluster_port" {
  description = "Aurora cluster port"
  value       = aws_rds_cluster.main.port
}

output "database_name" {
  description = "Database name"
  value       = aws_rds_cluster.main.database_name
}

output "master_user_secret_arn" {
  description = "Secrets Manager ARN for master user credentials"
  value       = aws_rds_cluster.main.master_user_secret[0].secret_arn
}

output "security_group_id" {
  description = "Aurora security group ID"
  value       = aws_security_group.main.id
}

output "security_group_id" {
  value = aws_security_group.main.id
}

output "instance_id" {
  value = aws_instance.main.id
}

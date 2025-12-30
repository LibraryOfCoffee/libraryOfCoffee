# CloudWatch Log Group for ECS
resource "aws_cloudwatch_log_group" "main" {
  name              = "/ecs/${var.account_id}-${var.env}-backend"
  retention_in_days = 30
}

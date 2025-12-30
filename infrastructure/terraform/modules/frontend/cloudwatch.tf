# CloudWatch Log Group（コンテナのログ出力先）
resource "aws_cloudwatch_log_group" "main" {
  name              = "/ecs/${var.account_id}-${var.env}-frontend"
  retention_in_days = 7
}

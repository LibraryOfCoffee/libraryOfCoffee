# ECSタスク実行用IAMロール
# ECRからイメージを取得し、CloudWatch Logsにログを書き込む権限
resource "aws_iam_role" "main" {
  name = "${var.account_id}-${var.env}-frontend-ecs-execution"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

# AWS管理ポリシーをアタッチ
resource "aws_iam_role_policy_attachment" "main" {
  role       = aws_iam_role.main.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

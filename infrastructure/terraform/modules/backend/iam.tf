# ECSタスク実行用IAMロール
# ECRからイメージを取得し、CloudWatch Logsにログを書き込む権限
resource "aws_iam_role" "execution" {
  name = "${var.account_id}-${var.env}-backend-ecs-execution"

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
resource "aws_iam_role_policy_attachment" "execution" {
  role       = aws_iam_role.execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# Secrets Manager読み取り権限（DB認証情報用）
resource "aws_iam_role_policy" "secrets" {
  count = var.db_secrets_arn != "" ? 1 : 0
  name  = "secrets-access"
  role  = aws_iam_role.execution.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue"
        ]
        Resource = [var.db_secrets_arn]
      }
    ]
  })
}

# ECSタスク用IAMロール（アプリケーションが使用する権限）
resource "aws_iam_role" "task" {
  name = "${var.account_id}-${var.env}-backend-ecs-task"

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

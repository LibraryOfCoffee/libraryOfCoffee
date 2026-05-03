# GitHub OIDC Provider
resource "aws_iam_openid_connect_provider" "main" {
  url = "https://token.actions.githubusercontent.com"

  client_id_list = ["sts.amazonaws.com"]

  # GitHub Actions OIDC の thumbprint
  # https://github.blog/changelog/2023-06-27-github-actions-update-on-oidc-integration-with-aws/
  thumbprint_list = ["1c58a3a8518e8759bf075b76b750d4f2df264fcd"]
}

# GitHub Actions 用 IAM ロール
resource "aws_iam_role" "main" {
  name = "${var.account_id}-${var.env}-gha-runner"

  assume_role_policy = templatefile("${path.module}/../../files/policy/assume_role_github_oidc.json", {
    oidc_provider_arn = aws_iam_openid_connect_provider.main.arn
    github_org        = var.github_org
    github_repo       = var.github_repo
  })
}

# ECR Push 用ポリシー
resource "aws_iam_role_policy" "ecr" {
  name = "ecr-push"
  role = aws_iam_role.main.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "GetAuthorizationToken"
        Effect = "Allow"
        Action = [
          "ecr:GetAuthorizationToken"
        ]
        Resource = "*"
      },
      {
        Sid    = "PushPullImages"
        Effect = "Allow"
        Action = [
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchGetImage",
          "ecr:PutImage",
          "ecr:InitiateLayerUpload",
          "ecr:UploadLayerPart",
          "ecr:CompleteLayerUpload"
        ]
        Resource = "arn:aws:ecr:${var.aws_region}:${var.account_id}:repository/*"
      }
    ]
  })
}

# ECS Deploy 用ポリシー
resource "aws_iam_role_policy" "ecs" {
  name = "ecs-deploy"
  role = aws_iam_role.main.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "ECSUpdateService"
        Effect = "Allow"
        Action = [
          "ecs:UpdateService",
          "ecs:DescribeServices"
        ]
        Resource = "arn:aws:ecs:${var.aws_region}:${var.account_id}:service/*/*"
      },
      {
        Sid    = "ECSRegisterTaskDefinition"
        Effect = "Allow"
        Action = [
          "ecs:RegisterTaskDefinition",
          "ecs:DescribeTaskDefinition"
        ]
        Resource = "*"
      },
      {
        Sid    = "IAMPassRole"
        Effect = "Allow"
        Action = [
          "iam:PassRole"
        ]
        Resource = "arn:aws:iam::${var.account_id}:role/*-ecs-*"
      }
    ]
  })
}

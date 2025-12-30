resource "aws_ecr_repository" "main" {
  name                 = "${var.repository_name}-${var.env}"
  image_tag_mutability = var.image_tag_mutability

  image_scanning_configuration {
    scan_on_push = true
  }
}

resource "aws_ecr_lifecycle_policy" "main" {
  repository = aws_ecr_repository.main.name

  policy = jsonencode({
    rules = [
      {
        rulePriority = 1
        description  = "Delete untagged images older than ${var.image_retention_days} days"
        selection = {
          tagStatus   = "untagged"
          countType   = "sinceImagePushed"
          countUnit   = "days"
          countNumber = var.image_retention_days
        }
        action = {
          type = "expire"
        }
      }
    ]
  })
}

# ACM証明書（DNS検証）
resource "aws_acm_certificate" "main" {
  domain_name               = var.domain_name
  subject_alternative_names = var.subject_alternative_names
  validation_method         = "DNS"

  lifecycle {
    create_before_destroy = true
  }

  tags = {
    Name = var.domain_name
  }
}

# 証明書の検証完了を待つ（DNS設定後に有効になるまで待機）
resource "aws_acm_certificate_validation" "main" {
  certificate_arn = aws_acm_certificate.main.arn

  timeouts {
    create = "30m"  # DNS反映を待つ（最大30分）
  }
}

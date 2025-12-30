output "certificate_arn" {
  description = "ACM certificate ARN (validated)"
  value       = aws_acm_certificate_validation.main.certificate_arn
}

output "domain_validation_options" {
  description = "DNS validation records (add these to your DNS provider)"
  value = [
    for dvo in aws_acm_certificate.main.domain_validation_options : {
      domain_name   = dvo.domain_name
      record_name   = dvo.resource_record_name
      record_type   = dvo.resource_record_type
      record_value  = dvo.resource_record_value
    }
  ]
}

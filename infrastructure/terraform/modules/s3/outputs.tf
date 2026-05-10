output "bucket_name" {
  value = aws_s3_bucket.main.bucket
}

output "bucket_arn" {
  value = aws_s3_bucket.main.arn
}

output "base_url" {
  value = "https://${aws_s3_bucket.main.bucket}.s3.ap-northeast-1.amazonaws.com"
}

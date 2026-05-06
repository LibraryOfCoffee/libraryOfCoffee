variable "account_id" {
  type = string
}

variable "env" {
  type = string
}

variable "prefix" {
  type        = string
  default     = "admin-api"
  description = "シークレット名とSSMパラメータパスのプレフィックス"
}

variable "rds_endpoint" {
  type        = string
  description = "SSMパラメータに保存するRDSエンドポイント"
}

variable "s3_bucket_name" {
  type        = string
  description = "SSMパラメータに保存するS3バケット名"
}

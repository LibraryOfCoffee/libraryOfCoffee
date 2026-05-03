variable "bucket_name" {
  type        = string
  description = "S3 bucket name"
}

variable "force_destroy" {
  type        = bool
  description = "Allow bucket to be destroyed even if it contains objects"
  default     = false
}

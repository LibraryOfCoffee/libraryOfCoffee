variable "repository_name" {
  type        = string
  description = "ECR repository name (e.g., frontend)"
}

variable "env" {
  type        = string
  description = "Environment (dev/prod)"
  validation {
    condition     = contains(["dev", "prod"], var.env)
    error_message = "Environment must be dev or prod"
  }
}

variable "image_retention_days" {
  type        = number
  description = "Number of days to retain untagged images"
  default     = 90 # 3ヶ月
}

variable "image_tag_mutability" {
  type        = string
  description = "Image tag mutability (MUTABLE or IMMUTABLE)"
  validation {
    condition     = contains(["MUTABLE", "IMMUTABLE"], var.image_tag_mutability)
    error_message = "image_tag_mutability must be MUTABLE or IMMUTABLE"
  }
}

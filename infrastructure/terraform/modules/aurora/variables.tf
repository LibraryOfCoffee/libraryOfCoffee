variable "account_id" {
  type        = string
  description = "AWS account ID"
}

variable "env" {
  type        = string
  description = "Environment(dev/prod)"
  validation {
    condition     = contains(["dev", "prod"], var.env)
    error_message = "Environment must be dev or prod"
  }
}

variable "vpc_id" {
  type        = string
  description = "VPC ID"
}

variable "private_subnet_ids" {
  type        = list(string)
  description = "Private subnet IDs for Aurora"
}

variable "allowed_security_group_ids" {
  type        = list(string)
  description = "Security group IDs allowed to access Aurora (e.g., backend ECS)"
  default     = []
}

variable "instance_class" {
  type        = string
  description = "Aurora instance class"
}

variable "instance_count" {
  type        = number
  description = "Number of Aurora instances"
}

variable "engine_version" {
  type        = string
  description = "Aurora MySQL engine version"
}

variable "backup_retention_period" {
  type        = number
  description = "Backup retention period in days"
}

variable "skip_final_snapshot" {
  type        = bool
  description = "Skip final snapshot on deletion"
}

variable "deletion_protection" {
  type        = bool
  description = "Enable deletion protection"
}

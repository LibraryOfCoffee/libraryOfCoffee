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
  default     = "db.t3.medium"
}

variable "instance_count" {
  type        = number
  description = "Number of Aurora instances"
  default     = 1
}

variable "engine_version" {
  type        = string
  description = "Aurora MySQL engine version"
  default     = "8.0.mysql_aurora.3.05.2"
}

variable "backup_retention_period" {
  type        = number
  description = "Backup retention period in days"
  default     = 7
}

variable "skip_final_snapshot" {
  type        = bool
  description = "Skip final snapshot on deletion"
  default     = false
}

variable "deletion_protection" {
  type        = bool
  description = "Enable deletion protection"
  default     = true
}

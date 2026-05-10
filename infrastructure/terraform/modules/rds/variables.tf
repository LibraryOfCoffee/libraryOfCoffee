variable "account_id" {
  type        = string
  description = "AWS account ID"
}

variable "env" {
  type        = string
  description = "Environment (dev/prod)"
}

variable "vpc_id" {
  type        = string
  description = "VPC ID"
}

variable "private_subnet_ids" {
  type        = list(string)
  description = "Private subnet IDs"
}

variable "allowed_security_group_ids" {
  type        = list(string)
  description = "Security group IDs allowed to access RDS"
}

variable "instance_class" {
  type        = string
  description = "RDS instance class"
}

variable "db_name" {
  type        = string
  description = "Database name"
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

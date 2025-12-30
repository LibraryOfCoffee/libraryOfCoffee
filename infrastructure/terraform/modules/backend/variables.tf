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
  description = "Private subnet IDs for ECS tasks"
}

variable "ecr_repository_url" {
  type        = string
  description = "ECR repository URL"
}

variable "image_tag" {
  type        = string
  description = "Docker image tag"
}

variable "container_port" {
  type        = number
  description = "Container port"
}

variable "cpu" {
  type        = number
  description = "Fargate CPU units (256, 512, 1024, 2048, 4096)"
}

variable "memory" {
  type        = number
  description = "Fargate memory (MB)"
}

variable "allowed_security_group_ids" {
  type        = list(string)
  description = "Security group IDs allowed to access backend (e.g., frontend ECS)"
}

# Aurora connection settings
variable "db_host" {
  type        = string
  description = "Aurora database host endpoint"
}

variable "db_port" {
  type        = number
  description = "Aurora database port"
}

variable "db_name" {
  type        = string
  description = "Aurora database name"
}

variable "db_secrets_arn" {
  type        = string
  description = "Secrets Manager ARN for database credentials"
}

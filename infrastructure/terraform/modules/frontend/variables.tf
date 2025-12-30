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

variable "public_subnet_ids" {
  type        = list(string)
  description = "Public subnet IDs for ALB"
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
  default     = "latest"
}

variable "container_port" {
  type        = number
  description = "Container port"
  default     = 3000
}

variable "cpu" {
  type        = number
  description = "Fargate CPU units (256, 512, 1024, 2048, 4096)"
  default     = 256
}

variable "memory" {
  type        = number
  description = "Fargate memory (MB)"
  default     = 512
}

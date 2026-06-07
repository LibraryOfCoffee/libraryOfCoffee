variable "env" {
  type        = string
  description = "Environment (dev/prod)"
}

variable "vpc_id" {
  type        = string
  description = "VPC ID"
}

variable "subnet_id" {
  type        = string
  description = "Public subnet ID for the bastion instance"
}

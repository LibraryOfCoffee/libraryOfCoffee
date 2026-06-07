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

variable "running" {
  type        = bool
  default     = false
  description = "trueにすると起動状態を維持する。デフォルトは停止状態（必要時のみ手動起動）"
}

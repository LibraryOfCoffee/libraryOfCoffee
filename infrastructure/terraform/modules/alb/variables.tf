variable "account_id" {
  type = string
}

variable "env" {
  type = string
}

variable "name" {
  type        = string
  description = "ALBの識別名 (例: admin)"
}

variable "vpc_id" {
  type = string
}

variable "public_subnet_ids" {
  type = list(string)
}

variable "allowed_cidr_blocks" {
  type        = list(string)
  description = "ALBへのHTTP/HTTPSアクセスを許可するCIDRブロック"
}

variable "certificate_arn" {
  type        = string
  description = "HTTPSリスナーに使用するACM証明書ARN"
}

variable "host_header" {
  type        = string
  description = "リスナールールのホストヘッダー条件 (例: dev.admin.example.com)"
}

variable "container_port" {
  type        = number
  description = "ターゲットグループのコンテナポート"
}

variable "health_check_path" {
  type    = string
  default = "/api/health"
}

variable "access_logs_bucket" {
  type        = string
  description = "ALBアクセスログを送るS3バケット名。空文字の場合はログ無効"
  default     = ""
}

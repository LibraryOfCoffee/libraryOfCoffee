variable "env" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "service_names" {
  type        = list(string)
  description = "作成するサービスディスカバリーサービス名のリスト"
  default     = []
}

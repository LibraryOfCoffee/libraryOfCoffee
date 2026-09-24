variable "env" {
  type = string
}

variable "name" {
  type        = string
  description = "Web ACLの識別名"
  default     = "main"
}

variable "alb_arn" {
  type        = string
  description = "WAFをアタッチするALBのARN"
}

variable "rate_limit" {
  type        = number
  description = "評価ウィンドウ内で同一IPからのリクエストを許容する上限数"
  default     = 2000
}

variable "rate_limit_window_sec" {
  type        = number
  description = "レート制限の評価ウィンドウ（秒）"
  default     = 300
}

variable "log_retention_days" {
  type    = number
  default = 30
}

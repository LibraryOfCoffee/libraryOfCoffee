variable "env" {
  type = string
}

variable "name" {
  type        = string
  description = "サービス識別名。ターゲットグループ名 (env-name-tg) に使われる"
}

variable "vpc_id" {
  type = string
}

variable "listener_arn" {
  type        = string
  description = "証明書とルールをアタッチするHTTPSリスナーのARN"
}

variable "certificate_arn" {
  type        = string
  description = "このサービスのACM証明書ARN (SNIでリスナーに追加される)"
}

variable "container_port" {
  type = number
}

variable "host_header" {
  type        = string
  description = "ルーティング条件となるホストヘッダー (例: api.mametosho.com)"
}

variable "health_check_path" {
  type    = string
  default = "/api/health"
}

variable "priority" {
  type        = number
  description = "リスナールールの優先度 (数値が小さいほど優先)"
}

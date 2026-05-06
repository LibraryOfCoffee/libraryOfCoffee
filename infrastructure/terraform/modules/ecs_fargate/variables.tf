variable "account_id" {
  type = string
}

variable "env" {
  type = string
}

variable "service_name" {
  type        = string
  description = "ECSサービス名 (例: admin-frontend, admin-api)"
}

variable "container_name" {
  type        = string
  description = "コンテナ名"
}

variable "vpc_id" {
  type = string
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "ecs_security_group_id" {
  type        = string
  description = "ECSタスクに付与するセキュリティグループID"
}

variable "container_port" {
  type = number
}

variable "cpu" {
  type = number
}

variable "memory" {
  type = number
}

variable "image_url" {
  type = string
}

variable "environment" {
  type = list(object({
    name  = string
    value = string
  }))
  default = []
}

variable "secrets" {
  type = list(object({
    name      = string
    valueFrom = string
  }))
  default = []
}

variable "log_retention_days" {
  type    = number
  default = 30
}

variable "target_group_arn" {
  type        = string
  default     = null
  description = "ALBターゲットグループARN (ALB連携時に指定)"
}

variable "service_registry_arn" {
  type        = string
  default     = null
  description = "Service Discovery サービスARN (Cloud Map連携時に指定)"
}

variable "secret_arns" {
  type        = list(string)
  default     = []
  description = "実行ロールにGetSecretValue権限を付与するSecrets Manager ARNのリスト"
}

variable "s3_bucket_arn" {
  type        = string
  default     = null
  description = "S3アクセス用タスクロールを作成するバケットARN (指定時のみタスクロールを作成)"
}

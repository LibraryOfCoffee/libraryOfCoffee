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

variable "subnet_ids" {
  type        = list(string)
  description = "ECSタスクを配置するサブネットのIDリスト"
}

variable "ingress_from_sg_id" {
  type        = string
  description = "ECSタスクへのingressを許可するセキュリティグループID"
}

variable "ingress_description" {
  type        = string
  default     = "allowed source"
  description = "ingressルールの説明"
}

variable "container_port" {
  type = number
}

# cpu/memory は ECSサービス作成時の「初期タスク定義(seed)」にのみ使われる。
# 実行中のスペックは ecspresso(ecs-task-def.json)が deploy 時に上書きするため、
# ここの値は実効スペックではない（aws_ecs_service の ignore_changes = task_definition を参照）。
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
  description = "S3アクセス用タスクロールに付与するバケットARN"
}

variable "enable_s3_access" {
  type        = bool
  default     = false
  description = "S3アクセス用タスクロールを作成するかどうか"
}

variable "rds_security_group_id" {
  type        = string
  default     = null
  description = "RDS SGのID (ingressルール追加先)"
}

variable "enable_rds_access" {
  type        = bool
  default     = false
  description = "RDS SGへのingressルールを追加するかどうか"
}

variable "use_spot" {
  type        = bool
  default     = true
  description = "true: FARGATE_SPOT 100% (コスト優先) / false: FARGATE オンデマンド 100% (可用性優先)"
}

locals {
  account_id = data.aws_caller_identity.current.account_id
  env        = "prod"

  config              = yamldecode(file("../../files/config/${local.env}.yml"))
  allowed_cidr_blocks = local.config.allowed_cidr_blocks
}

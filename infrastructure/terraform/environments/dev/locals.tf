locals {
  account_id = "446468848828"
  env        = "dev"

  config              = yamldecode(file("../../files/config/${local.env}.yml"))
  allowed_cidr_blocks = local.config.allowed_cidr_blocks
}

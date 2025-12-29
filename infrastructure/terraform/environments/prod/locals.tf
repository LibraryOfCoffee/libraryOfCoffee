locals {
  # 豆図書のAWSアカウントID
  account_id = "446468848828"
  env        = "prod"

  config = yamldecode(file("../../files/config/${local.env}.yml"))
}
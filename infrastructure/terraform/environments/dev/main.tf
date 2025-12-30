module "vpc" {
  source = "../../modules/vpc"
}

module "cd" {
  source = "../../modules/cd"

  account_id  = local.account_id
  env         = local.env
  github_org  = "LibraryOfCoffee"
  github_repo = "libraryOfCoffee"
}

module "ecr_frontend" {
  source = "../../modules/ecr"

  repository_name      = "frontend"
  env                  = local.env
  image_retention_days = 90 # 3ヶ月
  image_tag_mutability = "MUTABLE"
}

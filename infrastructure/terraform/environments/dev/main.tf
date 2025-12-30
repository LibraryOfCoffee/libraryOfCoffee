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
  image_retention_days = 90
  image_tag_mutability = "MUTABLE"
}

module "frontend" {
  source = "../../modules/frontend"

  account_id         = local.account_id
  env                = local.env
  vpc_id             = module.vpc.vpc_id
  public_subnet_ids  = module.vpc.public_subnet_ids
  private_subnet_ids = module.vpc.private_subnet_ids
  ecr_repository_url = module.ecr_frontend.repository_url
}

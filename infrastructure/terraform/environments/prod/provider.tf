terraform {
  required_version = "~> 1.14.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.27.0"
    }
  }

  backend "s3" {
    region       = "ap-northeast-1"
    bucket       = "mametosho-terraform-state-prod"
    key          = "prod/terraform.tfstate"
    use_lockfile = true
  }
}

provider "aws" {
  region = "ap-northeast-1"
  default_tags {
    tags = {
      Env   = "mametosho-terraform"
      Owner = "ysato"
    }
  }
}

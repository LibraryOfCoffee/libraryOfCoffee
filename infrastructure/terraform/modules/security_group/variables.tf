variable "name" {
  type = string
}

variable "description" {
  type = string
}

variable "vpc_id" {
  type = string
}

variable "ingress_with_cidr_rules" {
  type = list(object({
    from_port   = number
    to_port     = number
    cidr_blocks = list(string)
    description = string
  }))
  default = []
}

variable "ingress_with_sg_rules" {
  type = list(object({
    from_port   = number
    to_port     = number
    sg_id       = string
    description = string
  }))
  default = []
}

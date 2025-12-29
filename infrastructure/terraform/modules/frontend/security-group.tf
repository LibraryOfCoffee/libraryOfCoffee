resource "aws_security_group" "main" {
  name        = "${var.account_id}-${var.env}-frontend"
  description = "front security group"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
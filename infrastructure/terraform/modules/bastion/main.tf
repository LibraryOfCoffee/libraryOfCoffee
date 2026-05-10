data "aws_caller_identity" "current" {}

data "aws_ami" "amazon_linux_2023" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-2023*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_iam_role" "main" {
  name = "mametosho-${var.env}-bastion"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action    = "sts:AssumeRole"
      Effect    = "Allow"
      Principal = { Service = "ec2.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ssm" {
  role       = aws_iam_role.main.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_role_policy" "secretsmanager" {
  name = "secretsmanager-read"
  role = aws_iam_role.main.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect   = "Allow"
      Action   = ["secretsmanager:GetSecretValue"]
      Resource = "arn:aws:secretsmanager:ap-northeast-1:${data.aws_caller_identity.current.account_id}:secret:*"
    }]
  })
}

resource "aws_iam_role_policy" "ssm_parameter_store" {
  name = "ssm-parameter-store-read"
  role = aws_iam_role.main.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect   = "Allow"
      Action   = ["ssm:GetParameter", "ssm:GetParameters"]
      Resource = "arn:aws:ssm:ap-northeast-1:${data.aws_caller_identity.current.account_id}:parameter/*"
    }]
  })
}

resource "aws_iam_instance_profile" "main" {
  name = "mametosho-${var.env}-bastion"
  role = aws_iam_role.main.name
}

resource "aws_security_group" "main" {
  name        = "mametosho-${var.env}-bastion"
  description = "Security group for bastion EC2"
  vpc_id      = var.vpc_id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "main" {
  ami                         = data.aws_ami.amazon_linux_2023.id
  instance_type               = "t3.micro"
  subnet_id                   = var.subnet_id
  iam_instance_profile        = aws_iam_instance_profile.main.name
  associate_public_ip_address = true

  vpc_security_group_ids      = [aws_security_group.main.id]
  user_data_replace_on_change = true

  user_data = <<-EOF
    #!/bin/bash
    dnf install -y jq mysql git

    LATEST_URL=$(curl -fsSL "https://api.github.com/repos/sqldef/sqldef/releases/latest" \
      | grep '"browser_download_url"' \
      | grep 'mysqldef_linux_amd64\.tar\.gz' \
      | head -1 \
      | sed 's/.*"browser_download_url": "\(.*\)"/\1/')
    TMP_DIR=$(mktemp -d)
    curl -fsSL "$LATEST_URL" -o "$TMP_DIR/mysqldef.tar.gz"
    tar -xzf "$TMP_DIR/mysqldef.tar.gz" -C "$TMP_DIR"
    install -m 0755 "$TMP_DIR/mysqldef" /usr/local/bin/mysqldef
    rm -rf "$TMP_DIR"
  EOF

  tags = {
    Name = "mametosho-${var.env}-bastion"
  }
}

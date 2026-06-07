resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/22"
  enable_dns_hostnames = true
  enable_dns_support   = true
}

resource "aws_subnet" "private-1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.0.0/24"
  availability_zone = "ap-northeast-1a"
}

resource "aws_subnet" "private-2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "ap-northeast-1c"
}

resource "aws_subnet" "public-1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "ap-northeast-1a"
}

resource "aws_subnet" "public-2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "ap-northeast-1c"
}

resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id
}

resource "aws_route_table" "main" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
}

resource "aws_route_table_association" "public-1" {
  subnet_id      = aws_subnet.public-1.id
  route_table_id = aws_route_table.main.id
}

resource "aws_route_table_association" "public-2" {
  subnet_id      = aws_subnet.public-2.id
  route_table_id = aws_route_table.main.id
}

# ============================================
# VPC Endpoints
# ============================================

# S3用 (Gateway型) - 無料のため残す
resource "aws_vpc_endpoint" "s3" {
  vpc_id            = aws_vpc.main.id
  service_name      = "com.amazonaws.ap-northeast-1.s3"
  vpc_endpoint_type = "Gateway"
  route_table_ids   = [aws_route_table.private.id]
}

# プライベートサブネット用ルートテーブル
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.main.id
}

resource "aws_route_table_association" "private-1" {
  subnet_id      = aws_subnet.private-1.id
  route_table_id = aws_route_table.private.id
}

resource "aws_route_table_association" "private-2" {
  subnet_id      = aws_subnet.private-2.id
  route_table_id = aws_route_table.private.id
}
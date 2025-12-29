resource "aws_vpc" "main" {
  cidr_block           = "10.0.0.0/22"
  enable_dns_hostnames = true
}

# 10.0.1.0/24は将来的に冗長化のために予約しておく(private-2)
resource "aws_subnet" "private-1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.0.0/24"
  availability_zone = "ap-northeast-1a"
}

# 10.0.3.0/24は将来的に冗長化のために予約しておく(public-2)
resource "aws_subnet" "public-1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "ap-northeast-1a"
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

resource "aws_route_table_association" "main" {
  subnet_id      = aws_subnet.public-1.id
  route_table_id = aws_route_table.main.id
}
# Aurora MySQL Cluster
resource "aws_rds_cluster" "main" {
  cluster_identifier = "${var.account_id}-${var.env}-aurora"
  engine             = "aurora-mysql"
  engine_version     = var.engine_version
  database_name      = "coffee"
  master_username    = "admin"
  # パスワードはSecrets Managerで自動生成
  manage_master_user_password = true

  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.main.id]

  backup_retention_period = var.backup_retention_period
  preferred_backup_window = "03:00-04:00"

  skip_final_snapshot       = var.skip_final_snapshot
  final_snapshot_identifier = var.skip_final_snapshot ? null : "${var.account_id}-${var.env}-aurora-final"

  deletion_protection = var.deletion_protection

  # CloudWatch Logsへのエクスポート
  enabled_cloudwatch_logs_exports = ["error", "slowquery"]

  tags = {
    Name = "${var.account_id}-${var.env}-aurora"
    Env  = var.env
  }
}

# Aurora Instance
resource "aws_rds_cluster_instance" "main" {
  count = var.instance_count

  identifier         = "${var.account_id}-${var.env}-aurora-${count.index + 1}"
  cluster_identifier = aws_rds_cluster.main.id
  instance_class     = var.instance_class
  engine             = aws_rds_cluster.main.engine
  engine_version     = aws_rds_cluster.main.engine_version

  publicly_accessible = false

  tags = {
    Name = "${var.account_id}-${var.env}-aurora-${count.index + 1}"
    Env  = var.env
  }
}

# DB Subnet Group
resource "aws_db_subnet_group" "main" {
  name       = "${var.account_id}-${var.env}-aurora"
  subnet_ids = var.private_subnet_ids

  tags = {
    Name = "${var.account_id}-${var.env}-aurora"
    Env  = var.env
  }
}

#!/bin/bash
set -euo pipefail

ENV=${1:-}

if [[ "$ENV" != "dev" && "$ENV" != "prod" ]]; then
  echo "Usage: $0 <dev|prod>"
  exit 1
fi

BUCKET="mametosho-terraform-state-${ENV}"
REGION="ap-northeast-1"

echo "Creating S3 bucket: ${BUCKET}"
aws s3 mb "s3://${BUCKET}" --region "${REGION}"

echo "Enabling versioning..."
aws s3api put-bucket-versioning \
  --bucket "${BUCKET}" \
  --versioning-configuration Status=Enabled

echo "Done. Run 'terraform init' in environments/${ENV}/"

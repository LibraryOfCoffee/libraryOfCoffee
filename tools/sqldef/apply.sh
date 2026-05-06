#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCHEMA_FILE="${SCRIPT_DIR}/../../infrastructure/db/schema.sql"
REGION="ap-northeast-1"
DB_NAME="mametosho"
DB_PORT="3306"

usage() {
  cat <<EOF
使い方: $(basename "$0") <ENV> [--apply [--allow-drop]]

引数:
  ENV          環境名 (dev | prod)
  --apply      実際にDDLを適用する (省略時はdry-run)
  --allow-drop DROP文も実行する (省略時はDROPをスキップ、--applyと併用)

例:
  $(basename "$0") dev                       # dry-run で差分確認
  $(basename "$0") dev --apply               # DROP除外で適用
  $(basename "$0") dev --apply --allow-drop  # DROP含めて適用
EOF
  exit 1
}

log() { echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*"; }
err() { echo "[$(date '+%Y-%m-%d %H:%M:%S')] ERROR: $*" >&2; }

# ---------- 引数チェック ----------
ENV="${1:-}"
APPLY=false
ALLOW_DROP=false

if [[ -z "$ENV" ]]; then
  err "ENV 引数が必要です"
  usage
fi
if [[ "$ENV" != "dev" && "$ENV" != "prod" ]]; then
  err "ENV は 'dev' または 'prod' を指定してください (指定値: '${ENV}')"
  usage
fi
for arg in "${@:2}"; do
  case "$arg" in
    --apply)      APPLY=true ;;
    --allow-drop) ALLOW_DROP=true ;;
    *) err "不明な引数: $arg"; usage ;;
  esac
done
if [[ "$ALLOW_DROP" == true && "$APPLY" == false ]]; then
  err "--allow-drop は --apply と併用してください"
  usage
fi

if [[ ! -f "$SCHEMA_FILE" ]]; then
  err "スキーマファイルが見つかりません: ${SCHEMA_FILE}"
  exit 1
fi

log "環境: ${ENV} | dry-run: $([[ $APPLY == true ]] && echo false || echo true) | allow-drop: ${ALLOW_DROP}"

# ---------- mysqldef 確認 ----------
if ! command -v mysqldef &>/dev/null; then
  err "mysqldef が見つかりません。踏み台サーバ上で実行してください"
  exit 1
fi

# ---------- AWS から接続情報を取得 ----------
log "RDS エンドポイントを取得します (SSM: /${ENV}/admin-api/rds-endpoint)..."
RDS_ENDPOINT=$(
  aws ssm get-parameter \
    --name "/${ENV}/admin-api/rds-endpoint" \
    --region "${REGION}" \
    --query "Parameter.Value" \
    --output text
)
if [[ -z "$RDS_ENDPOINT" ]]; then
  err "RDS エンドポイントの取得に失敗しました"
  exit 1
fi

log "DB シークレット ARN を取得します (SSM: /${ENV}/admin-api/db-secret-arn)..."
DB_SECRET_ARN=$(
  aws ssm get-parameter \
    --name "/${ENV}/admin-api/db-secret-arn" \
    --region "${REGION}" \
    --query "Parameter.Value" \
    --output text
)
if [[ -z "$DB_SECRET_ARN" ]]; then
  err "DB シークレット ARN の取得に失敗しました"
  exit 1
fi

log "DB 認証情報を取得します (Secrets Manager)..."
DB_SECRET_JSON=$(
  aws secretsmanager get-secret-value \
    --secret-id "${DB_SECRET_ARN}" \
    --region "${REGION}" \
    --query "SecretString" \
    --output text
)

DB_USERNAME=$(echo "${DB_SECRET_JSON}" | jq -r '.username')
DB_PASSWORD=$(echo "${DB_SECRET_JSON}" | jq -r '.password')

if [[ -z "$DB_USERNAME" || "$DB_USERNAME" == "null" || -z "$DB_PASSWORD" || "$DB_PASSWORD" == "null" ]]; then
  err "DB 認証情報の取得に失敗しました"
  exit 1
fi

log "接続先: ${DB_USERNAME}@${RDS_ENDPOINT}:${DB_PORT}/${DB_NAME}"

# ---------- mysqldef 実行 ----------
MYSQLDEF_ARGS=(-u "${DB_USERNAME}" -h "${RDS_ENDPOINT}" -P "${DB_PORT}")

if [[ "$APPLY" == false ]]; then
  log "--- dry-run モードで実行します (DDL は適用されません) ---"
  MYSQLDEF_ARGS+=(--dry-run)
else
  log "--- DDL を適用します (DROP: $([[ $ALLOW_DROP == true ]] && echo 有効 || echo スキップ)) ---"
  if [[ "$ALLOW_DROP" == false ]]; then
    MYSQLDEF_ARGS+=(--skip-drop)
  fi
fi

# パスワードは MYSQL_PWD 経由で渡す (コマンドライン引数への露出を防ぐ)
MYSQL_PWD="${DB_PASSWORD}" mysqldef "${MYSQLDEF_ARGS[@]}" "${DB_NAME}" < "${SCHEMA_FILE}"

log "完了"

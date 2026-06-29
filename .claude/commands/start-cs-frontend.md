---
allowed-tools: Bash
description: "CS Frontendをローカル起動する (port 3000)"
---

# start-cs-frontend

CS Frontendを開発モードで起動する。

## 手順

1. ポート3000が既に使用されている場合は、そのプロセスをkillする:

```shell
lsof -ti :3000 | xargs kill 2>/dev/null || true
```

2. frontendディレクトリで以下のコマンドを実行する:

```shell
cd frontend && CS_API_HMAC_KEY_SSR=4cc1009cc9bfeb01bcce0c7a8418d9ea3fe7cf9e3e30bff3 pnpm dev
```

   `CS_API_HMAC_KEY_SSR` はCS APIへのHMAC署名に使うSSR用クライアント秘密鍵。
   ここで渡す値はローカル専用の平文で、`infrastructure/db/local-data.sql` の
   `ssr-frontend` の `encrypted_secret`（`application-local.yml` のマスター鍵で
   AES-GCM暗号化したもの）を復号した平文と一致している。本番/dev環境では
   Secrets Manager から注入されるため、この固定値はローカルでのみ使用する。

3. 起動が完了したら、ユーザーに `http://localhost:3000` でアクセス可能であることを伝える

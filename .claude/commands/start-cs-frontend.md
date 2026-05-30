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
cd frontend && pnpm dev
```

3. 起動が完了したら、ユーザーに `http://localhost:3000` でアクセス可能であることを伝える

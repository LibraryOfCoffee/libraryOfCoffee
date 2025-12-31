---
allowed-tools: Bash(git:*)
description: "変更差分を検知し、commit/pushを行う"
---

# commit-push

変更をコミットして現在のブランチにpushするコマンド。

## 手順

1. `git status` と `git diff --staged` と `git diff` を実行して、すべての変更を確認する
2. 最近のコミット履歴を `git log --oneline -10` で確認し、コミットメッセージのスタイルを把握する
3. 変更内容を分析し、適切なコミットメッセージを作成する
   - Conventional Commits形式を使用（feat, fix, docs, style, refactor, test, chore など）
   - 変更の本質を簡潔に表現する
4. 変更をステージング（`git add`）してコミットする
5. 現在のブランチにpushする（`git push origin HEAD`）

## 重要な制約

- **force pushは絶対に行わない**（`--force`, `-f`, `--force-with-lease` は使用禁止）
- pushが失敗した場合は、ユーザーに状況を報告し、手動での対応を促す
- 空のコミットは作成しない（変更がない場合はその旨を報告する）

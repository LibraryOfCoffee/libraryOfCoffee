@import backend/CLAUDE.md
@import frontend/CLAUDE.md
@import admin-frontend/CLAUDE.md

## Swagger YMLの編集について
`docs/swagger/*.yml` などのSwaggerファイルは**直接編集しないこと**。
バックエンドのコード（アノテーションや設定）から自動生成されるため、手動で変更してもビルド時に上書きされる。
Swagger定義を変更したい場合は、バックエンドのソースコードを修正すること。

## セットアップ
コード開発を始める前に、以下のコマンドを実行してください。
```shell
mise trust
mise install
```

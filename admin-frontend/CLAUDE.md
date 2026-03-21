# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## コマンド

```shell
pnpm dev       # 開発サーバー起動
pnpm build     # ビルド
pnpm lint      # lint（コード変更後は必ず実行）
pnpm format    # フォーマット
```

テストフレームワークは未導入。

## 技術スタック

- Next.js 16 (App Router) / React 19 / TypeScript (strict)
- Biome 2.2 — linter + formatter（2スペースインデント、ダブルクォート）
- React Compiler 有効
- パスエイリアス: `@/*` → `./src/*`

## 設計原則

[Next.jsの考え方](https://zenn.dev/akfm/books/nextjs-basic-principle)（[GitHub](https://github.com/AkifumiSato/zenn-article/tree/main/books/nextjs-basic-principle)）に基づく。

### 第1部: データフェッチ

- データフェッチはClient Componentsではなく**Server Componentsで行う**。3rd partyライブラリ（SWR, React Query等）やクライアントサイドでのfetchは不要。Server Componentsは非同期関数をサポートしており、シンプルにデータ取得を実装できる。
- データフェッチは**データを参照するコンポーネントにコロケーション**する。ページコンポーネントで一括取得してprops drilling（バケツリレー）するのではなく、末端のコンポーネントで必要なデータを直接取得する。
- 同一リクエスト内の重複fetchは**Request Memoization**で自動排除されるため、複数コンポーネントで同じfetch関数を呼んでも問題ない。ただしRequest Memoizationを活かすために、データフェッチ処理は関数として分離・共通化する。
- データフェッチが可能な限り**並行**になるよう設計する。兄弟コンポーネントに分割する（自動的に並行レンダリングされる）、`Promise.all()`を使う、preloadパターンを使うなどの方法がある。
- コンポーネント単位の独立性を高めると**N+1データフェッチ**が発生しやすくなるので、DataLoaderのバッチ処理で解消する。`React.cache()`でDataLoaderインスタンスをリクエスト単位にスコープする。
- Next.jsが呼び出すバックエンドAPIは**細粒度**な単位に分割されていることが望ましい。RSCのサーバー間通信は高速なため、通信回数が多くても問題になりにくい。RSCはGraphQLの精神的後継であり、細粒度APIと非常に相性が良い。
- ユーザー操作に基づくデータフェッチと再レンダリングには**Server Functionsと`useActionState()`**を利用する。

### 第2部: コンポーネント設計

- `"use client"`と`"use server"`は実行環境を示すものではなく、**バンドル境界**を宣言するためのもの。`"use client"`はClient Boundary（サーバー→クライアント）、`"use server"`はServer Boundary（クライアント→サーバー）。Client Boundaryとなるモジュールからimportされるモジュールとその子孫は暗黙的に全てClient Bundleに含まれる。
- Server Bundleでのみ利用すべきモジュール（データフェッチ層など）は`server-only`で保護する。
- Client Componentsを使うべき代表的なユースケース: (1)イベントハンドラ・hooks・ブラウザAPIなどの**クライアントサイド処理**、(2)RSC未対応の**サードパーティコンポーネント**（Swiper等。`"use client"`でre-exportするか利用側で宣言）、(3)**RSC Payload転送量の削減**（大量のReactElementを返すServer ComponentはClient Componentsに分離するとPayloadが軽くなる場合がある）。
- **Compositionパターン**を活用してClient Componentsを分離する。Client BundleはServer Componentsをimportできないが、Client Componentsの`children`等のpropsにServer Componentsを渡すことは可能。
- **UIをツリーに分解する**ことから設計を始める（トップダウン設計）。データフェッチコロケーションとCompositionパターンを早期適用するため、(1)UIをツリーに分解 → (2)コンポーネントツリーを仮実装 → (3)各コンポーネントの詳細実装の順で進める。
- **Container/Presentationalパターン**でテスト容易性を向上させる。Container Components（Server Components、データフェッチ担当）とPresentational Components（Shared/Client Components、表示担当）に分離する。Presentational ComponentsはRTLやStorybookで従来通りテスト可能。

### 第3部: キャッシュ

- **Static Renderingをデフォルト**とし、Dynamic Renderingは必要な場合のみオプトインする。Dynamic APIs（`cookies()`/`headers()`等）の使用、`cache: "no-store"`なfetch、Route Segment Config（`dynamic = "force-dynamic"`）等でDynamic Renderingに切り替わる。
- Static RenderingではFull Route Cache（HTMLやRSC Payloadのキャッシュ）が生成される。ユーザー固有情報を含まないページは積極的にFull Route Cacheを活用する。`revalidatePath()`/`revalidateTag()`でオンデマンドrevalidate、Route Segment Configの`revalidate`で時間ベースrevalidateが可能。
- Dynamic RenderingなページではData Cache（データフェッチ単位のキャッシュ）を活用してパフォーマンスを最適化する。ユーザー固有でないデータフェッチにはData Cacheを設定する。
- Router Cache（クライアントサイドキャッシュ）の有効期間は`staleTimes`で設定可能。Server Actionsで`revalidatePath()`/`revalidateTag()`を呼ぶとRouter Cacheも破棄される。
- **データ操作はServer Actionsで実装する**。`revalidatePath()`/`revalidateTag()`でキャッシュをrevalidateし、`redirect()`でリダイレクト先のRSC Payloadを1往復で返せる。

### 第4部: レンダリング

- Server Componentsのレンダリングは**純粋**であるべき。データフェッチは副作用だが、Request Memoizationにより同一リクエスト内で冪等性が保たれる。`fetch()`を使わないデータアクセス（DB等）には`React.cache()`でメモ化する。Cookie操作（`.set()`/`.delete()`）はServer Componentsでは行えず、Server ActionsかRoute Handlerで行う。
- 重いServer Componentsのレンダリングは`<Suspense>`で遅延させて**Streaming SSR**にする。fallbackのLayout Shiftとのトレードオフを考慮し、200ms程度ならTTFBより CLS優先、1s超なら遅延を選ぶ。

### 第5部: その他

- Server ComponentsやServer Functionsではリクエスト/レスポンスオブジェクト（`req`/`res`）を直接参照できない。代わりにNext.jsが提供するAPI（`params` props, `searchParams` props, `headers()`, `cookies()`, `notFound()`, `redirect()`等）を使う。
- **認可チェックをlayout.tsxで行わない**。ページとレイアウトは並行レンダリングされるため、レイアウトの認可チェックがページより先に実行される保証がない。各`page.tsx`やデータフェッチ層で認可チェックを行う。
- Server Functionsの予測可能なエラーは`throw`ではなく**戻り値で表現**する。エラーをthrowすると`error.tsx`が表示されformの入力内容が失われるため。

### ナビゲーション

- 内部遷移: `<Link>`を使う（`useRouter`の`router.push`ではなく）
- 外部URL遷移: `<a>`タグを使う（JSが不要なのでbutton+onClickではなく）

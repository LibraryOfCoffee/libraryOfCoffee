"use client";

export default function GlobalError({
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  // global-errorはルートレイアウトごと置き換わるためCSS Modulesに依存しない
  return (
    <html lang="ja">
      <body>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            minHeight: "100vh",
            gap: "16px",
            padding: "24px",
          }}
        >
          <h1 style={{ fontSize: "20px", fontWeight: 600 }}>
            エラーが発生しました
          </h1>
          <p style={{ fontSize: "14px", color: "#666" }}>
            問題が解決しない場合は、ページを再読み込みしてください。
          </p>
          <button
            type="button"
            onClick={reset}
            style={{
              padding: "10px 24px",
              fontSize: "14px",
              fontWeight: 600,
              color: "#ffffff",
              background: "#8b6914",
              border: "none",
              borderRadius: "6px",
              cursor: "pointer",
            }}
          >
            再試行
          </button>
        </div>
      </body>
    </html>
  );
}

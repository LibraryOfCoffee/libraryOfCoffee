"use client";

import Link from "next/link";
import "./globals.css";

export default function ErrorPage({
  error: _error,
  reset: _reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        padding: "20px",
      }}
    >
      <div
        style={{
          textAlign: "center",
          maxWidth: "500px",
        }}
      >
        <h1
          style={{
            fontSize: "80px",
            fontWeight: "bold",
            margin: "0 0 20px 0",
          }}
        >
          500
        </h1>

        <p
          style={{
            fontSize: "18px",
            marginBottom: "30px",
            color: "#666",
          }}
        >
          エラーが発生しました
        </p>

        <Link
          href="/lp"
          style={{
            display: "inline-block",
            padding: "12px 30px",
            fontSize: "16px",
            color: "#333",
            border: "1px solid #333",
            textDecoration: "none",
          }}
        >
          LPに戻る
        </Link>
      </div>
    </div>
  );
}

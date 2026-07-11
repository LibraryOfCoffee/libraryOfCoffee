"use client";

import styles from "@/app/error.module.css";

export default function ErrorPage({
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  return (
    <div className={styles.container}>
      <h1 className={styles.title}>エラーが発生しました</h1>
      <p className={styles.description}>
        問題が解決しない場合は、ページを再読み込みしてください。
      </p>
      <button type="button" className={styles.retryButton} onClick={reset}>
        再試行
      </button>
    </div>
  );
}

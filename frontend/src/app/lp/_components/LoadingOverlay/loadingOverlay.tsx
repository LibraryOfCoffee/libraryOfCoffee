"use client";

import styles from "./loadingOverlay.module.css";

export default function LoadingOverlay() {
  return (
    <div className={styles.overlay}>
      <div className={styles.spinner} />
    </div>
  );
}

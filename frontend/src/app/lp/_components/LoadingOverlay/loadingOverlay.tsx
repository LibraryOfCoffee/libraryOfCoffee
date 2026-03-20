"use client";

import { createPortal } from "react-dom";
import styles from "./loadingOverlay.module.css";

export default function LoadingOverlay() {
  return createPortal(
    <div className={styles.overlay}>
      <div className={styles.spinner} />
    </div>,
    document.body,
  );
}

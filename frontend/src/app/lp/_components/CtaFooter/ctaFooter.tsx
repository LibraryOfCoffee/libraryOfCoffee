"use client";

import type { ReactNode } from "react";
import { LuArrowRight } from "react-icons/lu";
import { moveToLoginPage } from "../../_lib/purchaseLinkUtil";
import styles from "./ctaFooter.module.css";

interface CtaFooterProps {
  summaryLabel: string;
  summaryValue: ReactNode;
  ctaText: ReactNode;
  onCtaClick: () => void;
  showIcon?: boolean;
  subText?: ReactNode;
  disabled?: boolean;
}

export default function CtaFooter({
  summaryLabel,
  summaryValue,
  ctaText,
  onCtaClick,
  showIcon = true,
  subText,
  disabled = false,
}: CtaFooterProps) {
  return (
    <div className={styles.footer}>
      <div className={styles.summary}>
        <span className={styles.summaryLabel}>{summaryLabel}</span>
        <span className={styles.summaryValue}>{summaryValue}</span>
      </div>
      {subText && <span className={styles.subText}>{subText}</span>}
      <button
        type="button"
        className={`${styles.btn} ${disabled ? styles.btnDisabled : ""}`}
        onClick={onCtaClick}
        disabled={disabled}
      >
        {ctaText}
        {showIcon && <LuArrowRight size={16} />}
      </button>
      <div className={styles.login}>
        <span className={styles.loginText}>すでにアカウントをお持ちの方は</span>
        <button
          type="button"
          className={styles.loginAction}
          onClick={() => moveToLoginPage()}
        >
          ログイン
        </button>
      </div>
    </div>
  );
}

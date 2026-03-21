"use client";

import { useActionState, useEffect, useId, useRef } from "react";
import styles from "@/components/modal.module.css";
import { type CreateShopState, createShopAction } from "./createShopAction";

const initialState: CreateShopState = {};

export function CreateShopModal({
  open,
  onClose,
}: {
  open: boolean;
  onClose: () => void;
}) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [state, formAction, isPending] = useActionState(
    createShopAction,
    initialState,
  );

  const shopifyIdId = useId();
  const nameId = useId();
  const introductionId = useId();
  const particularId = useId();

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;
    if (open) {
      dialog.showModal();
    } else {
      dialog.close();
    }
  }, [open]);

  useEffect(() => {
    if (state.success) {
      onClose();
    }
  }, [state.success, onClose]);

  const handleBackdropClick = (e: React.MouseEvent<HTMLDialogElement>) => {
    if (e.target === dialogRef.current) {
      onClose();
    }
  };

  return (
    <dialog
      ref={dialogRef}
      className={styles.dialog}
      onClose={onClose}
      onClick={handleBackdropClick}
      onKeyDown={(e) => {
        if (e.key === "Escape") onClose();
      }}
    >
      <div className={styles.dialogHeader}>
        <h2 className={styles.dialogTitle}>新規店舗登録</h2>
        <button
          type="button"
          className={styles.closeButton}
          onClick={onClose}
          aria-label="閉じる"
        >
          &times;
        </button>
      </div>

      <form action={formAction} className={styles.form}>
        {state.error && <div className={styles.error}>{state.error}</div>}

        <div className={styles.field}>
          <label htmlFor={shopifyIdId} className={styles.label}>
            Shopify Shop ID
            <span className={styles.required}>*</span>
          </label>
          <input
            id={shopifyIdId}
            name="shopifyShopId"
            type="text"
            maxLength={255}
            className={styles.input}
          />
          {state.fieldErrors?.shopifyShopId?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={styles.field}>
          <label htmlFor={nameId} className={styles.label}>
            店舗名
            <span className={styles.required}>*</span>
          </label>
          <input
            id={nameId}
            name="name"
            type="text"
            maxLength={255}
            className={styles.input}
          />
          {state.fieldErrors?.name?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={styles.field}>
          <label htmlFor={introductionId} className={styles.label}>
            紹介文
          </label>
          <textarea
            id={introductionId}
            name="introduction"
            maxLength={10000}
            className={styles.textarea}
          />
          {state.fieldErrors?.introduction?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={styles.field}>
          <label htmlFor={particularId} className={styles.label}>
            こだわり
          </label>
          <textarea
            id={particularId}
            name="particular"
            maxLength={10000}
            className={styles.textarea}
          />
          {state.fieldErrors?.particular?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={styles.actions}>
          <button
            type="button"
            className={styles.cancelButton}
            onClick={onClose}
          >
            キャンセル
          </button>
          <button
            type="submit"
            disabled={isPending}
            className={styles.submitButton}
          >
            {isPending ? "登録中..." : "登録"}
          </button>
        </div>
      </form>
    </dialog>
  );
}

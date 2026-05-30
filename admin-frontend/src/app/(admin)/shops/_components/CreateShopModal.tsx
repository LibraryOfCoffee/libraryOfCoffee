"use client";

import {
  startTransition,
  useActionState,
  useEffect,
  useId,
  useRef,
} from "react";
import { PREFECTURE_OPTIONS } from "@/app/(admin)/shops/_lib/prefecture";
import { ImageUploadField } from "@/components/ImageUploadField";
import styles from "@/components/modal.module.css";
import { PUBLISH_STATUS_OPTIONS } from "@/components/publishStatus";
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
  const shopUrlId = useId();
  const prefectureId = useId();
  const publishStatusId = useId();

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

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    startTransition(() => formAction(new FormData(e.currentTarget)));
  };

  return (
    <dialog
      ref={dialogRef}
      className={styles.dialog}
      onClose={onClose}
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

      <form onSubmit={handleSubmit} className={styles.form}>
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
            defaultValue={state.values?.shopifyShopId ?? ""}
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
            defaultValue={state.values?.name ?? ""}
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
            defaultValue={state.values?.introduction ?? ""}
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
            defaultValue={state.values?.particular ?? ""}
            className={styles.textarea}
          />
          {state.fieldErrors?.particular?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={styles.field}>
          <label htmlFor={shopUrlId} className={styles.label}>
            店舗URL
            <span className={styles.required}>*</span>
          </label>
          <input
            id={shopUrlId}
            name="shopUrl"
            type="url"
            maxLength={2048}
            placeholder="https://example.com"
            defaultValue={state.values?.shopUrl ?? ""}
            className={styles.input}
          />
          {state.fieldErrors?.shopUrl?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={styles.field}>
          <label htmlFor={prefectureId} className={styles.label}>
            都道府県
            <span className={styles.required}>*</span>
          </label>
          <select
            id={prefectureId}
            name="prefecture"
            defaultValue={state.values?.prefecture ?? ""}
            className={styles.input}
          >
            <option value="" disabled>
              選択してください
            </option>
            {PREFECTURE_OPTIONS.map((p) => (
              <option key={p.value} value={p.value}>
                {p.label}
              </option>
            ))}
          </select>
          {state.fieldErrors?.prefecture?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={styles.field}>
          <label htmlFor={publishStatusId} className={styles.label}>
            公開状態
            <span className={styles.required}>*</span>
          </label>
          <select
            id={publishStatusId}
            name="publishStatus"
            defaultValue={state.values?.publishStatus ?? "DRAFT"}
            className={styles.input}
          >
            {PUBLISH_STATUS_OPTIONS.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
          {state.fieldErrors?.publishStatus?.map((msg) => (
            <span key={msg} className={styles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <ImageUploadField imageTypes={[{ value: "MAIN", label: "メイン" }]} />
        <ImageUploadField
          imageTypes={[{ value: "LOGO", label: "ロゴ" }]}
          required
        />

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

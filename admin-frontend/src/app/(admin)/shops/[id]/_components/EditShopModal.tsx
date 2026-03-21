"use client";

import { useActionState, useEffect, useId, useRef } from "react";
import type { ShopDetail } from "@/api/shops";
import modalStyles from "@/components/modal.module.css";
import { type EditShopState, editShopAction } from "./editShopAction";

const initialState: EditShopState = {};

export function EditShopModal({
  shop,
  open,
  onClose,
}: {
  shop: ShopDetail;
  open: boolean;
  onClose: () => void;
}) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [state, formAction, isPending] = useActionState(
    editShopAction,
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
      className={modalStyles.dialog}
      onClose={onClose}
      onClick={handleBackdropClick}
      onKeyDown={(e) => {
        if (e.key === "Escape") onClose();
      }}
    >
      <div className={modalStyles.dialogHeader}>
        <h2 className={modalStyles.dialogTitle}>店舗編集</h2>
        <button
          type="button"
          className={modalStyles.closeButton}
          onClick={onClose}
          aria-label="閉じる"
        >
          &times;
        </button>
      </div>

      <form action={formAction} className={modalStyles.form}>
        <input type="hidden" name="id" value={shop.id} />

        {state.error && <div className={modalStyles.error}>{state.error}</div>}

        <div className={modalStyles.field}>
          <label htmlFor={shopifyIdId} className={modalStyles.label}>
            Shopify Shop ID
            <span className={modalStyles.required}>*</span>
          </label>
          <input
            id={shopifyIdId}
            name="shopifyShopId"
            type="text"
            maxLength={255}
            defaultValue={shop.shopifyShopId}
            className={modalStyles.input}
          />
          {state.fieldErrors?.shopifyShopId?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={nameId} className={modalStyles.label}>
            店舗名
            <span className={modalStyles.required}>*</span>
          </label>
          <input
            id={nameId}
            name="name"
            type="text"
            maxLength={255}
            defaultValue={shop.name}
            className={modalStyles.input}
          />
          {state.fieldErrors?.name?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={introductionId} className={modalStyles.label}>
            紹介文
          </label>
          <textarea
            id={introductionId}
            name="introduction"
            maxLength={10000}
            defaultValue={shop.introduction ?? ""}
            className={modalStyles.textarea}
          />
          {state.fieldErrors?.introduction?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={particularId} className={modalStyles.label}>
            こだわり
          </label>
          <textarea
            id={particularId}
            name="particular"
            maxLength={10000}
            defaultValue={shop.particular ?? ""}
            className={modalStyles.textarea}
          />
          {state.fieldErrors?.particular?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.actions}>
          <button
            type="button"
            className={modalStyles.cancelButton}
            onClick={onClose}
          >
            キャンセル
          </button>
          <button
            type="submit"
            disabled={isPending}
            className={modalStyles.submitButton}
          >
            {isPending ? "更新中..." : "更新"}
          </button>
        </div>
      </form>
    </dialog>
  );
}

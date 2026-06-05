"use client";

import {
  startTransition,
  useActionState,
  useEffect,
  useId,
  useRef,
} from "react";
import type { ShopDetail } from "@/api/shops";
import { PREFECTURE_OPTIONS } from "@/app/(admin)/shops/_lib/prefecture";
import { ImageUploadField } from "@/components/ImageUploadField";
import modalStyles from "@/components/modal.module.css";
import {
  PARTICIPATION_STATUS_LABELS,
  PARTICIPATION_STATUS_OPTIONS,
} from "@/components/participationStatus";
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
  const shopUrlId = useId();
  const prefectureId = useId();
  const participationStatusId = useId();

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
    const formData = new FormData(e.currentTarget);
    const nextStatus = formData.get("participationStatus");
    if (nextStatus === "DROPPED") {
      const confirmed = window.confirm(
        "店舗を「参画落ち」にすると、この店舗の全コーヒー豆が無効化され、元に戻すことができません。\n\n本当に参画落ちにしますか？",
      );
      if (!confirmed) return;
    }
    startTransition(() => formAction(formData));
  };

  return (
    <dialog
      ref={dialogRef}
      className={modalStyles.dialog}
      onClose={onClose}
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

      <form onSubmit={handleSubmit} className={modalStyles.form}>
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
            defaultValue={state.values?.shopifyShopId ?? shop.shopifyShopId}
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
            defaultValue={state.values?.name ?? shop.name}
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
            defaultValue={state.values?.introduction ?? shop.introduction ?? ""}
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
            defaultValue={state.values?.particular ?? shop.particular ?? ""}
            className={modalStyles.textarea}
          />
          {state.fieldErrors?.particular?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={shopUrlId} className={modalStyles.label}>
            店舗URL
            <span className={modalStyles.required}>*</span>
          </label>
          <input
            id={shopUrlId}
            name="shopUrl"
            type="url"
            maxLength={2048}
            placeholder="https://example.com"
            defaultValue={state.values?.shopUrl ?? shop.shopUrl}
            className={modalStyles.input}
          />
          {state.fieldErrors?.shopUrl?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={prefectureId} className={modalStyles.label}>
            都道府県
            <span className={modalStyles.required}>*</span>
          </label>
          <select
            id={prefectureId}
            name="prefecture"
            defaultValue={state.values?.prefecture ?? shop.prefecture}
            className={modalStyles.input}
          >
            {PREFECTURE_OPTIONS.map((p) => (
              <option key={p.value} value={p.value}>
                {p.label}
              </option>
            ))}
          </select>
          {state.fieldErrors?.prefecture?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={participationStatusId} className={modalStyles.label}>
            参画ステータス
            <span className={modalStyles.required}>*</span>
          </label>
          {shop.participationStatus === "DROPPED" ? (
            <>
              <input type="hidden" name="participationStatus" value="DROPPED" />
              <input
                id={participationStatusId}
                type="text"
                value={PARTICIPATION_STATUS_LABELS.DROPPED}
                className={modalStyles.input}
                disabled
                readOnly
              />
            </>
          ) : (
            <select
              id={participationStatusId}
              name="participationStatus"
              defaultValue={
                state.values?.participationStatus ?? shop.participationStatus
              }
              className={modalStyles.input}
            >
              {(shop.participationStatus === "PARTICIPATING"
                ? [
                    ...PARTICIPATION_STATUS_OPTIONS,
                    {
                      value: "DROPPED" as const,
                      label: PARTICIPATION_STATUS_LABELS.DROPPED,
                    },
                  ]
                : PARTICIPATION_STATUS_OPTIONS
              ).map((opt) => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </select>
          )}
          {state.fieldErrors?.participationStatus?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <ImageUploadField
          imageTypes={[{ value: "MAIN", label: "メイン" }]}
          existingImages={shop.images.filter((img) => img.type === "MAIN")}
        />
        <ImageUploadField
          imageTypes={[{ value: "LOGO", label: "ロゴ" }]}
          existingImages={shop.images.filter((img) => img.type === "LOGO")}
          required
        />

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

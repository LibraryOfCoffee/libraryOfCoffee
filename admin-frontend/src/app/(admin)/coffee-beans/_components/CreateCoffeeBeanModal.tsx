"use client";

import {
  startTransition,
  useActionState,
  useEffect,
  useId,
  useRef,
} from "react";
import type { ShopListItem } from "@/api/shops";
import type {
  ProcessingMethod,
  RoastLevel,
} from "@/app/(admin)/coffee-beans/_lib/coffeeBeanLabels";
import {
  PROCESSING_METHOD_LABELS,
  ROAST_LEVEL_LABELS,
} from "@/app/(admin)/coffee-beans/_lib/coffeeBeanLabels";
import { ImageUploadField } from "@/components/ImageUploadField";
import modalStyles from "@/components/modal.module.css";
import { ShopSearchSelect } from "@/components/ShopSearchSelect";
import {
  type CreateCoffeeBeanState,
  createCoffeeBeanAction,
} from "./createCoffeeBeanAction";

const initialState: CreateCoffeeBeanState = {};

export function CreateCoffeeBeanModal({
  shops,
  open,
  onClose,
}: {
  shops: ShopListItem[];
  open: boolean;
  onClose: () => void;
}) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [state, formAction, isPending] = useActionState(
    createCoffeeBeanAction,
    initialState,
  );

  const shopifyBeanIdId = useId();
  const nameId = useId();
  const descriptionId = useId();
  const originId = useId();
  const farmId = useId();
  const roastLevelId = useId();
  const processingMethodId = useId();
  const isSpecialtyId = useId();

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

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    startTransition(() => formAction(new FormData(e.currentTarget)));
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
        <h2 className={modalStyles.dialogTitle}>新規コーヒー豆登録</h2>
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
        {state.error && <div className={modalStyles.error}>{state.error}</div>}

        <ShopSearchSelect
          initialShops={shops.map((s) => ({ id: s.id, name: s.name }))}
          defaultValue={
            (state.values as Record<string, string> | undefined)?.shopId
          }
          fieldErrors={state.fieldErrors?.shopId}
        />

        <div className={modalStyles.field}>
          <label htmlFor={shopifyBeanIdId} className={modalStyles.label}>
            Shopify Bean ID
            <span className={modalStyles.required}>*</span>
          </label>
          <input
            id={shopifyBeanIdId}
            name="shopifyBeanId"
            type="text"
            maxLength={255}
            defaultValue={state.values?.shopifyBeanId ?? ""}
            className={modalStyles.input}
          />
          {state.fieldErrors?.shopifyBeanId?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={nameId} className={modalStyles.label}>
            名前
            <span className={modalStyles.required}>*</span>
          </label>
          <input
            id={nameId}
            name="name"
            type="text"
            maxLength={255}
            defaultValue={state.values?.name ?? ""}
            className={modalStyles.input}
          />
          {state.fieldErrors?.name?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={descriptionId} className={modalStyles.label}>
            説明
            <span className={modalStyles.required}>*</span>
          </label>
          <textarea
            id={descriptionId}
            name="description"
            maxLength={10000}
            defaultValue={state.values?.description ?? ""}
            className={modalStyles.textarea}
          />
          {state.fieldErrors?.description?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={originId} className={modalStyles.label}>
            産地
            <span className={modalStyles.required}>*</span>
          </label>
          <input
            id={originId}
            name="origin"
            type="text"
            maxLength={255}
            defaultValue={state.values?.origin ?? ""}
            className={modalStyles.input}
          />
          {state.fieldErrors?.origin?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={farmId} className={modalStyles.label}>
            農園
          </label>
          <input
            id={farmId}
            name="farm"
            type="text"
            maxLength={255}
            defaultValue={state.values?.farm ?? ""}
            className={modalStyles.input}
          />
          {state.fieldErrors?.farm?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={roastLevelId} className={modalStyles.label}>
            焙煎度
            <span className={modalStyles.required}>*</span>
          </label>
          <select
            id={roastLevelId}
            name="roastLevel"
            className={modalStyles.select}
            defaultValue={state.values?.roastLevel ?? ""}
          >
            <option value="" disabled>
              焙煎度を選択してください
            </option>
            {(Object.entries(ROAST_LEVEL_LABELS) as [RoastLevel, string][]).map(
              ([value, label]) => (
                <option key={value} value={value}>
                  {label}
                </option>
              ),
            )}
          </select>
          {state.fieldErrors?.roastLevel?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={processingMethodId} className={modalStyles.label}>
            精製方法
            <span className={modalStyles.required}>*</span>
          </label>
          <select
            id={processingMethodId}
            name="processingMethod"
            className={modalStyles.select}
            defaultValue={state.values?.processingMethod ?? ""}
          >
            <option value="" disabled>
              精製方法を選択してください
            </option>
            {(
              Object.entries(PROCESSING_METHOD_LABELS) as [
                ProcessingMethod,
                string,
              ][]
            ).map(([value, label]) => (
              <option key={value} value={value}>
                {label}
              </option>
            ))}
          </select>
          {state.fieldErrors?.processingMethod?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={isSpecialtyId} className={modalStyles.label}>
            スペシャルティ
            <span className={modalStyles.required}>*</span>
          </label>
          <select
            id={isSpecialtyId}
            name="isSpecialty"
            defaultValue={state.values?.isSpecialty ?? "false"}
            className={modalStyles.select}
          >
            <option value="true">あり</option>
            <option value="false">なし</option>
          </select>
          {state.fieldErrors?.isSpecialty?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <ImageUploadField imageTypes={[{ value: "MAIN", label: "メイン" }]} />

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
            {isPending ? "登録中..." : "登録"}
          </button>
        </div>
      </form>
    </dialog>
  );
}

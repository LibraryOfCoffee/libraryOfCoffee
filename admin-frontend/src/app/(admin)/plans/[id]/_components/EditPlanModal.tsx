"use client";

import { useActionState, useEffect, useId, useRef } from "react";
import type { PlanDetail } from "@/api/plans";
import modalStyles from "@/components/modal.module.css";
import {
  BEAN_QUANTITY_OPTIONS,
  GRAM_WEIGHT_OPTIONS,
  PLAN_TYPE_OPTIONS,
} from "../../_lib/planLabels";
import { editPlanAction } from "./editPlanAction";

export function EditPlanModal({
  plan,
  isOpen,
  onClose,
}: {
  plan: PlanDetail;
  isOpen: boolean;
  onClose: () => void;
}) {
  const [state, formAction, isPending] = useActionState(editPlanAction, {});
  const dialogRef = useRef<HTMLDialogElement>(null);

  const shopifyPlanIdId = useId();
  const labelId = useId();
  const typeId = useId();
  const gramWeightId = useId();
  const beanQuantityId = useId();
  const priceId = useId();
  const isRecommendedId = useId();

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;
    if (isOpen && !dialog.open) {
      dialog.showModal();
    } else if (!isOpen && dialog.open) {
      dialog.close();
    }
  }, [isOpen]);

  useEffect(() => {
    if (state.success) {
      onClose();
    }
  }, [state.success, onClose]);

  return (
    <dialog ref={dialogRef} className={modalStyles.dialog} onClose={onClose}>
      <div className={modalStyles.dialogHeader}>
        <h2 className={modalStyles.dialogTitle}>プランを編集</h2>
        <button
          type="button"
          onClick={onClose}
          className={modalStyles.closeButton}
          aria-label="閉じる"
        >
          &times;
        </button>
      </div>
      <form action={formAction} className={modalStyles.form}>
        <input type="hidden" name="id" value={plan.id} />
        {state.error && <div className={modalStyles.error}>{state.error}</div>}

        <div className={modalStyles.field}>
          <label htmlFor={shopifyPlanIdId} className={modalStyles.label}>
            ShopifyプランID<span className={modalStyles.required}>*</span>
          </label>
          <input
            id={shopifyPlanIdId}
            name="shopifyPlanId"
            type="text"
            maxLength={255}
            defaultValue={plan.shopifyPlanId}
            className={modalStyles.input}
          />
          {state.fieldErrors?.shopifyPlanId?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={labelId} className={modalStyles.label}>
            プラン表示名<span className={modalStyles.required}>*</span>
          </label>
          <input
            id={labelId}
            name="label"
            type="text"
            maxLength={50}
            defaultValue={plan.label}
            className={modalStyles.input}
          />
          {state.fieldErrors?.label?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={typeId} className={modalStyles.label}>
            プラン種別<span className={modalStyles.required}>*</span>
          </label>
          <select
            id={typeId}
            name="type"
            defaultValue={plan.type}
            className={modalStyles.select}
          >
            {PLAN_TYPE_OPTIONS.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
          {state.fieldErrors?.type?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={gramWeightId} className={modalStyles.label}>
            1種あたりのグラム数<span className={modalStyles.required}>*</span>
          </label>
          <select
            id={gramWeightId}
            name="gramWeight"
            defaultValue={plan.gramWeight}
            className={modalStyles.select}
          >
            {GRAM_WEIGHT_OPTIONS.map((gram) => (
              <option key={gram} value={gram}>
                {gram}g
              </option>
            ))}
          </select>
          {state.fieldErrors?.gramWeight?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={beanQuantityId} className={modalStyles.label}>
            豆の種類数<span className={modalStyles.required}>*</span>
          </label>
          <select
            id={beanQuantityId}
            name="beanQuantity"
            defaultValue={plan.beanQuantity}
            className={modalStyles.select}
          >
            {BEAN_QUANTITY_OPTIONS.map((quantity) => (
              <option key={quantity} value={quantity}>
                {quantity}種
              </option>
            ))}
          </select>
          {state.fieldErrors?.beanQuantity?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.field}>
          <label htmlFor={priceId} className={modalStyles.label}>
            価格<span className={modalStyles.required}>*</span>
          </label>
          <input
            id={priceId}
            name="price"
            type="number"
            min="0"
            defaultValue={plan.price}
            className={modalStyles.input}
          />
          {state.fieldErrors?.price?.map((msg) => (
            <span key={msg} className={modalStyles.fieldError}>
              {msg}
            </span>
          ))}
        </div>

        <div className={modalStyles.toggleField}>
          <label htmlFor={isRecommendedId} className={modalStyles.label}>
            おすすめバッジ
          </label>
          <label className={modalStyles.toggleSwitch}>
            <input
              id={isRecommendedId}
              name="isRecommended"
              type="checkbox"
              defaultChecked={plan.isRecommended}
            />
            <span className={modalStyles.toggleSlider} />
          </label>
        </div>

        <div className={modalStyles.actions}>
          <button
            type="button"
            onClick={onClose}
            className={modalStyles.cancelButton}
          >
            キャンセル
          </button>
          <button
            type="submit"
            disabled={isPending}
            className={modalStyles.submitButton}
          >
            {isPending ? "保存中..." : "保存"}
          </button>
        </div>
      </form>
    </dialog>
  );
}

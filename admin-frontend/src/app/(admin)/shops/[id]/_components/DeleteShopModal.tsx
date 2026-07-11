"use client";

import { useActionState, useEffect, useRef } from "react";
import styles from "@/components/deleteModal.module.css";
import modalStyles from "@/components/modal.module.css";
import { type DeleteShopState, deleteShopAction } from "./deleteShopAction";

const initialState: DeleteShopState = {};

export function DeleteShopModal({
  shopId,
  shopName,
  open,
  onClose,
}: {
  shopId: string;
  shopName: string;
  open: boolean;
  onClose: () => void;
}) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [state, formAction, isPending] = useActionState(
    deleteShopAction,
    initialState,
  );

  useEffect(() => {
    const dialog = dialogRef.current;
    if (!dialog) return;
    if (open) {
      dialog.showModal();
    } else {
      dialog.close();
    }
  }, [open]);

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
        <h2 className={modalStyles.dialogTitle}>店舗削除</h2>
        <button
          type="button"
          className={modalStyles.closeButton}
          onClick={onClose}
          aria-label="閉じる"
        >
          &times;
        </button>
      </div>

      <div className={styles.body}>
        <p className={styles.message}>
          <strong>{shopName}</strong> を削除しますか？
        </p>
        <p className={styles.warning}>この操作は取り消せません。</p>

        {state.error && <div className={modalStyles.error}>{state.error}</div>}

        <form action={formAction}>
          <input type="hidden" name="id" value={shopId} />
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
              className={styles.deleteButton}
            >
              {isPending ? "削除中..." : "削除"}
            </button>
          </div>
        </form>
      </div>
    </dialog>
  );
}

"use client";

import { useState } from "react";
import type { ShopDetail } from "@/api/shops";
import { DeleteShopModal } from "./DeleteShopModal";
import { EditShopModal } from "./EditShopModal";
import styles from "./ShopActions.module.css";

export function ShopActions({ shop }: { shop: ShopDetail }) {
  const [editOpen, setEditOpen] = useState(false);
  const [editKey, setEditKey] = useState(0);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [deleteKey, setDeleteKey] = useState(0);

  const handleEditOpen = () => {
    setEditKey((prev) => prev + 1);
    setEditOpen(true);
  };

  const handleDeleteOpen = () => {
    setDeleteKey((prev) => prev + 1);
    setDeleteOpen(true);
  };

  return (
    <>
      <div className={styles.actions}>
        <button
          type="button"
          className={styles.editButton}
          onClick={handleEditOpen}
        >
          編集
        </button>
        <button
          type="button"
          className={styles.deleteButton}
          onClick={handleDeleteOpen}
        >
          削除
        </button>
      </div>
      <EditShopModal
        key={editKey}
        shop={shop}
        open={editOpen}
        onClose={() => setEditOpen(false)}
      />
      <DeleteShopModal
        key={deleteKey}
        shopId={shop.id}
        shopName={shop.name}
        open={deleteOpen}
        onClose={() => setDeleteOpen(false)}
      />
    </>
  );
}

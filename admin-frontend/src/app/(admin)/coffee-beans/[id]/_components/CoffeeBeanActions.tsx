"use client";

import { useState } from "react";
import type { CoffeeBeanDetail } from "@/api/coffee-beans";
import styles from "@/components/actions.module.css";
import { DeleteCoffeeBeanModal } from "./DeleteCoffeeBeanModal";
import { EditCoffeeBeanModal } from "./EditCoffeeBeanModal";

export function CoffeeBeanActions({
  coffeeBean,
  initialShops,
}: {
  coffeeBean: CoffeeBeanDetail;
  initialShops: { id: string; name: string }[];
}) {
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
      <EditCoffeeBeanModal
        key={`edit-${editKey}`}
        coffeeBean={coffeeBean}
        initialShops={initialShops}
        open={editOpen}
        onClose={() => setEditOpen(false)}
      />
      <DeleteCoffeeBeanModal
        key={`delete-${deleteKey}`}
        coffeeBeanId={coffeeBean.id}
        coffeeBeanName={coffeeBean.name}
        open={deleteOpen}
        onClose={() => setDeleteOpen(false)}
      />
    </>
  );
}

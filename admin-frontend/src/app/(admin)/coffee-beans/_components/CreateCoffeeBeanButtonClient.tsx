"use client";

import { useState } from "react";
import type { ShopListItem } from "@/api/shops";
import type { TasteListItem } from "@/api/tastes";
import styles from "@/components/list-page.module.css";
import { CreateCoffeeBeanModal } from "./CreateCoffeeBeanModal";

export function CreateCoffeeBeanButtonClient({
  shops,
  tastes,
}: {
  shops: ShopListItem[];
  tastes: TasteListItem[];
}) {
  const [open, setOpen] = useState(false);
  const [modalKey, setModalKey] = useState(0);

  const handleOpen = () => {
    setModalKey((prev) => prev + 1);
    setOpen(true);
  };

  return (
    <>
      <button
        type="button"
        className={styles.createButton}
        onClick={handleOpen}
      >
        + 新規作成
      </button>
      <CreateCoffeeBeanModal
        key={modalKey}
        shops={shops}
        tastes={tastes}
        open={open}
        onClose={() => setOpen(false)}
      />
    </>
  );
}

"use client";

import { useState } from "react";
import styles from "../page.module.css";
import { CreateShopModal } from "./CreateShopModal";

export function CreateShopButton() {
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
      <CreateShopModal
        key={modalKey}
        open={open}
        onClose={() => setOpen(false)}
      />
    </>
  );
}

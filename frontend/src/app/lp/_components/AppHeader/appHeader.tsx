"use client";

import Image from "next/image";
import { useState } from "react";
import { LuChevronLeft } from "react-icons/lu";
import LoadingOverlay from "../LoadingOverlay/loadingOverlay";
import styles from "./appHeader.module.css";

interface AppHeaderProps {
  onBack: () => void;
}

export default function AppHeader({ onBack }: AppHeaderProps) {
  const [loading, setLoading] = useState(false);

  return (
    <>
      {loading && <LoadingOverlay />}
      <header className={styles.header}>
        <button
          type="button"
          className={styles.back}
          onClick={() => {
            setLoading(true);
            onBack();
          }}
          aria-label="戻る"
        >
          <LuChevronLeft size={24} />
        </button>
        <Image
          src="/logo.svg"
          alt="豆図書"
          width={80}
          height={26}
          className={styles.logo}
        />
        <div className={styles.spacer} />
      </header>
    </>
  );
}

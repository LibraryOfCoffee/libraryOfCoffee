"use client";

import Image from "next/image";
import { LuChevronLeft } from "react-icons/lu";
import styles from "./appHeader.module.css";

interface AppHeaderProps {
  onBack: () => void;
}

export default function AppHeader({ onBack }: AppHeaderProps) {
  return (
    <header className={styles.header}>
      <button
        type="button"
        className={styles.back}
        onClick={onBack}
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
  );
}

import type { ReactNode } from "react";
import styles from "./sectionHeading.module.css";

interface SectionHeadingProps {
  num: string;
  label: string;
  title: ReactNode;
  tone?: "light" | "dark";
  align?: "left" | "center";
}

export default function SectionHeading({
  num,
  label,
  title,
  tone = "light",
  align = "left",
}: SectionHeadingProps) {
  const className = [
    tone === "dark" ? styles.dark : "",
    align === "center" ? styles.center : "",
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <div className={className}>
      <p className={styles.index}>
        <span className={styles.num}>{num}</span>
        <span className={styles.rule} aria-hidden="true" />
        {label}
      </p>
      <h2 className={styles.title}>{title}</h2>
    </div>
  );
}

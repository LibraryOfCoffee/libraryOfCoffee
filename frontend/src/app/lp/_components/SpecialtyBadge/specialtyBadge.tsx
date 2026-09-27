import styles from "./specialtyBadge.module.css";

interface SpecialtyBadgeProps {
  size?: "small" | "large";
}

/** 画像の左上に重ねるスペシャリティコーヒーのバッジ（親は position: relative） */
export default function SpecialtyBadge({
  size = "small",
}: SpecialtyBadgeProps) {
  return (
    <span
      className={`${styles.badge} ${size === "large" ? styles.large : ""}`}
      role="img"
      aria-label="スペシャリティコーヒー"
    >
      ♔
    </span>
  );
}

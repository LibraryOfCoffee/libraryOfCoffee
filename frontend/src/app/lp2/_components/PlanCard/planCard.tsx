import styles from "./planCard.module.css";

interface PlanCardProps {
  badge: string;
  name: string;
  price: string;
  description: string;
  selected: boolean;
  onSelect: () => void;
}

export default function PlanCard({
  badge,
  name,
  price,
  description,
  selected,
  onSelect,
}: PlanCardProps) {
  return (
    <button
      type="button"
      className={`${styles.card} ${selected ? styles.selected : ""}`}
      onClick={onSelect}
    >
      <span className={styles.badge}>{badge}</span>
      <div className={styles.top}>
        <div className={styles.info}>
          <span className={styles.name}>{name}</span>
          <div className={styles.price}>
            <span className={styles.yen}>¥</span>
            <span className={styles.amount}>{price}</span>
            <span className={styles.period}>/月</span>
          </div>
          <span className={styles.desc}>{description}</span>
        </div>
        <div
          className={`${styles.radio} ${selected ? styles.radioSelected : ""}`}
        >
          {selected && <div className={styles.radioInner} />}
        </div>
      </div>
    </button>
  );
}

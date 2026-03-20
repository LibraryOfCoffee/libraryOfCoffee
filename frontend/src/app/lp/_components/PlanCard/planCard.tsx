import { LuCheck } from "react-icons/lu";
import { formatPrice, type PlanDetail } from "../../_lib/planData";
import styles from "./planCard.module.css";

interface PlanCardProps {
  plan: PlanDetail;
  selected: boolean;
  onSelect: () => void;
}

export default function PlanCard({ plan, selected, onSelect }: PlanCardProps) {
  return (
    <button
      type="button"
      className={`${styles.card} ${selected ? styles.selected : ""}`}
      onClick={onSelect}
    >
      {plan.badge && <span className={styles.badge}>{plan.badge}</span>}
      <div className={styles.top}>
        <div className={styles.info}>
          <span className={styles.name}>{plan.name}</span>
          <div className={styles.price}>
            <span className={styles.yen}>¥</span>
            <span className={styles.amount}>{formatPrice(plan.price)}</span>
            <span className={styles.period}>/月</span>
          </div>
          <span className={styles.desc}>{plan.description}</span>
        </div>
        <div
          className={`${styles.radio} ${selected ? styles.radioSelected : ""}`}
        >
          {selected && <div className={styles.radioInner} />}
        </div>
      </div>
      <div className={styles.divider} />
      <ul className={styles.features}>
        <li className={styles.feat}>
          <LuCheck size={14} className={styles.featIcon} />
          最大{plan.maxSelection}種まで選べる＋おすすめで計{plan.totalBeans}種
        </li>
      </ul>
    </button>
  );
}

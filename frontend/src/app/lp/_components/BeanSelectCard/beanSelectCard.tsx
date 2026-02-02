import { LuCheck } from "react-icons/lu";
import type { BeanDetail } from "../../_lib/beanData";
import styles from "./beanSelectCard.module.css";

interface BeanSelectCardProps {
  bean: BeanDetail;
  selected: boolean;
  onToggle: () => void;
  onDetail: () => void;
}

export default function BeanSelectCard({
  bean,
  selected,
  onToggle,
  onDetail,
}: BeanSelectCardProps) {
  const inputId = `bean-select-${bean.id}`;

  return (
    <div className={`${styles.card} ${selected ? styles.selected : ""}`}>
      <input
        type="checkbox"
        id={inputId}
        className={styles.input}
        checked={selected}
        onChange={onToggle}
      />
      <label htmlFor={inputId} className={styles.label}>
        <div
          className={styles.image}
          style={{ backgroundImage: `url(${bean.imageSrc})` }}
        />
        <div className={styles.info}>
          <span className={styles.tag} style={{ background: bean.tagColor }}>
            {bean.tag}
          </span>
          <span className={styles.name}>{bean.name}</span>
          <span className={styles.desc}>{bean.description}</span>
          <button
            type="button"
            className={styles.detail}
            onClick={(e) => {
              e.preventDefault();
              onDetail();
            }}
          >
            詳細を見る
          </button>
        </div>
        <div
          className={`${styles.check} ${selected ? styles.checkSelected : ""}`}
          aria-hidden="true"
        >
          {selected && <LuCheck size={16} color="#FFFFFF" />}
        </div>
      </label>
    </div>
  );
}

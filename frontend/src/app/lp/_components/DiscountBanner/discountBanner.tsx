import { LuPercent } from "react-icons/lu";
import styles from "./discountBanner.module.css";

export default function DiscountBanner() {
  return (
    <div className={styles.banner}>
      <div className={styles.icon}>
        <LuPercent size={20} color="#FFFFFF" />
      </div>
      <div className={styles.text}>
        <span className={styles.title}>今だけ初月1,200円!</span>
        <span className={styles.desc}>通常1,500円が初月1,200円になります</span>
      </div>
    </div>
  );
}

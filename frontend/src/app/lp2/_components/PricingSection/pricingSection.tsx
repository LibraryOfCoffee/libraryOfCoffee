import Link from "next/link";
import { LuCheck } from "react-icons/lu";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import lp2Styles from "../../lp2.module.css";
import styles from "./pricingSection.module.css";

const features = [
  "毎月8種類の珈琲豆から3種類選べる",
  "送料無料でお届け",
  "いつでも解約・スキップOK",
];

export default function PricingSection() {
  return (
    <section className={`${styles.pricing} ${lp2Styles.section}`}>
      <h2 className={lp2Styles.sectionTitle} style={{ color: "#FFFFFF" }}>
        シンプルな料金プラン
      </h2>
      <p
        className={lp2Styles.sectionSubtitle}
        style={{ color: "rgba(255,255,255,0.73)" }}
      >
        いつでも解約OK
      </p>
      <div className={styles.card}>
        <span className={styles.badge}>今だけ初月1,200円</span>
        <div className={styles.header}>
          <span className={styles.planName}>月額プラン</span>
          <div className={styles.price}>
            <span className={styles.yen}>¥</span>
            <span className={styles.amount}>1,500</span>
            <span className={styles.period}>/月</span>
          </div>
          <span className={styles.desc}>30g × 3種類 / 毎月届く</span>
        </div>
        <Link href={getPlanPagePath()} className={styles.btn}>
          今すぐ始める
        </Link>
        <ul className={styles.features}>
          {features.map((feat) => (
            <li key={feat} className={styles.feat}>
              <LuCheck size={16} color="#7D9B76" />
              <span>{feat}</span>
            </li>
          ))}
        </ul>
      </div>
    </section>
  );
}

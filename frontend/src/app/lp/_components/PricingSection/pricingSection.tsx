import { LuCheck, LuChevronRight } from "react-icons/lu";
import sharedStyles from "../../shared.module.css";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./pricingSection.module.css";

const features = [
  "最大2種まで選べる＋おすすめで計3種をお届け",
  "送料無料でお届け",
  "いつでも解約OK",
];

export default function PricingSection() {
  return (
    <section
      id="pricing"
      className={`${styles.pricing} ${sharedStyles.section}`}
    >
      <h2 className={sharedStyles.sectionTitle} style={{ color: "#FFFFFF" }}>
        シンプルな料金プラン
      </h2>
      <div className={styles.card}>
        <div className={styles.header}>
          <span className={styles.planName}>月額プラン</span>
          <div className={styles.price}>
            <span className={styles.yen}>¥</span>
            <span className={styles.amount}>1,500</span>
            <span className={styles.period}>/月</span>
          </div>
          <span className={styles.desc}>30g × 3種類 / 毎月届く</span>
        </div>
        <LinkWithLoading
          href="/lp/beans?planId=cbl-3b-30g"
          className={styles.btn}
        >
          このプランで豆を選ぶ
          <LuChevronRight className={styles.btnArrow} size={22} />
        </LinkWithLoading>
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

import sharedStyles from "../../shared.module.css";
import styles from "./pricingSection.module.css";
import PricingWithToggle from "./pricingWithToggle";

export default function PricingSection() {
  return (
    <section
      id="pricing"
      className={`${styles.pricing} ${sharedStyles.section}`}
      style={{ paddingLeft: 0, paddingRight: 0, overflow: "hidden" }}
    >
      <h2
        className={sharedStyles.sectionTitle}
        style={{ color: "#FFFFFF", padding: "0 24px" }}
      >
        料金プラン
      </h2>
      <p className={styles.subtitle}>
        様々なプランをご用意。焙煎したての新鮮な豆を送料無料でお届けします。いつでも解約OK。
      </p>
      <PricingWithToggle />
    </section>
  );
}

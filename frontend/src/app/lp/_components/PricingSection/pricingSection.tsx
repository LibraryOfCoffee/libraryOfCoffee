import { plans } from "../../_lib/planData";
import sharedStyles from "../../shared.module.css";
import PricingCarousel from "./pricingCarousel";
import styles from "./pricingSection.module.css";

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
        シンプルな料金プラン
      </h2>
      <p className={styles.subtitle}>
        3つのプランをご用意。焙煎したての新鮮な豆を送料無料でお届けします。いつでも解約OK。
      </p>
      <PricingCarousel plans={plans} />
    </section>
  );
}

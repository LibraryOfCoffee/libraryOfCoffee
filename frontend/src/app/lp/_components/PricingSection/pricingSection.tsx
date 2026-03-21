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
      <div className={styles.campaignBox}>
        <p className={styles.campaignTitle}>初月割引キャンペーン実施中</p>
        <p className={styles.campaignText}>
          珈琲探求を気軽に試してほしい——そんな想いから、豆図書では期間限定で初月割引をご用意しました。ぜひこの機会に、さまざまな珈琲との出会いをお楽しみください。
        </p>
      </div>
      <PricingWithToggle />
    </section>
  );
}

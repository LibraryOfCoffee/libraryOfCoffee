"use client";

import { useState } from "react";
import {
  groupPlansByGram,
  type PlanGroup,
  type WeightGrams,
} from "../../_lib/planApi";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./pricingSection.module.css";

const GRAM_LABELS: Record<number, string> = {
  30: "お試し",
  60: "標準",
  90: "たっぷり",
};

interface PricingSectionProps {
  planGroups: PlanGroup[];
}

export default function PricingSection({ planGroups }: PricingSectionProps) {
  const [gram, setGram] = useState<WeightGrams>(30);
  const plans = groupPlansByGram(planGroups, gram);

  return (
    <section id="pricing" className={styles.section}>
      <p className={styles.eyebrow}>— PRICING</p>
      <h2 className={styles.headline}>料金プラン</h2>
      <p className={styles.desc}>
        焙煎したての新鮮な豆を、送料無料でお届け。
        <br />
        定期便はいつでも解約OK。単品購入も同じプランから選べます。
      </p>

      <div className={styles.tabs}>
        {([30, 60, 90] as WeightGrams[]).map((g) => (
          <button
            key={g}
            type="button"
            onClick={() => setGram(g)}
            className={`${styles.tab} ${gram === g ? styles.tabActive : styles.tabInactive}`}
          >
            {g}
            <span className={styles.tabNum}>g</span>
          </button>
        ))}
      </div>

      <div className={styles.cards}>
        {plans.map((p) => (
          <LinkWithLoading
            key={p.subscriptionId}
            href={getPlanPagePath()}
            className={styles.card}
          >
            {p.isRecommended && <span className={styles.badge}>おすすめ</span>}
            <div className={styles.cardInner}>
              <div className={styles.cardLeft}>
                <div className={styles.planName}>{p.label}プラン</div>
                <div className={styles.planDesc}>
                  <span className={styles.planDescEn}>{p.gramWeight}g</span> ×{" "}
                  {p.beanQuantity}種 / {GRAM_LABELS[p.gramWeight]}
                </div>
              </div>
              <div className={styles.cardRight}>
                <div className={styles.price}>
                  ¥{p.subscriptionPrice.toLocaleString()}
                  <span className={styles.priceUnit}>/月</span>
                </div>
                <div className={styles.singlePrice}>
                  単品 ¥{p.singlePrice.toLocaleString()}
                </div>
              </div>
            </div>
          </LinkWithLoading>
        ))}
      </div>
    </section>
  );
}

"use client";

import { useState } from "react";
import {
  groupPlansByGram,
  type PlanGroup,
  type WeightGrams,
} from "../../_lib/planApi";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import SectionHeading from "../SectionHeading/sectionHeading";
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
      <SectionHeading num="06" label="Pricing" title="料金プラン" />
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
            aria-pressed={gram === g}
            onClick={() => setGram(g)}
            className={`${styles.tab} ${gram === g ? styles.tabActive : ""}`}
          >
            {g}g
          </button>
        ))}
      </div>

      {plans.map((p) => (
        <LinkWithLoading
          key={p.subscriptionId}
          href={getPlanPagePath(undefined, p.subscriptionId)}
          className={styles.card}
        >
          <div>
            <div className={styles.nameRow}>
              <span className={styles.planName}>{p.label}プラン</span>
              {p.isRecommended && (
                <span className={styles.badge}>Recommended</span>
              )}
            </div>
            <div className={styles.planDesc}>
              {p.gramWeight}g × {p.beanQuantity}種 / {GRAM_LABELS[p.gramWeight]}
            </div>
          </div>
          <div className={styles.cardRight}>
            <div className={styles.price}>
              ¥{p.subscriptionPrice.toLocaleString()}
            </div>
            <div className={styles.priceUnit}>/月 · 定期便</div>
            <div className={styles.singlePrice}>
              単品 ¥{p.singlePrice.toLocaleString()}
            </div>
          </div>
        </LinkWithLoading>
      ))}

      <div className={styles.notes}>
        <div>
          <div>送料無料</div>
          <div>いつでも解約OK</div>
        </div>
        <div className={styles.notesRight}>
          <div>焙煎したてをお届け</div>
          <div>単品購入もOK</div>
        </div>
      </div>
    </section>
  );
}

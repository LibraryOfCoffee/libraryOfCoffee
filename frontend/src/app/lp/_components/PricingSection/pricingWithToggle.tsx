"use client";

import { useState } from "react";
import type { PlanDetail, WeightGrams } from "../../_lib/planApi";
import WeightToggle from "../WeightToggle/weightToggle";
import PricingCarousel from "./pricingCarousel";
import styles from "./pricingSection.module.css";

export default function PricingWithToggle() {
  const [weight, setWeight] = useState<WeightGrams>(30);
  // NOTE: このコンポーネントは現在未使用。プランデータはAPIから取得するため空配列を渡す。
  const filteredPlans: PlanDetail[] = [];

  return (
    <>
      <div className={styles.toggleWrap}>
        <WeightToggle value={weight} onChange={setWeight} />
      </div>
      <PricingCarousel plans={filteredPlans} />
    </>
  );
}

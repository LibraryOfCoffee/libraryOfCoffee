"use client";

import { useState } from "react";
import { getPlansForWeight, type WeightGrams } from "../../_lib/planData";
import WeightToggle from "../WeightToggle/weightToggle";
import PricingCarousel from "./pricingCarousel";
import styles from "./pricingSection.module.css";

export default function PricingWithToggle() {
  const [weight, setWeight] = useState<WeightGrams>(30);
  const filteredPlans = getPlansForWeight(weight);

  return (
    <>
      <div className={styles.toggleWrap}>
        <WeightToggle value={weight} onChange={setWeight} />
      </div>
      <PricingCarousel key={weight} plans={filteredPlans} />
    </>
  );
}

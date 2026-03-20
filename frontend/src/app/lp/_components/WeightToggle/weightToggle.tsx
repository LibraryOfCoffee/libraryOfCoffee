"use client";

import { WEIGHT_OPTIONS, type WeightGrams } from "../../_lib/planData";
import styles from "./weightToggle.module.css";

interface WeightToggleProps {
  value: WeightGrams;
  onChange: (weight: WeightGrams) => void;
}

export default function WeightToggle({ value, onChange }: WeightToggleProps) {
  return (
    <div className={styles.toggle}>
      {WEIGHT_OPTIONS.map((w) => (
        <button
          key={w}
          type="button"
          className={`${styles.option} ${w === value ? styles.active : ""}`}
          onClick={() => onChange(w)}
        >
          {w}g
        </button>
      ))}
    </div>
  );
}

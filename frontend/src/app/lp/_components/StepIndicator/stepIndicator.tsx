import styles from "./stepIndicator.module.css";

interface StepIndicatorProps {
  currentStep: 1 | 2 | 3;
}

const steps = [
  { num: 1, label: "プラン" },
  { num: 2, label: "豆を選ぶ" },
  { num: 3, label: "登録" },
];

export default function StepIndicator({ currentStep }: StepIndicatorProps) {
  return (
    <div className={styles.indicator}>
      {steps.map((step, i) => {
        const isActive = step.num <= currentStep;
        return (
          <div key={step.num} className={styles.group}>
            {i > 0 && (
              <div
                className={`${styles.line} ${step.num <= currentStep ? styles.lineActive : ""}`}
              />
            )}
            <div className={styles.item}>
              <div
                className={`${styles.dot} ${isActive ? styles.dotActive : ""}`}
              >
                {step.num}
              </div>
              <span
                className={`${styles.label} ${isActive ? styles.labelActive : ""}`}
              >
                {step.label}
              </span>
            </div>
          </div>
        );
      })}
    </div>
  );
}

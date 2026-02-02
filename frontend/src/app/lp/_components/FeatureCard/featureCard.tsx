import type { ReactNode } from "react";
import styles from "./featureCard.module.css";

interface FeatureCardProps {
  icon: ReactNode;
  iconColor: string;
  title: string;
  description: string;
}

export default function FeatureCard({
  icon,
  iconColor,
  title,
  description,
}: FeatureCardProps) {
  return (
    <div className={styles.card}>
      <div className={styles.icon} style={{ background: iconColor }}>
        {icon}
      </div>
      <div className={styles.content}>
        <h3 className={styles.title}>{title}</h3>
        <p className={styles.desc}>{description}</p>
      </div>
    </div>
  );
}

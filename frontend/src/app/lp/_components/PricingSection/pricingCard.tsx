import { LuCheck, LuChevronRight } from "react-icons/lu";
import { formatPrice, type PlanDetail } from "../../_lib/planData";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./pricingSection.module.css";

interface PricingCardProps {
  plan: PlanDetail;
}

export default function PricingCard({ plan }: PricingCardProps) {
  return (
    <div className={styles.card}>
      {plan.badge && <span className={styles.badge}>{plan.badge}</span>}
      <div className={styles.header}>
        <span className={styles.planName}>{plan.name}</span>
        {plan.catchphrase && (
          <span className={styles.catchphrase}>{plan.catchphrase}</span>
        )}
        <div className={styles.price}>
          <span className={styles.yen}>¥</span>
          <span className={styles.amount}>{formatPrice(plan.price)}</span>
          <span className={styles.period}>/月</span>
        </div>
        <span className={styles.desc}>{plan.description}</span>
      </div>
      <div className={styles.divider} />
      <ul className={styles.features}>
        <li className={styles.feat}>
          <LuCheck size={16} className={styles.featIcon} />
          <span>
            最大{plan.maxSelection}種まで選べる＋おすすめで計{plan.totalBeans}
            種をお届け
          </span>
        </li>
      </ul>
      <LinkWithLoading
        href={`/lp/beans?planId=${plan.id}`}
        className={styles.btn}
      >
        このプランで豆を選ぶ
        <LuChevronRight className={styles.btnArrow} size={22} />
      </LinkWithLoading>
    </div>
  );
}

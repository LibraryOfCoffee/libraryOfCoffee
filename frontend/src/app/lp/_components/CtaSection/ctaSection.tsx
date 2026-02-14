import Link from "next/link";
import { LuArrowRight } from "react-icons/lu";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import sharedStyles from "../../shared.module.css";
import styles from "./ctaSection.module.css";

export default function CtaSection() {
  return (
    <section className={styles.cta}>
      <h2 className={styles.title}>
        新しいコーヒー体験を
        <br />
        始めませんか？
      </h2>
      <p className={styles.subtitle}>いつでも解約OK</p>
      <Link href={getPlanPagePath()} className={sharedStyles.btnPrimary}>
        豆を選ぶ
        <LuArrowRight size={18} />
      </Link>
    </section>
  );
}

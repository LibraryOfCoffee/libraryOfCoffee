import Link from "next/link";
import { LuArrowRight } from "react-icons/lu";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import lp2Styles from "../../lp2.module.css";
import styles from "./ctaSection.module.css";

export default function CtaSection() {
  return (
    <section className={styles.cta}>
      <h2 className={styles.title}>
        新しいコーヒー体験を
        <br />
        始めませんか？
      </h2>
      <p className={styles.subtitle}>今だけ初月1,200円・いつでも解約OK</p>
      <Link href={getPlanPagePath()} className={lp2Styles.btnPrimary}>
        今すぐ始める
        <LuArrowRight size={18} />
      </Link>
    </section>
  );
}

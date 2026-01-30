import Link from "next/link";
import { LuArrowRight, LuCoffee } from "react-icons/lu";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import lp2Styles from "../../lp2.module.css";
import styles from "./heroSection.module.css";

export default function HeroSection() {
  return (
    <section className={styles.hero}>
      <div className={styles.overlay}>
        <div className={styles.badge}>
          <LuCoffee size={16} />
          <span>今だけ初月1,200円</span>
        </div>
        <h1 className={styles.title}>
          あなただけの
          <br />
          コーヒー体験を
        </h1>
        <p className={styles.sub}>
          毎月届く、厳選された3種の豆で
          <br />
          新しい味わいとの出会いを
        </p>
        <Link href={getPlanPagePath()} className={lp2Styles.btnPrimary}>
          今すぐ始める
          <LuArrowRight size={18} />
        </Link>
      </div>
    </section>
  );
}

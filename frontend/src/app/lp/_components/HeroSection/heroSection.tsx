import Link from "next/link";
import { LuArrowRight } from "react-icons/lu";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import sharedStyles from "../../shared.module.css";
import styles from "./heroSection.module.css";

export default function HeroSection() {
  return (
    <section className={styles.hero}>
      <div className={styles.overlay}>
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
        <Link href={getPlanPagePath()} className={sharedStyles.btnPrimary}>
          今すぐ始める
          <LuArrowRight size={18} />
        </Link>
      </div>
    </section>
  );
}

import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./ctaSection.module.css";

export default function CtaSection() {
  return (
    <section className={styles.cta}>
      <p className={styles.eyebrow}>Begin Your Coffee Journey</p>
      <h2 className={styles.title}>
        豆を溜めず、
        <br />
        経験を貯める。
      </h2>
      <p className={styles.subtitle}>
        30g×3種の少量多品種でお届け。
        <br />
        ハンドドリップで楽しむ試し飲み体験を、定額で。
      </p>
      <div className={styles.actions}>
        <LinkWithLoading href={getPlanPagePath()} className={styles.ctaBtn}>
          豆を選ぶ
        </LinkWithLoading>
        <a href="#beans" className={styles.subCta}>
          ラインナップを見る
        </a>
      </div>
    </section>
  );
}

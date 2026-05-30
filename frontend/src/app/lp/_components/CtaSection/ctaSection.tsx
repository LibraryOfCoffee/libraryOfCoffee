import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
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
      <LinkWithLoading href={getPlanPagePath()} className={styles.ctaBtn}>
        豆を選ぶ
        <span className={styles.ctaArrow}>→</span>
      </LinkWithLoading>
    </section>
  );
}

import { LuChevronDown, LuChevronRight } from "react-icons/lu";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import sharedStyles from "../../shared.module.css";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./heroSection.module.css";

export default function HeroSection() {
  return (
    <section className={styles.hero}>
      <div className={styles.overlay}>
        <div className={styles.mainContent}>
          <h1 className={styles.title}>
            豆を溜めず
            <br />
            経験を貯める
          </h1>
          <LinkWithLoading
            href={getPlanPagePath()}
            className={`${sharedStyles.btnPrimary} ${styles.ctaBtn}`}
          >
            豆を選ぶ
            <LuChevronRight className={styles.ctaArrow} size={22} />
          </LinkWithLoading>
        </div>
        <a href="#beans" className={styles.anchorLink}>
          初回のラインナップ
          <LuChevronDown size={16} />
        </a>
      </div>
    </section>
  );
}

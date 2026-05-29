import Image from "next/image";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./heroSection.module.css";

export default function HeroSection() {
  return (
    <div className={styles.heroWrap}>
      <div className={styles.heroImg}>
        <Image
          src="/hero-image.jpeg"
          alt=""
          fill
          sizes="(max-width: 480px) 100vw, 480px"
          priority
          className={styles.heroImgInner}
        />
        <div className={styles.heroImgOverlay} />
      </div>

      <h1 className={styles.title}>
        豆を溜めず
        <br />
        経験を貯める
      </h1>
      <p className={styles.subtitle}>珈琲豆のサブスク、はじまる。</p>

      <div className={styles.ctaArea}>
        <LinkWithLoading href={getPlanPagePath()} className={styles.ctaBtn}>
          豆を選ぶ
          <span className={styles.ctaArrow}>→</span>
        </LinkWithLoading>
        <p className={styles.priceNote}>
          <span className={styles.priceNoteEn}>
            定期便 ¥1,500〜/月 · 単品購入 ¥1,650〜
          </span>
        </p>
        <a href="#beans" className={styles.subCta}>
          ラインナップを見る ↓
        </a>
      </div>
    </div>
  );
}

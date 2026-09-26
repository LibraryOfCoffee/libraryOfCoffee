import Image from "next/image";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import Header from "../Header/header";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./heroSection.module.css";

export default function HeroSection() {
  return (
    <div className={styles.hero}>
      <Header />
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

      <div className={styles.body}>
        <p className={styles.eyebrow}>Coffee Bean Subscription</p>
        <h1 className={styles.title}>
          豆を溜めず
          <br />
          経験を貯める
        </h1>
        <p className={styles.subtitle}>珈琲豆のサブスク、はじまる。</p>

        <div className={styles.ctaArea}>
          <LinkWithLoading href={getPlanPagePath()} className={styles.ctaBtn}>
            豆を選ぶ
          </LinkWithLoading>
          <a href="#bean-list" className={styles.subCta}>
            ラインナップを見る
          </a>
        </div>
        <p className={styles.priceNote}>
          定期便 ¥1,580〜/月 · 単品購入 ¥1,700〜
        </p>
      </div>
    </div>
  );
}

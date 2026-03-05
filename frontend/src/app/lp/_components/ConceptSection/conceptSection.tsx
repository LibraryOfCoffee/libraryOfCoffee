import styles from "./conceptSection.module.css";

export default function ConceptSection() {
  return (
    <section className={styles.section}>
      <h2 className={styles.headline}>
        「運命の1杯」に出会う、
        <br />
        珈琲の図書館へ
      </h2>

      <ul className={styles.checkList}>
        <li className={styles.checkItem}>
          <span className={styles.checkIcon} aria-hidden="true">
            ✓
          </span>
          <span>種類が多すぎて、何を選べばいいかわからない</span>
        </li>
        <li className={styles.checkItem}>
          <span className={styles.checkIcon} aria-hidden="true">
            ✓
          </span>
          <span>お店のあの味を、自宅でも再現してみたい</span>
        </li>
        <li className={styles.checkItem}>
          <span className={styles.checkIcon} aria-hidden="true">
            ✓
          </span>
          <span>気になる珈琲豆が自分好みの味か試し飲みしたい</span>
        </li>
      </ul>

      <p className={styles.bridge}>そんなあなたのための「豆図書」</p>

      <div className={styles.features}>
        <div className={styles.featureItem}>
          <p className={styles.featureLabel}>未知の体験を</p>
          <p className={styles.featureDesc}>
            毎月3種類以上の厳選された珈琲豆をお届け
          </p>
        </div>
        <div className={styles.featureItem}>
          <p className={styles.featureLabel}>贅沢な飲み比べ</p>
          <p className={styles.featureDesc}>
            お試しサイズで、様々なロースタリーの味を飲み比べ
          </p>
        </div>
        <div className={styles.featureItem}>
          <p className={styles.featureLabel}>プロの技術を自宅で</p>
          <p className={styles.featureDesc}>
            各店舗推奨の「淹れ方レシピ」で、驚きの1杯を再現
          </p>
        </div>
      </div>

      <p className={styles.closing}>
        毎日の珈琲時間が、
        <br />
        もっと自由で、もっと楽しみになる
      </p>
    </section>
  );
}

import styles from "./experienceSection.module.css";

const features = [
  {
    num: "01",
    label: "未知の体験を",
    desc: "毎月3種類以上の厳選された珈琲豆をお届け",
  },
  {
    num: "02",
    label: "贅沢な飲み比べ",
    desc: "お試しサイズで、様々なロースタリーの味を飲み比べ",
  },
  {
    num: "03",
    label: "プロの技術を自宅で",
    desc: "各店舗推奨の「淹れ方レシピ」で、驚きの1杯を再現",
  },
];

export default function ExperienceSection() {
  return (
    <section className={styles.section}>
      <p className={styles.eyebrow}>— EXPERIENCE</p>
      <div className={styles.list}>
        {features.map((f) => (
          <div key={f.num} className={styles.item}>
            <span className={styles.num}>{f.num}</span>
            <div>
              <h3 className={styles.label}>{f.label}</h3>
              <p className={styles.desc}>{f.desc}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}

import Image from "next/image";
import SectionHeading from "../SectionHeading/sectionHeading";
import styles from "./experienceSection.module.css";

const features = [
  {
    num: "01",
    label: "未知の体験を",
    desc: "毎月3種類以上の厳選された珈琲豆をお届け",
    image: "/experience/discovery.jpg",
  },
  {
    num: "02",
    label: "贅沢な飲み比べ",
    desc: "お試しサイズで、様々なロースタリーの味を飲み比べ",
    image: "/experience/tasting-flight-cups.jpg",
  },
  {
    num: "03",
    label: "プロの技術を自宅で",
    desc: "各店舗推奨の「淹れ方レシピ」で、驚きの1杯を再現",
    image: "/experience/store-recipe-pour.jpg",
  },
];

export default function ExperienceSection() {
  return (
    <section className={styles.section}>
      <div className={styles.head}>
        <SectionHeading
          num="03"
          label="Experience"
          title={
            <>
              豆図書ならではの
              <br />
              3つの体験
            </>
          }
        />
      </div>
      <div className={styles.rule} />
      {features.map((f, i) => (
        <div key={f.num}>
          <div
            className={`${styles.row} ${i % 2 === 1 ? styles.rowReverse : ""}`}
          >
            <div className={styles.media}>
              <Image
                src={f.image}
                alt=""
                fill
                sizes="(max-width: 480px) 44vw, 212px"
                className={styles.mediaImg}
              />
            </div>
            <div className={styles.text}>
              <span className={styles.num} aria-hidden="true">
                {f.num}
              </span>
              <h3 className={styles.label}>{f.label}</h3>
              <p className={styles.desc}>{f.desc}</p>
            </div>
          </div>
          <div className={styles.rule} />
        </div>
      ))}
    </section>
  );
}

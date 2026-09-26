import SectionHeading from "../SectionHeading/sectionHeading";
import styles from "./howItWorks.module.css";

const steps = [
  {
    num: "01",
    title: "選ぶ",
    desc: "お好きな豆を選択",
  },
  {
    num: "02",
    title: "届く",
    desc: "選んだ以外は豆図書におまかせで、焙煎したてでお届け",
  },
  {
    num: "03",
    title: "淹れる",
    desc: "店舗おすすめの淹れ方を参考に楽しむ",
  },
];

export default function HowItWorks() {
  return (
    <section id="how-it-works" className={styles.section}>
      <SectionHeading
        num="05"
        label="How It Works"
        title="ご利用の流れ"
        tone="dark"
      />
      <ol className={styles.steps}>
        {steps.map((step) => (
          <li key={step.num} className={styles.step}>
            <span className={styles.marker} aria-hidden="true" />
            <p className={styles.num}>{step.num}</p>
            <h3 className={styles.stepTitle}>{step.title}</h3>
            <p className={styles.stepDesc}>{step.desc}</p>
          </li>
        ))}
      </ol>
    </section>
  );
}

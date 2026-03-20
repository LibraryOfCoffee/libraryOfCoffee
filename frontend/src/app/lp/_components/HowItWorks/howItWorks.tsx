import sharedStyles from "../../shared.module.css";
import styles from "./howItWorks.module.css";

const steps = [
  {
    num: 1,
    title: "選ぶ",
    desc: "お好きな豆を選択",
  },
  {
    num: 2,
    title: "届く",
    desc: "選んだ豆 ＋ おすすめの豆を受け取り",
  },
  {
    num: 3,
    title: "淹れる",
    desc: "店舗おすすめの淹れ方を参考に楽しむ",
  },
];

export default function HowItWorks() {
  return (
    <section
      id="how-it-works"
      className={`${styles.how} ${sharedStyles.section}`}
    >
      <h2 className={sharedStyles.sectionTitle}>ご利用の流れ</h2>
      <div className={styles.steps}>
        {steps.map((step) => (
          <div key={step.num} className={styles.step}>
            <div className={styles.num}>{step.num}</div>
            <div className={styles.content}>
              <h3 className={styles.stepTitle}>{step.title}</h3>
              <p className={styles.stepDesc}>{step.desc}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}

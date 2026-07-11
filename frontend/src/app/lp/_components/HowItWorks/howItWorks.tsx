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
    desc: "選んだ以外は豆図書におまかせで、焙煎したてでお届け",
  },
  {
    num: 3,
    title: "淹れる",
    desc: "店舗おすすめの淹れ方を参考に楽しむ",
  },
];

export default function HowItWorks() {
  return (
    <section id="how-it-works" className={styles.section}>
      <p className={styles.eyebrow}>— HOW IT WORKS</p>
      <h2 className={styles.headline}>ご利用の流れ</h2>
      <div className={styles.steps}>
        {steps.map((step) => (
          <div key={step.num} className={styles.step}>
            <div className={styles.num}>{step.num}</div>
            <div>
              <h3 className={styles.stepTitle}>{step.title}</h3>
              <p className={styles.stepDesc}>{step.desc}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}

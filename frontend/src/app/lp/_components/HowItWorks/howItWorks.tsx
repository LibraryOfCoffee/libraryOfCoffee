import sharedStyles from "../../shared.module.css";
import styles from "./howItWorks.module.css";

const steps = [
  {
    num: 1,
    title: "気になる珈琲豆を選択",
    desc: "✓ 珈琲豆・店舗情報を確認\n✓ 気になる4種を登録 or 豆図書にお任せ\n\n※初回以降は毎月登録となります。その月の登録期間を過ぎると、自動で「月のおすすめ」が４種登録されます。",
  },
  {
    num: 2,
    title: "お届け先登録",
    desc: "✓ 目安1分で登録完了",
  },
  {
    num: 3,
    title: "珈琲豆が届く",
    desc: "✓ 登録した豆から２種＋「月のおすすめ」から１種が届く\n\n※必ず異なる３種類の豆が届きます。",
  },
  {
    num: 4,
    title: "様々な珈琲体験",
    desc: "✓ 新鮮な珈琲を味わう\n✓ 珈琲豆の特徴を知る\n✓ 店舗の味やこだわりを体験",
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

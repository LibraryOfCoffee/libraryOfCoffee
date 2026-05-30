import styles from "./conceptSection.module.css";

export default function ConceptSection() {
  return (
    <section className={styles.section}>
      <p className={styles.eyebrow}>— WHY 豆図書</p>
      <h2 className={styles.headline}>
        「運命の1杯」に出会う、
        <br />
        珈琲の図書館へ
      </h2>

      <ul className={styles.checkList}>
        {[
          "種類が多すぎて、何を選べばいいかわからない",
          "お店のあの味を、自宅でも再現してみたい",
          "気になる珈琲豆が自分好みの味か試し飲みしたい",
        ].map((text) => (
          <li key={text} className={styles.checkItem}>
            <span className={styles.checkIcon} aria-hidden="true">
              ✓
            </span>
            <span>{text}</span>
          </li>
        ))}
      </ul>

      <p className={styles.bridge}>
        そんなあなたのための
        <br />
        <span className={styles.bridgeHighlight}>「豆図書」</span>
      </p>
    </section>
  );
}

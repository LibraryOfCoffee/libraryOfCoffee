import SectionHeading from "../SectionHeading/sectionHeading";
import styles from "./problemSection.module.css";

const problems = [
  "種類が多すぎて、\n何を選べばいいかわからない",
  "お店のあの味を、\n自宅でも再現してみたい",
  "気になる珈琲豆が、\n自分好みの味か試し飲みしたい",
];

export default function ProblemSection() {
  return (
    <section className={styles.section}>
      <SectionHeading
        num="02"
        label="The Problem"
        title={
          <>
            こんなこと、
            <br />
            ありませんか。
          </>
        }
      />
      <ul className={styles.list}>
        {problems.map((text, i) => (
          <li key={text} className={styles.item}>
            <span className={styles.num} aria-hidden="true">
              {String(i + 1).padStart(2, "0")}
            </span>
            <h3 className={styles.text}>{text}</h3>
          </li>
        ))}
      </ul>
      <div className={styles.solution}>
        <p className={styles.solutionLabel}>Solution</p>
        <p className={styles.solutionText}>
          そんなあなたのための
          <br />
          「豆図書」
        </p>
      </div>
    </section>
  );
}

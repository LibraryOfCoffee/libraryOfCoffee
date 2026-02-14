import styles from "./conceptSection.module.css";

export default function ConceptSection() {
  return (
    <section className={styles.section}>
      <div className={styles.block}>
        <h2 className={styles.title}>
          珈琲は飲むだけのものですか
          <br />
          出会うものですか
        </h2>
        <p className={styles.body}>
          100gという「量」で豆を買うとき、
          <br />
          飲み切るまでの「作業」を課してはいないでしょうか。
        </p>
        <p className={styles.body}>
          流れ作業のように消費される珈琲。
          <br />
          新しい豆と出会うはずだった機会が失われています。
        </p>
      </div>

      <div className={styles.block}>
        <h2 className={styles.title}>珈琲を『消費』から『体験』へ</h2>
        <p className={styles.body}>
          豆図書が届けるのは30g×3種類。
          <br />
          毎月様々な焙煎所の珈琲豆を少しずつ楽しめます。
        </p>
        <p className={styles.body}>
          豆を溜めずに、経験を貯める。
          <br />
          そんな新しいコーヒーライフ、始めてみませんか。
        </p>
      </div>
    </section>
  );
}

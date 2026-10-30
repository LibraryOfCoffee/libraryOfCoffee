import SectionHeading from "../SectionHeading/sectionHeading";
import styles from "./conceptSection.module.css";

export default function ConceptSection() {
  return (
    <section className={styles.section}>
      <SectionHeading
        num="01"
        label="Why 豆図書"
        align="center"
        title={
          <>
            「運命の1杯」に出会う、
            <br />
            珈琲の図書館へ
          </>
        }
      />
      <div className={styles.divider} aria-hidden="true" />
      <p className={styles.body}>
        豆を溜めずに、経験を貯める。
        <br />
        様々な自家焙煎店舗・ロースタリーの
        <br />
        こだわり珈琲豆を、30g×3種の
        <br />
        少量多品種でお届け。
        <br />
        ハンドドリップで楽しむ試し飲み体験を、定額で。
      </p>
    </section>
  );
}

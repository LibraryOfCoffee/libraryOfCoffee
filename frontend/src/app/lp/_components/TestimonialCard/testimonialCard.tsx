import Image from "next/image";
import styles from "./testimonialCard.module.css";

interface TestimonialCardProps {
  quote: string;
  name: string;
  meta: string;
  avatarSrc: string;
}

export default function TestimonialCard({
  quote,
  name,
  meta,
  avatarSrc,
}: TestimonialCardProps) {
  return (
    <div className={styles.card}>
      <p className={styles.quote}>{quote}</p>
      <div className={styles.user}>
        <Image
          src={avatarSrc}
          alt={name}
          width={40}
          height={40}
          className={styles.avatar}
        />
        <div className={styles.info}>
          <span className={styles.name}>{name}</span>
          <span className={styles.meta}>{meta}</span>
        </div>
      </div>
    </div>
  );
}

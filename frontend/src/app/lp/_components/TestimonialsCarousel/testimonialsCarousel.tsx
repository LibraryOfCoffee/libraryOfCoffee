import Image from "next/image";
import SectionHeading from "../SectionHeading/sectionHeading";
import styles from "./testimonialsCarousel.module.css";

interface Testimonial {
  quote: string;
  name: string;
  meta: string;
  avatarSrc: string;
}

interface TestimonialsCarouselProps {
  testimonials: Testimonial[];
}

export default function TestimonialsCarousel({
  testimonials,
}: TestimonialsCarouselProps) {
  return (
    <section className={styles.section}>
      <SectionHeading num="07" label="Voices" title="お客様の声" />
      <ul className={styles.list}>
        {testimonials.map((t) => (
          <li key={t.name} className={styles.item}>
            <div className={styles.mark} aria-hidden="true">
              “
            </div>
            <p className={styles.quote}>{t.quote}</p>
            <div className={styles.footer}>
              <Image
                src={t.avatarSrc}
                alt=""
                width={28}
                height={28}
                className={styles.avatarImg}
              />
              <div>
                <div className={styles.name}>{t.name}</div>
                <div className={styles.meta}>{t.meta}</div>
              </div>
            </div>
          </li>
        ))}
      </ul>
    </section>
  );
}

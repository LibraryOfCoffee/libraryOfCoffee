"use client";

import Image from "next/image";
import { Autoplay } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import styles from "./testimonialsCarousel.module.css";

interface Testimonial {
  quote: string;
  name: string;
  meta: string;
  avatarSrc: string;
}

interface TestimonialsCarouselProps {
  testimonials: Testimonial[];
  className?: string;
}

export default function TestimonialsCarousel({
  testimonials,
}: TestimonialsCarouselProps) {
  return (
    <section className={styles.section}>
      <p className={styles.eyebrow}>— VOICE</p>
      <h2 className={styles.headline}>お客様の声</h2>
      <Swiper
        modules={[Autoplay]}
        spaceBetween={12}
        slidesPerView={1.15}
        centeredSlides={false}
        autoplay={{ delay: 5000, disableOnInteraction: false }}
        className={styles.swiper}
      >
        {testimonials.map((t) => (
          <SwiperSlide key={t.name} className={styles.slide}>
            <div className={styles.card}>
              <p className={styles.quote}>{t.quote}</p>
              <div className={styles.footer}>
                <div className={styles.avatarWrap}>
                  <Image
                    src={t.avatarSrc}
                    alt={t.name}
                    width={28}
                    height={28}
                    className={styles.avatarImg}
                  />
                </div>
                <div>
                  <div className={styles.name}>{t.name}</div>
                  <div className={styles.meta}>{t.meta}</div>
                </div>
              </div>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </section>
  );
}

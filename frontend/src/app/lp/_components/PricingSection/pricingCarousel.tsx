"use client";

import { LuChevronLeft, LuChevronRight } from "react-icons/lu";
import { Navigation } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import type { PlanDetail } from "../../_lib/planData";
import PricingCard from "./pricingCard";
import styles from "./pricingSection.module.css";
import "swiper/css";

interface PricingCarouselProps {
  plans: PlanDetail[];
}

export default function PricingCarousel({ plans }: PricingCarouselProps) {
  return (
    <div className={styles.carouselWrap}>
      <button type="button" className={`${styles.navBtn} ${styles.navPrev}`}>
        <LuChevronLeft size={20} />
      </button>
      <Swiper
        modules={[Navigation]}
        spaceBetween={12}
        slidesPerView={1.15}
        centeredSlides={true}
        initialSlide={1}
        navigation={{
          prevEl: `.${styles.navPrev}`,
          nextEl: `.${styles.navNext}`,
        }}
        className="pricing-carousel"
      >
        {plans.map((plan) => (
          <SwiperSlide key={plan.id}>
            <PricingCard plan={plan} />
          </SwiperSlide>
        ))}
      </Swiper>
      <button type="button" className={`${styles.navBtn} ${styles.navNext}`}>
        <LuChevronRight size={20} />
      </button>
    </div>
  );
}

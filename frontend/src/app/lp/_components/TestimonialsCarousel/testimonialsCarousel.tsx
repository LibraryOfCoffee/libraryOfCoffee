"use client";

import { Autoplay } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import TestimonialCard from "../TestimonialCard/testimonialCard";
import "swiper/css";

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
  className,
}: TestimonialsCarouselProps) {
  return (
    <Swiper
      modules={[Autoplay]}
      spaceBetween={12}
      slidesPerView={1.3}
      centeredSlides={true}
      autoplay={{ delay: 5000, disableOnInteraction: false }}
      className={className}
    >
      {testimonials.map((t) => (
        <SwiperSlide key={t.name}>
          <TestimonialCard {...t} />
        </SwiperSlide>
      ))}
    </Swiper>
  );
}

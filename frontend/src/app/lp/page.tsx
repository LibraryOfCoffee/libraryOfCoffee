import type { Metadata } from "next";
import BeanShowcase from "./_components/BeanShowcase/beanShowcase";
import ConceptSection from "./_components/ConceptSection/conceptSection";
import CtaSection from "./_components/CtaSection/ctaSection";
import Footer from "./_components/Footer/footer";
import Header from "./_components/Header/header";
import HeroSection from "./_components/HeroSection/heroSection";
import HowItWorks from "./_components/HowItWorks/howItWorks";
import PartnerShops from "./_components/PartnerShops/partnerShops";
import PricingSection from "./_components/PricingSection/pricingSection";
import TestimonialsCarousel from "./_components/TestimonialsCarousel/testimonialsCarousel";
import { beans } from "./_lib/beanData";
import { JsonLd } from "./_lib/jsonLd";
import "./globals.css";
import styles from "./shared.module.css";

export const metadata: Metadata = {
  title: "色々な珈琲と出会える定額サブスク",
};

const organizationJsonLd = {
  "@context": "https://schema.org" as const,
  "@type": "Organization",
  name: "豆図書",
  url: "https://mametosho.com",
  logo: "https://mametosho.com/logo.png",
};

const webSiteJsonLd = {
  "@context": "https://schema.org" as const,
  "@type": "WebSite",
  name: "豆図書",
  url: "https://mametosho.com",
};

const productJsonLd = {
  "@context": "https://schema.org" as const,
  "@type": "Product",
  name: "豆図書 珈琲豆サブスクリプション",
  description:
    "毎月届く珈琲豆の定額サブスク。注文後焙煎の新鮮な豆30g×3種類をお届け。",
  offers: {
    "@type": "Offer",
    price: "1500",
    priceCurrency: "JPY",
    availability: "https://schema.org/InStock",
  },
};

const testimonials = [
  {
    quote:
      "「量がちょうどよく、豆が余らないのが嬉しいです。少量だからいつも新鮮な味を楽しめます。」",
    name: "Y.Mさん",
    meta: "20代・会社員",
    avatarSrc: "/lpUsers/20man.png",
  },
  {
    quote:
      "「隠れた名店の味を気軽に体験できるのが嬉しいです。お店の淹れ方を参考に、自分に合った抽出を研究しています。」",
    name: "T.Kさん",
    meta: "50代・自営業",
    avatarSrc: "/lpUsers/50man.png",
  },
  {
    quote:
      "「定期的に届く豆が生活の楽しみになりました。毎回違う味に出会えて、自分の\u201C好き\u201Dが少しずつ見えてきます。」",
    name: "T.Aさん",
    meta: "50代・会社員",
    avatarSrc: "/lpUsers/Woman.png",
  },
  {
    quote:
      "「珈琲豆の情報が分かりやすくまとまっているおかげで、さらに自分で調べて\u201Cディグる\u201D楽しさに出会えました。」",
    name: "F.Aさん",
    meta: "20代・学生",
    avatarSrc: "/lpUsers/Student.png",
  },
];

export default function LP2() {
  return (
    <>
      <JsonLd data={organizationJsonLd} />
      <JsonLd data={webSiteJsonLd} />
      <JsonLd data={productJsonLd} />
      <Header />
      <div className={styles.container}>
        <div style={{ height: 56 }} />
        <HeroSection />

        <ConceptSection />

        {/* Bean Selection */}
        <section
          id="beans"
          className={styles.section}
          style={{ background: "var(--color-bg)" }}
        >
          <h2 className={styles.sectionTitle}>初回のラインナップ</h2>
          <p className={styles.sectionSubtitle}>
            毎月8種類の珈琲豆から選べます
          </p>
          <BeanShowcase beans={beans} />
        </section>

        <HowItWorks />

        <PricingSection />

        {/* Testimonials */}
        <section
          id="testimonials"
          className={styles.section}
          style={{
            background: "var(--color-white)",
            paddingLeft: 0,
            paddingRight: 0,
            overflow: "hidden",
          }}
        >
          <h2 className={styles.sectionTitle} style={{ padding: "0 24px" }}>
            お客様の声
          </h2>
          <TestimonialsCarousel
            testimonials={testimonials}
            className={styles.testimonialsSwiper}
          />
        </section>

        <PartnerShops />
        <CtaSection />
      </div>
      <Footer />
    </>
  );
}

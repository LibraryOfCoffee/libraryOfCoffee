import { LuBookOpen, LuHandHeart, LuTruck } from "react-icons/lu";
import BeanShowcase from "./_components/BeanShowcase/beanShowcase";
import CtaSection from "./_components/CtaSection/ctaSection";
import FeatureCard from "./_components/FeatureCard/featureCard";
import Footer from "./_components/Footer/footer";
import Header from "./_components/Header/header";
import HeroSection from "./_components/HeroSection/heroSection";
import HowItWorks from "./_components/HowItWorks/howItWorks";
import PartnerShops from "./_components/PartnerShops/partnerShops";
import PricingSection from "./_components/PricingSection/pricingSection";
import TestimonialsCarousel from "./_components/TestimonialsCarousel/testimonialsCarousel";
import { beans } from "./_lib/beanData";
import "./globals.css";
import styles from "./shared.module.css";

const features = [
  {
    icon: <LuHandHeart size={24} />,
    iconColor: "#8B5A2B",
    title: "毎月3種を自由に選択",
    description: "豊富なラインナップから、あなた好みの豆を毎月選べます",
  },
  {
    icon: <LuTruck size={24} />,
    iconColor: "#D4A574",
    title: "新鮮なまま毎月届く",
    description: "焙煎したての豆を毎月20日前後にお届け。鮮度が違います",
  },
  {
    icon: <LuBookOpen size={24} />,
    iconColor: "#7D9B76",
    title: "コーヒーの知識も深まる",
    description: "産地やテイストの解説カード付き。毎月新しい発見があります",
  },
];

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
      <Header />
      <div className={styles.container}>
        <div style={{ height: 56 }} />
        <HeroSection />

        {/* Features */}
        <section
          id="features"
          className={styles.section}
          style={{ background: "var(--color-white)" }}
        >
          <h2 className={styles.sectionTitle}>豆図書が選ばれる理由</h2>
          <div style={{ display: "flex", flexDirection: "column", gap: 20 }}>
            {features.map((f) => (
              <FeatureCard
                key={f.title}
                icon={f.icon}
                iconColor={f.iconColor}
                title={f.title}
                description={f.description}
              />
            ))}
          </div>
        </section>

        <HowItWorks />

        {/* Bean Selection */}
        <section
          id="beans"
          className={styles.section}
          style={{ background: "var(--color-white)" }}
        >
          <h2 className={styles.sectionTitle}>今月のおすすめ豆</h2>
          <p className={styles.sectionSubtitle}>
            毎月8種類の珈琲豆から選べます
          </p>
          <BeanShowcase beans={beans} />
        </section>

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

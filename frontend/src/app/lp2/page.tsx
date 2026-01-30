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
import styles from "./lp2.module.css";

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
      "「毎月届く豆を選ぶのが楽しみ。解説カードのおかげでコーヒーの知識も増えました！」",
    name: "田中 美咲さん",
    meta: "30代・会社員",
    avatarSrc: "/lp2/user1.png",
  },
  {
    quote:
      "「スーパーの豆とは比べものにならない香り。家族全員がコーヒー好きになりました」",
    name: "佐藤 健太さん",
    meta: "40代・自営業",
    avatarSrc: "/lp2/user2.png",
  },
  {
    quote:
      "「コーヒー初心者でも安心。毎月届くカードで産地の知識が増えて、より深く味わえるようになりました」",
    name: "山本 あやかさん",
    meta: "20代・学生",
    avatarSrc: "/lp2/user3.png",
  },
  {
    quote:
      "「在宅勤務のお供に最高。毎朝の楽しみができて、仕事の効率も上がった気がします」",
    name: "高橋 誠さん",
    meta: "30代・エンジニア",
    avatarSrc: "/lp2/user4.png",
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

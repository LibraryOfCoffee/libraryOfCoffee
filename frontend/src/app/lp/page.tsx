export const dynamic = "force-dynamic";

import type { Metadata } from "next";
import BeanShowcase from "./_components/BeanShowcase/beanShowcase";
import ConceptSection from "./_components/ConceptSection/conceptSection";
import CtaSection from "./_components/CtaSection/ctaSection";
import ExperienceSection from "./_components/ExperienceSection/experienceSection";
import FloatingCta from "./_components/FloatingCta/floatingCta";
import Footer from "./_components/Footer/footer";
import Header from "./_components/Header/header";
import HeroSection from "./_components/HeroSection/heroSection";
import HowItWorks from "./_components/HowItWorks/howItWorks";
import PartnerShops from "./_components/PartnerShops/partnerShops";
import PricingSection from "./_components/PricingSection/pricingSection";
import TestimonialsCarousel from "./_components/TestimonialsCarousel/testimonialsCarousel";
import { fetchCoffeeBeans } from "./_lib/coffeeBeanApi";
import { JsonLd } from "./_lib/jsonLd";
import { fetchPlans } from "./_lib/planApi";
import { fetchShops } from "./_lib/shopApi";
import "./globals.css";
import pageStyles from "./page.module.css";

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
    "毎月届く珈琲豆の定額サブスク。注文後焙煎の新鮮な豆30g〜90g×3〜5種類をお届け。",
  image: "https://mametosho.com/ogImageRectangle.jpeg",
  offers: {
    "@type": "AggregateOffer",
    lowPrice: "1500",
    highPrice: "6750",
    priceCurrency: "JPY",
    offerCount: "9",
    availability: "https://schema.org/InStock",
    shippingDetails: {
      "@type": "OfferShippingDetails",
      shippingRate: {
        "@type": "MonetaryAmount",
        value: "0",
        currency: "JPY",
      },
      shippingDestination: {
        "@type": "DefinedRegion",
        addressCountry: "JP",
      },
    },
    hasMerchantReturnPolicy: {
      "@type": "MerchantReturnPolicy",
      applicableCountry: "JP",
      returnPolicyCategory: "https://schema.org/MerchantReturnNotPermitted",
    },
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
      "「定期的に届く豆が生活の楽しみになりました。毎回違う味に出会えて、自分の“好き”が少しずつ見えてきます。」",
    name: "T.Aさん",
    meta: "50代・会社員",
    avatarSrc: "/lpUsers/Woman.png",
  },
  {
    quote:
      "「珈琲豆の情報が分かりやすくまとまっているおかげで、さらに自分で調べて“ディグる”楽しさに出会えました。」",
    name: "F.Aさん",
    meta: "20代・学生",
    avatarSrc: "/lpUsers/Student.png",
  },
];

export default async function LpPage() {
  const [beans, planGroups, shops] = await Promise.all([
    fetchCoffeeBeans(),
    fetchPlans(),
    fetchShops(),
  ]);

  return (
    <>
      <JsonLd data={organizationJsonLd} />
      <JsonLd data={webSiteJsonLd} />
      <JsonLd data={productJsonLd} />
      <Header />
      <main className={pageStyles.pageMain}>
        <HeroSection />
        <ConceptSection />
        <ExperienceSection />
        <BeanShowcase beans={beans} plans={planGroups} />
        <HowItWorks />
        <PricingSection planGroups={planGroups} />
        <TestimonialsCarousel testimonials={testimonials} />
        <PartnerShops shops={shops} />
        <CtaSection />
      </main>
      <div className={pageStyles.pageFooterWrap}>
        <Footer />
      </div>
      <FloatingCta />
    </>
  );
}

"use client";

import Image from "next/image";
import { Autoplay } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import FeatureCard from "./_components/FeatureCard/featureCard";
import FlowSection from "./_components/FlowSection/flowSection";
import Footer from "./_components/Footer/footer";
import Header from "./_components/Header/header";
import PricingCard from "./_components/PricingCard/pricingCard";
import Section from "./_components/Section/section";
import ShopSection from "./_components/ShopSection/shopSection";
import UserVoiceCard from "./_components/UserVoiceCard/userVoiceCard";
import "swiper/css";
import "./lp.css";
import { moveToCoffeeBeanListPage} from "./_lib/PurchaseLinkUtil/purchaseLinkUtil";

const featureList = [
  {
    number: "01",
    title: "選べる",
    iconElement: (
      <Image
        src="/lpIcons/36576_1.png"
        alt="lp-icons-36576"
        height={120}
        width={120}
      />
    ),
    subtitle: "毎月変わる店舗から気になる珈琲豆を選択",
    details: [
      "1種類当たり30gで気軽にチャレンジ",
      "どの珈琲豆を選んでも、定額1,500円",
      "普段行けない地元で愛される店舗",
    ],
  },
  {
    number: "02",
    title: "試せる",
    iconElement: (
      <Image
        src="/lpIcons/36583_1.png"
        alt="lp-icons-36583"
        height={120}
        width={120}
      />
    ),
    subtitle: "気になる豆2種類とおすすめ1種類の合計3種類が届く",
    details: [
      "自分の気になる豆を体験",
      "未知なる珈琲豆との出会い",
      "注文後の焙煎なので新鮮な豆が届く",
    ],
  },
  {
    number: "03",
    title: "知れる",
    iconElement: (
      <Image
        src="/lpIcons/36573_1.png"
        alt="lp-icons-36573"
        height={120}
        width={120}
      />
    ),
    subtitle: "店舗のこだわりや珈琲豆の特徴を分かりやすく紹介",
    details: [
      "店舗のこだわりがわかる",
      "豆の産地や焙煎などの背景を知れる",
      "店舗おすすめの淹れ方が体験可能",
    ],
  },
];

export default function LP() {
  return (
    <>
      <Header />
      <div className="lp-container">
        {/* Hero Section */}
        <section className="lp-hero">
          <div className="lp-hero-container">
            <div className="lp-hero-inner">
              {/* Background Image Placeholder */}
              <div className="lp-hero-background">
                <div className="lp-hero-gradient"></div>
              </div>

              <div className="lp-hero-content">
                {/* FIXME: img部分仮置き */}
                <div className="lp-hero-main-image">
                  <Image
                    src="/heroImage.png"
                    alt="lp-hero-gradient"
                    height={300}
                    width={9900}
                  />
                </div>
                <h1 className="lp-hero-title">色々な珈琲と出会える豆図書</h1>
                <p className="lp-hero-subtitle">
                  高品質な珈琲豆を30gで気軽にお試し。
                  <br />
                  自分好みの珈琲豆を見つけたい・美味しい入れ方を探求したいあなたに。
                </p>
                <button
                  type="button"
                  className="lp-button-large"
                  onClick={() => moveToCoffeeBeanListPage()}
                >
                  今すぐ始める
                </button>
              </div>
            </div>
          </div>
        </section>
        {/* Features Section */}
        <Section
          id="features"
          title={"豆図書のメリット"}
          subtitle={`「選べる」「試せる」「知れる」　様々な珈琲を体験できる。`}
        >
          <div className="lp-features-grid">
            {featureList.map((feature, index) => (
              <FeatureCard
                key={index}
                number={feature.number}
                mainTitle={feature.title}
                iconElement={feature.iconElement}
                heading={feature.subtitle}
                features={feature.details}
              />
            ))}
          </div>
        </Section>

        <Section
          id="testimonials"
          title={"色々な珈琲豆と出会える場所。豆図書"}
          subtitle={"利用者の声"}
        >
          <Swiper
            modules={[Autoplay]}
            spaceBetween={20}
            slidesPerView={1.3}
            centeredSlides={true}
            autoplay={{
              delay: 5000,
              disableOnInteraction: false,
            }}
            breakpoints={{
              768: {
                slidesPerView: 2.2,
                centeredSlides: false,
              },
              1024: {
                slidesPerView: 3,
                centeredSlides: false,
              },
            }}
            className="lp-testimonials-swiper"
          >
            <SwiperSlide>
              <UserVoiceCard
                imageUrl="/lpUsers/20man.png"
                userName="Y.Mさん"
                userAge="20代"
                userGender="男性"
                userOccupation="会社員"
                tags={["休日は家で珈琲", "新鮮さが大切"]}
                comment={
                  "量がちょうどよく、豆が余らないのが嬉しいです。少量だからいつも新鮮な味を楽しめます。"
                }
              />
            </SwiperSlide>
            <SwiperSlide>
              <UserVoiceCard
                imageUrl="/lpUsers/50man.png"
                userName="T.Kさん"
                userAge="50代"
                userGender="男性"
                userOccupation="自営業"
                tags={["抽出の探究", "お家で名店の味"]}
                comment={
                  "普段は行けない隠れた名店の味を気軽に体験できるのが嬉しいです。お店の入れ方を参考に、自分に合った抽出を研究しています。"
                }
              />
            </SwiperSlide>
            <SwiperSlide>
              <UserVoiceCard
                imageUrl="/lpUsers/Woman.png"
                userName="T.Aさん"
                userAge="50代"
                userGender="女性"
                userOccupation="会社員"
                tags={["新しい出会い", "自分だけの一杯"]}
                comment={
                  '定期的に届く豆が生活の楽しみになりました。毎回違う味に出会えて、自分の"好き"が少しずつ見えてきます。'
                }
              />
            </SwiperSlide>
            <SwiperSlide>
              <UserVoiceCard
                imageUrl="/lpUsers/Student.png"
                userName="F.Aさん"
                userAge="20代"
                userGender="男性"
                userOccupation="学生"
                tags={["珈琲をディグる", "味の背景まで楽しむ"]}
                comment={
                  '珈琲豆の情報が分かりやすくまとまっているおかげで、さらに自分で調べて"ディグる"楽しさに出会えました。'
                }
              />
            </SwiperSlide>
          </Swiper>
          <p className="lp-testimonials-note">
            ※サービスα版にてご購入いただいた方の声
          </p>
        </Section>
        <Section
          id="flow"
          title={"ご利用の流れ"}
          subtitle={"簡単ステップで珈琲体験をスタート"}
        >
          <FlowSection />
        </Section>
        <Section id="pricing" title={"利用料"} subtitle={"料金プラン"}>
          <div className="lp-pricing-grid">
            <PricingCard
              planName="スタンダード"
              planSubtitle={`様々な珈琲店舗の豆を\n毎月3種類体験できるプラン`}
              price={1500}
              campaignPrice={1200}
              campaignText="初月お得キャンペーン実施中！"
              taxIncluded={true}
              freeShipping={true}
              coffeeTypes={3}
              cupsPerType={3}
              totalCups={9}
              initialSetItems={[
                { text: "気になる珈琲店舗から豆をセレクト" },
                { text: "珈琲豆30g×3種類" },
                { text: "各店舗の淹れ方解説付き" },
              ]}
              ctaText="このプランで始める"
              onCtaClick={() => moveToCoffeeBeanListPage()}
            />
          </div>
        </Section>
        <ShopSection />
      </div>
      <Footer />
    </>
  );
}

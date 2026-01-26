import type { Metadata } from "next";
import { OrganizationJsonLd, ProductJsonLd } from "@/components/JsonLd";

const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || "https://mamezusho.com";

export const metadata: Metadata = {
  title: "豆図書 | 色々な珈琲と出会える珈琲豆お試しサービス",
  description:
    "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。注文後焙煎の新鮮な豆で、自分好みの一杯を見つけませんか？定額1,500円(税込・送料無料)で、店舗のこだわりや豆の特徴を学びながら様々な珈琲体験を。",
  keywords: [
    "珈琲豆",
    "コーヒー豆",
    "お試し",
    "サブスクリプション",
    "焙煎",
    "スペシャルティコーヒー",
    "豆図書",
  ],
  openGraph: {
    title: "豆図書 | 色々な珈琲と出会える珈琲豆お試しサービス",
    description:
      "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。定額1,500円(税込・送料無料)。",
    url: `${baseUrl}/lp`,
    siteName: "豆図書",
    locale: "ja_JP",
    type: "website",
    images: [
      {
        url: `${baseUrl}/og-image.png`,
        width: 1200,
        height: 630,
        alt: "豆図書 - 色々な珈琲と出会える",
      },
    ],
  },
  twitter: {
    card: "summary_large_image",
    title: "豆図書 | 色々な珈琲と出会える珈琲豆お試しサービス",
    description:
      "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。定額1,500円(税込・送料無料)。",
    images: [`${baseUrl}/og-image.png`],
  },
  alternates: {
    canonical: `${baseUrl}/lp`,
  },
};

export default function LpLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <OrganizationJsonLd />
      <ProductJsonLd />
      {children}
    </>
  );
}

import type { Metadata } from "next";
import {
  EB_Garamond,
  Shippori_Mincho,
  Zen_Kaku_Gothic_New,
} from "next/font/google";
import "./globals.css";
import { GoogleAnalytics } from "@next/third-parties/google";
import { MicrosoftClarity } from "./MicrosoftClarity";

const shipporiMincho = Shippori_Mincho({
  weight: ["400", "500", "600"],
  subsets: ["latin"],
  variable: "--font-shippori",
  display: "swap",
  preload: false,
});

const ebGaramond = EB_Garamond({
  weight: ["400", "500"],
  style: ["normal", "italic"],
  subsets: ["latin"],
  variable: "--font-eb-garamond",
  display: "swap",
});

const zenKakuGothicNew = Zen_Kaku_Gothic_New({
  weight: ["400", "500", "700"],
  subsets: ["latin"],
  variable: "--font-zen-kaku",
  display: "swap",
  preload: false,
});

export const metadata: Metadata = {
  metadataBase: new URL(process.env.URL ?? "https://mametosho.com"),
  title: {
    default: "豆図書",
    template: "%s | 豆図書",
  },
  description:
    "珈琲豆のサブスク「豆図書」。豆を溜めずに、経験を貯める。様々な自家焙煎店舗・ロースタリーのこだわり自家焙煎珈琲豆を30g×3種の少量多品種でお届け。ハンドドリップで楽しむ試し飲み体験を、定額で。",
  openGraph: {
    title: "豆図書",
    description:
      "珈琲豆のサブスク「豆図書」。豆を溜めずに、経験を貯める。様々な自家焙煎店舗・ロースタリーのこだわり自家焙煎珈琲豆を30g×3種の少量多品種でお届け。ハンドドリップで楽しむ試し飲み体験を、定額で。",
    siteName: "豆図書",
    locale: "ja_JP",
    type: "website",
    images: [
      {
        url: "/ogImageRectangle.jpeg",
        width: 1200,
        height: 630,
      },
    ],
  },
  twitter: {
    card: "summary",
    images: ["/ogImageSquare.jpeg"],
  },
  icons: {
    icon: "/favicon.ico",
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="ja"
      className={`${shipporiMincho.variable} ${ebGaramond.variable} ${zenKakuGothicNew.variable}`}
    >
      <body>{children}</body>
      <GoogleAnalytics gaId={process.env.GOOGLE_GA_ID ?? "no-config"} />
      <MicrosoftClarity
        projectId={process.env.MICROSOFT_CLARITY_ID ?? "no-config"}
      />
    </html>
  );
}

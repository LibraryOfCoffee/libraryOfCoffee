import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { GoogleAnalytics } from "@next/third-parties/google";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || "https://mamezusho.com";

export const metadata: Metadata = {
  metadataBase: new URL(baseUrl),
  title: {
    default: "豆図書",
    template: "%s | 豆図書",
  },
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
  authors: [{ name: "豆図書" }],
  creator: "豆図書",
  publisher: "豆図書",
  openGraph: {
    title: "豆図書",
    description:
      "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。注文後焙煎の新鮮な豆で、自分好みの一杯を見つけませんか？定額1,500円(税込・送料無料)で、店舗のこだわりや豆の特徴を学びながら様々な珈琲体験を。",
    type: "website",
    url: baseUrl,
    siteName: "豆図書",
    locale: "ja_JP",
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
    title: "豆図書",
    description:
      "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。定額1,500円(税込・送料無料)。",
    images: [`${baseUrl}/og-image.png`],
  },
  alternates: {
    canonical: baseUrl,
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      "max-video-preview": -1,
      "max-image-preview": "large",
      "max-snippet": -1,
    },
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ja">
      <body className={`${geistSans.variable} ${geistMono.variable}`}>
        {children}
      </body>
      <GoogleAnalytics gaId={process.env.GOOGLE_GA_ID ?? "no-config"} />
    </html>
  );
}

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

export const metadata: Metadata = {
  metadataBase: new URL("https://mametosho.com"),
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
  },
  twitter: {
    card: "summary_large_image",
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

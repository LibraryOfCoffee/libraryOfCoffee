import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "豆図書",
  description: "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。注文後焙煎の新鮮な豆で、自分好みの一杯を見つけませんか？定額1,500円(税込・送料無料)で、店舗のこだわりや豆の特徴を学びながら様々な珈琲体験を。",
  openGraph: {
    title: "豆図書",
    description: "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。注文後焙煎の新鮮な豆で、自分好みの一杯を見つけませんか？定額1,500円(税込・送料無料)で、店舗のこだわりや豆の特徴を学びながら様々な珈琲体験を。",
    type: "website",
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
    </html>
  );
}

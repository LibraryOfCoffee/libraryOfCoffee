import type { Metadata } from "next";
import {
  EB_Garamond,
  Shippori_Mincho,
  Zen_Kaku_Gothic_New,
} from "next/font/google";

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
  alternates: {
    canonical: "/lp",
  },
};

export default function LpLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <div
      className={`${shipporiMincho.variable} ${ebGaramond.variable} ${zenKakuGothicNew.variable}`}
    >
      {children}
    </div>
  );
}

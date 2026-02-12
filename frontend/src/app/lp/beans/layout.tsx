import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "豆を選ぶ",
  description:
    "毎月届く珈琲豆を選びましょう。個性豊かなロースターが焙煎するこだわりの豆から、お好みの3種類をお選びいただけます。",
  alternates: {
    canonical: "/lp/beans",
  },
};

export default function BeansLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return <>{children}</>;
}

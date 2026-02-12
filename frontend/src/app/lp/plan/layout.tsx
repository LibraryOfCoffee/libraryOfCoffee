import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "プラン選択",
  description:
    "豆図書のサブスクリプションプランを選択。月額1,500円（税込・送料無料）で、注文後焙煎の新鮮な珈琲豆30g×3種類をお届けします。",
  alternates: {
    canonical: "/lp/plan",
  },
};

export default function PlanLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return <>{children}</>;
}

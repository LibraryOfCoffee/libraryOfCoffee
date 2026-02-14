import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "プラン選択",
  alternates: {
    canonical: "/lp/plan",
  },
};

export default function PlanLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return <>{children}</>;
}

import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "豆を選ぶ",
  alternates: {
    canonical: "/lp/beans",
  },
};

export default function BeansLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return <>{children}</>;
}

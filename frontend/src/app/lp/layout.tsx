import type { Metadata } from "next";

export const metadata: Metadata = {
  alternates: {
    canonical: "/lp",
  },
};

export default function LpLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return <>{children}</>;
}

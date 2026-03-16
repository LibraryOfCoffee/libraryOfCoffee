export type PlanId = "cbl-3b-30g" | "cbl-4b-30g" | "cbl-5b-30g";

export interface PlanDetail {
  id: PlanId;
  name: string;
  price: number;
  totalBeans: number;
  maxSelection: number;
  description: string;
  catchphrase?: string;
  badge?: string;
}

export const plans: PlanDetail[] = [
  {
    id: "cbl-3b-30g",
    name: "はじめてプラン",
    price: 1500,
    totalBeans: 3,
    maxSelection: 2,
    description: "30g × 3種類 / 約6〜9杯分",
    catchphrase: "お手頃値段で、サービスを体験したいあなたへ",
  },
  {
    id: "cbl-4b-30g",
    name: "定番プラン",
    price: 1950,
    totalBeans: 4,
    maxSelection: 3,
    description: "30g × 4種類 / 約8〜12杯分",
    catchphrase: "毎月の珈琲時間をもっと豊かにしたいあなたへ",
    badge: "おすすめ",
  },
  {
    id: "cbl-5b-30g",
    name: "たっぷりプラン",
    price: 2350,
    totalBeans: 5,
    maxSelection: 4,
    description: "30g × 5種類 / 約10〜15杯分",
    catchphrase: "たくさんの味に出会って、自分の好みを見つけたいあなたへ",
  },
];

export const VALID_PLAN_IDS: string[] = plans.map((p) => p.id);

export function getPlanById(id: string): PlanDetail | undefined {
  return plans.find((p) => p.id === id);
}

export function formatPrice(price: number): string {
  return price.toLocaleString("ja-JP");
}

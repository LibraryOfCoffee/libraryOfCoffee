export type WeightGrams = 30 | 60 | 90;

export type PlanId =
  | "cbl-3b-30g"
  | "cbl-3b-60g"
  | "cbl-3b-90g"
  | "cbl-4b-30g"
  | "cbl-4b-60g"
  | "cbl-4b-90g"
  | "cbl-5b-30g"
  | "cbl-5b-60g"
  | "cbl-5b-90g"
  | "cbl-3b-30g-exclusive"
  | "cbl-3b-60g-exclusive"
  | "cbl-3b-90g-exclusive"
  | "cbl-4b-30g-exclusive"
  | "cbl-4b-60g-exclusive"
  | "cbl-4b-90g-exclusive"
  | "cbl-5b-30g-exclusive"
  | "cbl-5b-60g-exclusive"
  | "cbl-5b-90g-exclusive";

export interface PlanDetail {
  id: PlanId;
  discountPlanId: PlanId;
  name: string;
  price: number;
  discountPrice: number;
  totalBeans: number;
  maxSelection: number;
  description: string;
  weight: WeightGrams;
  catchphrase?: string;
  badge?: string;
}

export const WEIGHT_OPTIONS: WeightGrams[] = [30, 60, 90];

interface BasePlan {
  name: string;
  totalBeans: number;
  maxSelection: number;
  catchphrase?: string;
  badge?: string;
  variants: Record<
    WeightGrams,
    { price: number; discountPrice: number; description: string }
  >;
}

const basePlans: BasePlan[] = [
  {
    name: "はじめてプラン",
    totalBeans: 3,
    maxSelection: 2,
    catchphrase: "お手頃値段で、サービスを体験したいあなたへ",
    variants: {
      30: {
        price: 1500,
        discountPrice: 980,
        description: "30g × 3種類 / 約6〜9杯分",
      },
      60: {
        price: 2900,
        discountPrice: 2180,
        description: "60g × 3種類 / 約12〜18杯分",
      },
      90: {
        price: 4200,
        discountPrice: 3180,
        description: "90g × 3種類 / 約18〜27杯分",
      },
    },
  },
  {
    name: "定番プラン",
    totalBeans: 4,
    maxSelection: 3,
    catchphrase: "毎月の珈琲時間をもっと豊かにしたいあなたへ",
    badge: "おすすめ",
    variants: {
      30: {
        price: 1950,
        discountPrice: 1280,
        description: "30g × 4種類 / 約8〜12杯分",
      },
      60: {
        price: 3800,
        discountPrice: 2780,
        description: "60g × 4種類 / 約16〜24杯分",
      },
      90: {
        price: 5550,
        discountPrice: 4180,
        description: "90g × 4種類 / 約24〜36杯分",
      },
    },
  },
  {
    name: "たっぷりプラン",
    totalBeans: 5,
    maxSelection: 4,
    catchphrase: "たくさんの味に出会って、自分の好みを見つけたいあなたへ",
    variants: {
      30: {
        price: 2350,
        discountPrice: 1580,
        description: "30g × 5種類 / 約10〜15杯分",
      },
      60: {
        price: 4600,
        discountPrice: 3380,
        description: "60g × 5種類 / 約20〜30杯分",
      },
      90: {
        price: 6750,
        discountPrice: 5180,
        description: "90g × 5種類 / 約30〜45杯分",
      },
    },
  },
];

function generatePlans(): PlanDetail[] {
  const result: PlanDetail[] = [];
  for (const base of basePlans) {
    for (const weight of WEIGHT_OPTIONS) {
      const variant = base.variants[weight];
      result.push({
        id: `cbl-${base.totalBeans}b-${weight}g` as PlanId,
        discountPlanId:
          `cbl-${base.totalBeans}b-${weight}g-exclusive` as PlanId,
        name: base.name,
        price: variant.price,
        discountPrice: variant.discountPrice,
        totalBeans: base.totalBeans,
        maxSelection: base.maxSelection,
        description: variant.description,
        weight,
        catchphrase: base.catchphrase,
        badge: base.badge,
      });
    }
  }
  return result;
}

export const plans: PlanDetail[] = generatePlans();

export const VALID_PLAN_IDS: string[] = plans.flatMap((p) => [
  p.id,
  p.discountPlanId,
]);

export function getPlanById(id: string): PlanDetail | undefined {
  return plans.find((p) => p.id === id || p.discountPlanId === id);
}

const plansByWeight: Record<WeightGrams, PlanDetail[]> = {
  30: plans.filter((p) => p.weight === 30),
  60: plans.filter((p) => p.weight === 60),
  90: plans.filter((p) => p.weight === 90),
};

export function getPlansForWeight(weight: WeightGrams): PlanDetail[] {
  return plansByWeight[weight];
}

export function formatPrice(price: number): string {
  return price.toLocaleString("ja-JP");
}

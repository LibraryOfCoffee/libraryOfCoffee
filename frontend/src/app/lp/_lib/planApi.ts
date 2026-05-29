export type WeightGrams = 30 | 60 | 90;

export const WEIGHT_OPTIONS: WeightGrams[] = [30, 60, 90];

export interface PlanDetail {
  id: string;
  name: string;
  price: number;
  singlePrice: number;
  totalBeans: number;
  maxSelection: number;
  description: string;
  weight: WeightGrams;
  catchphrase?: string;
  badge?: string;
}

export function formatPrice(price: number): string {
  return price.toLocaleString("ja-JP");
}

export type PlanGroup = {
  subscriptionId: string;
  singleId: string;
  label: string;
  gramWeight: WeightGrams;
  beanQuantity: number;
  subscriptionPrice: number;
  singlePrice: number;
  maxSelection: number;
  isRecommended: boolean;
};

type PlanApiItem = {
  id: string;
  label: string;
  gramWeight: number;
  beanQuantity: number;
  price: number;
  type: "SUBSCRIPTION" | "SINGLE";
  isRecommended: boolean;
};

export function groupPlansByGram(
  groups: PlanGroup[],
  gramWeight: WeightGrams,
): PlanGroup[] {
  return groups.filter((g) => g.gramWeight === gramWeight);
}

export async function fetchPlans(): Promise<PlanGroup[]> {
  const baseUrl = process.env.CS_API_BASE_URL ?? "http://localhost:8080";
  let items: PlanApiItem[];
  try {
    const res = await fetch(`${baseUrl}/api/plans`, {
      cache: "no-store",
    });
    if (!res.ok) return [];
    items = await res.json();
  } catch {
    return [];
  }

  // SUBSCRIPTION と SINGLE を label+gramWeight でペアリング
  const subMap = new Map<string, PlanApiItem>();
  const singleMap = new Map<string, PlanApiItem>();
  for (const item of items) {
    const key = `${item.label}-${item.gramWeight}`;
    if (item.type === "SUBSCRIPTION") subMap.set(key, item);
    else singleMap.set(key, item);
  }

  const groups: PlanGroup[] = [];
  for (const [key, sub] of subMap.entries()) {
    const single = singleMap.get(key);
    if (!single) continue;
    groups.push({
      subscriptionId: sub.id,
      singleId: single.id,
      label: sub.label,
      gramWeight: sub.gramWeight as WeightGrams,
      beanQuantity: sub.beanQuantity,
      subscriptionPrice: sub.price,
      singlePrice: single.price,
      maxSelection: sub.beanQuantity - 1,
      isRecommended: sub.isRecommended,
    });
  }

  // gram → label の順でソート
  const GRAM_ORDER = [30, 60, 90];
  const LABEL_ORDER = ["はじめて", "定番", "たっぷり"];
  return groups.sort((a, b) => {
    if (a.gramWeight !== b.gramWeight)
      return (
        GRAM_ORDER.indexOf(a.gramWeight) - GRAM_ORDER.indexOf(b.gramWeight)
      );
    return LABEL_ORDER.indexOf(a.label) - LABEL_ORDER.indexOf(b.label);
  });
}

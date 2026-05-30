export type TasteProfile = {
  label: string;
  value: number;
};

export interface BeanDetail {
  id: string;
  imageSrc: string;
  tag: string;
  tagColor: string;
  name: string;
  subName: string;
  region: string;
  description: string;
  detailDescription: string;
  origin: string;
  farm: string;
  roastLevel: string;
  processing: string;
  roaster: string;
  roasterLink: string;
  prefecture: string;
  tasteProfile: TasteProfile[];
  isSpecialty: boolean;
}

export const SPECIALTY_TAG_COLOR = "#C4972A";

type TasteProfileApiItem = {
  name: string;
  value: number;
};

type CoffeeBeanApiItem = {
  id: string;
  name: string;
  origin: string;
  roastLevel: "LIGHT" | "MEDIUM" | "CITY" | "FRENCH";
  processingMethod: string;
  isSpecialty: boolean;
  description: string;
  imageUrl: string;
  shopName: string;
  shopPrefecture: string;
  shopUrl: string;
  tasteProfiles: TasteProfileApiItem[];
};

type BeansApiResponse = {
  items: CoffeeBeanApiItem[];
  totalCount: number;
  page: number;
  size: number;
};

const ROAST_LEVEL_JP: Record<string, string> = {
  LIGHT: "浅煎り",
  MEDIUM: "中煎り",
  CITY: "中深煎り",
  FRENCH: "深煎り",
};

const ROAST_TAG_COLOR: Record<string, string> = {
  LIGHT: "#E2B36E",
  MEDIUM: "#A6683D",
  CITY: "#5C3317",
  FRENCH: "#321E14",
};

function toBeanDetail(item: CoffeeBeanApiItem): BeanDetail {
  const roastJP = ROAST_LEVEL_JP[item.roastLevel] ?? item.roastLevel;
  const tagColor = ROAST_TAG_COLOR[item.roastLevel] ?? "#A6683D";
  return {
    id: item.id,
    imageSrc: item.imageUrl,
    tag: roastJP,
    tagColor,
    name: item.origin,
    subName: item.name,
    region: item.name,
    description: item.description,
    detailDescription: item.description,
    origin: item.origin,
    farm: "",
    roastLevel: roastJP,
    processing: item.processingMethod,
    roaster: item.shopName,
    roasterLink: item.shopUrl,
    prefecture: item.shopPrefecture,
    tasteProfile: item.tasteProfiles.map((t) => ({
      label: t.name,
      value: t.value,
    })),
    isSpecialty: item.isSpecialty,
  };
}

export async function fetchCoffeeBeans(): Promise<BeanDetail[]> {
  const baseUrl = process.env.CS_API_BASE_URL ?? "http://localhost:8080";
  try {
    const res = await fetch(`${baseUrl}/api/coffeeBeans?page=0&size=100`, {
      cache: "no-store",
    });
    if (!res.ok) return [];
    const data: BeansApiResponse = await res.json();
    return data.items.map(toBeanDetail);
  } catch {
    return [];
  }
}

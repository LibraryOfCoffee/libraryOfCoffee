import type { components } from "@/api/generated/cs-api";

export type TasteProfile =
  components["schemas"]["CoffeeBeanResponse"]["tasteProfiles"][number];

export interface BeanDetail {
  id: string;
  shopifyBeanId: string;
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

type CoffeeBeanApiItem = components["schemas"]["CoffeeBeanResponse"];

const PREFECTURE_JP: Record<string, string> = {
  HOKKAIDO: "北海道",
  AOMORI: "青森県",
  IWATE: "岩手県",
  MIYAGI: "宮城県",
  AKITA: "秋田県",
  YAMAGATA: "山形県",
  FUKUSHIMA: "福島県",
  IBARAKI: "茨城県",
  TOCHIGI: "栃木県",
  GUNMA: "群馬県",
  SAITAMA: "埼玉県",
  CHIBA: "千葉県",
  TOKYO: "東京都",
  KANAGAWA: "神奈川県",
  NIIGATA: "新潟県",
  TOYAMA: "富山県",
  ISHIKAWA: "石川県",
  FUKUI: "福井県",
  YAMANASHI: "山梨県",
  NAGANO: "長野県",
  SHIZUOKA: "静岡県",
  AICHI: "愛知県",
  MIE: "三重県",
  SHIGA: "滋賀県",
  KYOTO: "京都府",
  OSAKA: "大阪府",
  HYOGO: "兵庫県",
  NARA: "奈良県",
  WAKAYAMA: "和歌山県",
  TOTTORI: "鳥取県",
  SHIMANE: "島根県",
  OKAYAMA: "岡山県",
  HIROSHIMA: "広島県",
  YAMAGUCHI: "山口県",
  TOKUSHIMA: "徳島県",
  KAGAWA: "香川県",
  EHIME: "愛媛県",
  KOCHI: "高知県",
  FUKUOKA: "福岡県",
  SAGA: "佐賀県",
  NAGASAKI: "長崎県",
  KUMAMOTO: "熊本県",
  OITA: "大分県",
  MIYAZAKI: "宮崎県",
  KAGOSHIMA: "鹿児島県",
  OKINAWA: "沖縄県",
};

const ROAST_LEVEL_JP: Record<string, string> = {
  LIGHT: "浅煎り",
  CINNAMON: "中浅煎り",
  MEDIUM: "中煎り",
  CITY: "中深煎り",
  FRENCH: "深煎り",
};

const ROAST_TAG_COLOR: Record<string, string> = {
  LIGHT: "#E2B36E",
  CINNAMON: "#C48D55",
  MEDIUM: "#A6683D",
  CITY: "#5C3317",
  FRENCH: "#321E14",
};

function toBeanDetail(item: CoffeeBeanApiItem): BeanDetail {
  const roastJP = ROAST_LEVEL_JP[item.roastLevel] ?? item.roastLevel;
  const tagColor = ROAST_TAG_COLOR[item.roastLevel] ?? "#A6683D";
  return {
    id: item.id,
    shopifyBeanId: item.shopifyBeanId,
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
    prefecture: PREFECTURE_JP[item.shopPrefecture] ?? item.shopPrefecture,
    tasteProfile: item.tasteProfiles,
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
    const data: components["schemas"]["CoffeeBeanListResponse"] =
      await res.json();
    return data.items.map(toBeanDetail);
  } catch {
    return [];
  }
}

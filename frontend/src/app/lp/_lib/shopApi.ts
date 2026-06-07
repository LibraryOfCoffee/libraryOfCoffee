import type { components } from "@/api/generated/cs-api";

export type Shop = components["schemas"]["ShopResponse"];

export async function fetchShops(): Promise<Shop[]> {
  const baseUrl = process.env.CS_API_BASE_URL ?? "http://localhost:8080";
  try {
    const res = await fetch(`${baseUrl}/api/shops?page=0&size=100`, {
      cache: "no-store",
    });
    if (!res.ok) return [];
    const data: components["schemas"]["ShopListResponse"] = await res.json();
    return data.items;
  } catch {
    return [];
  }
}

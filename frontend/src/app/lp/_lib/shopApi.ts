import type { components } from "@/api/generated/cs-api";
import { hmacFetch } from "./hmacFetch";

export type Shop = components["schemas"]["ShopResponse"];

export async function fetchShops(): Promise<Shop[]> {
  try {
    const res = await hmacFetch("/api/shops?page=0&size=100", {
      cache: "no-store",
    });
    if (!res.ok) return [];
    const data: components["schemas"]["ShopListResponse"] = await res.json();
    return data.items;
  } catch {
    return [];
  }
}

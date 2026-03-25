"use server";

import { fetchShops } from "@/api/shops";

export async function searchShopsAction(name?: string) {
  const result = await fetchShops(0, 10, name || undefined);
  return result.items.map((shop) => ({ id: shop.id, name: shop.name }));
}

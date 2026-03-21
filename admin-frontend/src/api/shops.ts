import "server-only";

import { createAuthenticatedApiClient } from "@/api/client";

export type ShopListItem = {
  id: string;
  shopifyShopId: string;
  name: string;
  introduction: string | null;
  particular: string | null;
};

export type PagedResponse<T> = {
  items: T[];
  totalCount: number;
  page: number;
  size: number;
};

export async function fetchShops(
  page = 0,
  size = 20,
): Promise<PagedResponse<ShopListItem>> {
  const client = await createAuthenticatedApiClient();
  const { data, error } = await client.GET("/api/admin/shops", {
    params: { query: { page, size } },
  });

  if (error) {
    throw new Error("店舗一覧の取得に失敗しました");
  }

  return data as PagedResponse<ShopListItem>;
}

import "server-only";

import { notFound } from "next/navigation";
import { createAuthenticatedApiClient } from "@/api/client";
import type { components } from "@/api/generated/admin-api";

export type ShopListItem = components["schemas"]["ShopSummaryResponse"];
export type ShopDetail = components["schemas"]["ShopDetailResponse"];
export type ShopListResponse = components["schemas"]["ShopListResponse"];

export type ImageDetail = NonNullable<ShopDetail["images"]>[number];

export async function fetchShops(
  page = 0,
  size = 20,
  name?: string,
): Promise<ShopListResponse> {
  const client = await createAuthenticatedApiClient();
  const { data, error } = await client.GET("/api/admin/shops", {
    params: { query: { page, size, name } },
  });

  if (error) {
    throw new Error("店舗一覧の取得に失敗しました");
  }

  return data;
}

export async function fetchShop(id: string): Promise<ShopDetail> {
  const client = await createAuthenticatedApiClient();
  const { data, response } = await client.GET("/api/admin/shops/{id}", {
    params: { path: { id } },
  });

  if (response.status === 404) {
    notFound();
  }

  if (!data) {
    throw new Error("店舗の取得に失敗しました");
  }

  return data;
}

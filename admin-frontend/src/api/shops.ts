import "server-only";

import { notFound } from "next/navigation";
import { createAuthenticatedApiClient } from "@/api/client";
import type { PagedResponse } from "@/api/types";

export type { PagedResponse } from "@/api/types";

export type ShopListItem = {
  id: string;
  shopifyShopId: string;
  name: string;
  introduction: string | null;
  particular: string | null;
  shopUrl: string;
  prefecture: string;
  publishStatus: "DRAFT" | "PUBLISHED";
};

export type ImageDetail = {
  id: string;
  type: "MAIN" | "LOGO";
  imageUrl: string;
};

export type ShopDetail = ShopListItem & {
  images: ImageDetail[];
};

export async function fetchShops(
  page = 0,
  size = 20,
  name?: string,
): Promise<PagedResponse<ShopListItem>> {
  const client = await createAuthenticatedApiClient();
  const { data, error } = await client.GET("/api/admin/shops", {
    params: { query: { page, size, name } },
  });

  if (error) {
    throw new Error("店舗一覧の取得に失敗しました");
  }

  return data as PagedResponse<ShopListItem>;
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

  return data as ShopDetail;
}

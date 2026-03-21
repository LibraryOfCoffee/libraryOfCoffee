import "server-only";

import { createAuthenticatedApiClient } from "@/api/client";
import type { PagedResponse } from "@/api/types";

export type CoffeeBeanListItem = {
  id: string;
  shopId: string;
  shopifyBeanId: string;
  name: string;
  description: string;
  origin: string;
  farm: string | null;
  roastLevel: string;
  processingMethod: string;
  isSpecialty: boolean;
};

export async function fetchCoffeeBeans(
  page = 0,
  size = 20,
): Promise<PagedResponse<CoffeeBeanListItem>> {
  const client = await createAuthenticatedApiClient();
  const { data, error } = await client.GET("/api/admin/coffee-beans", {
    params: { query: { page, size } },
  });

  if (error) {
    throw new Error("コーヒー豆一覧の取得に失敗しました");
  }

  return data as PagedResponse<CoffeeBeanListItem>;
}

import "server-only";

import { notFound } from "next/navigation";
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
  publishStatus: "DRAFT" | "PUBLISHED";
};

export type CoffeeBeanDetail = CoffeeBeanListItem & {
  images: { id: string; type: string; imageUrl: string }[];
  tastes: {
    id: string;
    tasteId: string;
    tasteName: string;
    evaluationValue: number;
  }[];
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

export async function fetchCoffeeBean(id: string): Promise<CoffeeBeanDetail> {
  const client = await createAuthenticatedApiClient();
  const { data, response } = await client.GET("/api/admin/coffee-beans/{id}", {
    params: { path: { id } },
  });

  if (response.status === 404) {
    notFound();
  }

  if (!data) {
    throw new Error("コーヒー豆の取得に失敗しました");
  }

  return data as CoffeeBeanDetail;
}

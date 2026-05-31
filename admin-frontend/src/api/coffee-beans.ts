import "server-only";

import { notFound } from "next/navigation";
import { createAuthenticatedApiClient } from "@/api/client";
import type { components } from "@/api/generated/admin-api";

export type CoffeeBeanListItem =
  components["schemas"]["CoffeeBeanSummaryResponse"];
export type CoffeeBeanDetail =
  components["schemas"]["CoffeeBeanDetailResponse"];
export type CoffeeBeanListResponse =
  components["schemas"]["CoffeeBeanListResponse"];

export async function fetchCoffeeBeans(
  page = 0,
  size = 20,
): Promise<CoffeeBeanListResponse> {
  const client = await createAuthenticatedApiClient();
  const { data, error } = await client.GET("/api/admin/coffee-beans", {
    params: { query: { page, size } },
  });

  if (error) {
    throw new Error("コーヒー豆一覧の取得に失敗しました");
  }

  return data;
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

  return data;
}

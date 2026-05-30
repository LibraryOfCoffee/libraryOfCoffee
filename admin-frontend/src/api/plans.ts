import "server-only";

import { notFound } from "next/navigation";
import { createAuthenticatedApiClient } from "@/api/client";
import type { PagedResponse } from "@/api/types";

export type { PagedResponse } from "@/api/types";

export type PlanType = "SUBSCRIPTION" | "SINGLE";

export type PlanListItem = {
  id: string;
  shopifyPlanId: string;
  label: string;
  gramWeight: number;
  beanQuantity: number;
  price: number;
  type: PlanType;
  isRecommended: boolean;
};

export type PlanDetail = PlanListItem;

export async function fetchPlans(
  page = 0,
  size = 20,
  keyword?: string,
): Promise<PagedResponse<PlanListItem>> {
  const client = await createAuthenticatedApiClient();
  const { data, error } = await client.GET("/api/admin/plans", {
    params: { query: { page, size, keyword } },
  });

  if (error) {
    throw new Error("プラン一覧の取得に失敗しました");
  }

  return data as PagedResponse<PlanListItem>;
}

export async function fetchPlan(id: string): Promise<PlanDetail> {
  const client = await createAuthenticatedApiClient();
  const { data, response } = await client.GET("/api/admin/plans/{id}", {
    params: { path: { id } },
  });

  if (response.status === 404) {
    notFound();
  }

  if (!data) {
    throw new Error("プランの取得に失敗しました");
  }

  return data as PlanDetail;
}

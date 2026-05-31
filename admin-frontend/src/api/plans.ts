import "server-only";

import { notFound } from "next/navigation";
import { createAuthenticatedApiClient } from "@/api/client";
import type { components } from "@/api/generated/admin-api";

export type PlanListItem = components["schemas"]["PlanSummaryResponse"];
export type PlanDetail = components["schemas"]["PlanDetailResponse"];
export type PlanListResponse = components["schemas"]["PlanListResponse"];

export type PlanType = "SUBSCRIPTION" | "SINGLE";

export async function fetchPlans(
  page = 0,
  size = 20,
  keyword?: string,
): Promise<PlanListResponse> {
  const client = await createAuthenticatedApiClient();
  const { data, error } = await client.GET("/api/admin/plans", {
    params: { query: { page, size, keyword } },
  });

  if (error) {
    throw new Error("プラン一覧の取得に失敗しました");
  }

  return data;
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

  return data;
}

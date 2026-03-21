import "server-only";

import { cookies } from "next/headers";
import createClient from "openapi-fetch";
import type { paths } from "@/api/generated/admin-api";

const API_BASE_URL = process.env.API_BASE_URL ?? "http://localhost:8081";

export function createAdminApiClient(accessToken?: string) {
  return createClient<paths>({
    baseUrl: API_BASE_URL,
    headers: accessToken
      ? { Authorization: `Bearer ${accessToken}` }
      : undefined,
  });
}

export async function createAuthenticatedApiClient() {
  const cookieStore = await cookies();
  const accessToken = cookieStore.get("accessToken")?.value ?? "";
  return createAdminApiClient(accessToken);
}

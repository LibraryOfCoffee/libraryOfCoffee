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

export async function multipartRequest(
  path: string,
  method: "POST" | "PUT",
  data: Record<string, unknown>,
  formData: FormData,
): Promise<Response> {
  const cookieStore = await cookies();
  const accessToken = cookieStore.get("accessToken")?.value ?? "";

  const apiFormData = new FormData();
  apiFormData.append(
    "data",
    new Blob([JSON.stringify(data)], { type: "application/json" }),
  );

  const imageFiles = formData.getAll("images") as File[];
  const imageTypes = formData.getAll("imageTypes") as string[];
  imageFiles.forEach((file, index) => {
    if (file.size > 0) {
      apiFormData.append("images", file);
      apiFormData.append("imageTypes", imageTypes[index] ?? "MAIN");
    }
  });

  return fetch(`${API_BASE_URL}${path}`, {
    method,
    headers: { Authorization: `Bearer ${accessToken}` },
    body: apiFormData,
  });
}

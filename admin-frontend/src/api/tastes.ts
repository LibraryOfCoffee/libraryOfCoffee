import "server-only";

import { cookies } from "next/headers";

const API_BASE_URL = process.env.API_BASE_URL ?? "http://localhost:8081";

export type TasteListItem = {
  id: string;
  name: string;
};

/**
 * テイスト一覧を取得する。
 * APIエラー時は例外を throw する。
 */
export async function fetchTastes(): Promise<TasteListItem[]> {
  const cookieStore = await cookies();
  const accessToken = cookieStore.get("accessToken")?.value ?? "";

  const response = await fetch(`${API_BASE_URL}/api/admin/tastes`, {
    headers: { Authorization: `Bearer ${accessToken}` },
  });

  if (!response.ok) {
    throw new Error("テイスト一覧の取得に失敗しました");
  }

  return response.json() as Promise<TasteListItem[]>;
}

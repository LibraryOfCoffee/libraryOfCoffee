import "server-only";

import { cookies } from "next/headers";

const API_BASE_URL = process.env.API_BASE_URL ?? "http://localhost:8081";

export type TasteListItem = {
  id: string;
  name: string;
};

/**
 * テイスト一覧を取得する。
 * APIエラー時は null を返す（空配列 [] との区別のため）。
 */
export async function fetchTastes(): Promise<TasteListItem[] | null> {
  try {
    const cookieStore = await cookies();
    const accessToken = cookieStore.get("accessToken")?.value ?? "";

    const response = await fetch(`${API_BASE_URL}/api/admin/tastes`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });

    if (!response.ok) {
      return null;
    }

    return response.json() as Promise<TasteListItem[]>;
  } catch {
    return null;
  }
}

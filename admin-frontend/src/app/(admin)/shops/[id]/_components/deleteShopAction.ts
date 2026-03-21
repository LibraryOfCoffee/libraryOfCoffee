"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { createAuthenticatedApiClient } from "@/api/client";

export type DeleteShopState = {
  error?: string;
};

export async function deleteShopAction(
  _prevState: DeleteShopState,
  formData: FormData,
): Promise<DeleteShopState> {
  const id = formData.get("id") as string;

  const client = await createAuthenticatedApiClient();
  const { response } = await client.DELETE("/api/admin/shops/{id}", {
    params: { path: { id } },
  });

  if (response.status === 404) {
    return { error: "店舗が見つかりません。" };
  }

  if (!response.ok) {
    return { error: "店舗の削除に失敗しました。" };
  }

  revalidatePath("/shops");
  redirect("/shops");
}

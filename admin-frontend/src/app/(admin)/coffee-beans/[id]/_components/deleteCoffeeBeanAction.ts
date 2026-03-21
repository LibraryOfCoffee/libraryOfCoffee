"use server";

import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";
import { createAuthenticatedApiClient } from "@/api/client";

export type DeleteCoffeeBeanState = {
  error?: string;
};

export async function deleteCoffeeBeanAction(
  _prevState: DeleteCoffeeBeanState,
  formData: FormData,
): Promise<DeleteCoffeeBeanState> {
  const id = formData.get("id") as string;

  const client = await createAuthenticatedApiClient();
  const { response } = await client.DELETE("/api/admin/coffee-beans/{id}", {
    params: { path: { id } },
  });

  if (response.status === 404) {
    return { error: "コーヒー豆が見つかりません。" };
  }

  if (!response.ok) {
    return { error: "コーヒー豆の削除に失敗しました。" };
  }

  revalidatePath("/coffee-beans");
  redirect("/coffee-beans");
}

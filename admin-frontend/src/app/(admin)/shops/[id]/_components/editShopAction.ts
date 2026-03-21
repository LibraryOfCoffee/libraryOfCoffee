"use server";

import { revalidatePath } from "next/cache";
import { z } from "zod/v4";
import { createAuthenticatedApiClient } from "@/api/client";
import {
  type ShopFormState,
  shopFieldsSchema,
} from "@/app/(admin)/shops/_lib/shopFormSchema";

const editShopSchema = shopFieldsSchema.extend({
  id: z.string(),
});

export type EditShopState = ShopFormState;

export async function editShopAction(
  _prevState: EditShopState,
  formData: FormData,
): Promise<EditShopState> {
  const result = editShopSchema.safeParse({
    id: formData.get("id"),
    shopifyShopId: formData.get("shopifyShopId"),
    name: formData.get("name"),
    introduction: formData.get("introduction"),
    particular: formData.get("particular"),
  });

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors };
  }

  const { id, shopifyShopId, name, introduction, particular } = result.data;

  const client = await createAuthenticatedApiClient();
  const { error, response } = await client.PUT("/api/admin/shops/{id}", {
    params: { path: { id } },
    body: {
      shopifyShopId,
      name,
      introduction,
      particular,
      images: [],
    },
  });

  if (error) {
    if (response.status === 409) {
      return { error: "このShopify Shop IDは既に登録されています。" };
    }
    return { error: "店舗の更新に失敗しました。" };
  }

  revalidatePath(`/shops/${id}`);
  revalidatePath("/shops");
  return { success: true };
}

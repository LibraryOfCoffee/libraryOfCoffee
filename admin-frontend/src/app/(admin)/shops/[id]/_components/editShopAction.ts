"use server";

import { revalidatePath } from "next/cache";
import { z } from "zod/v4";
import { multipartRequest } from "@/api/client";
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
  const values = {
    shopifyShopId: (formData.get("shopifyShopId") as string) ?? "",
    name: (formData.get("name") as string) ?? "",
    introduction: (formData.get("introduction") as string) ?? "",
    particular: (formData.get("particular") as string) ?? "",
  };

  const result = editShopSchema.safeParse({
    id: formData.get("id"),
    ...values,
  });

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors, values };
  }

  const { id, shopifyShopId, name, introduction, particular } = result.data;

  const response = await multipartRequest(
    `/api/admin/shops/${id}`,
    "PUT",
    { shopifyShopId, name, introduction, particular },
    formData,
  );

  if (!response.ok) {
    if (response.status === 409) {
      return {
        error: "このShopify Shop IDは既に登録されています。",
        values,
      };
    }
    return { error: "店舗の更新に失敗しました。", values };
  }

  revalidatePath(`/shops/${id}`);
  revalidatePath("/shops");
  return { success: true };
}

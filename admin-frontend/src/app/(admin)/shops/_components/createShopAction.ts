"use server";

import { revalidatePath } from "next/cache";
import { createAuthenticatedApiClient } from "@/api/client";
import {
  type ShopFormState,
  shopFieldsSchema,
} from "@/app/(admin)/shops/_lib/shopFormSchema";

export type CreateShopState = ShopFormState;

export async function createShopAction(
  _prevState: CreateShopState,
  formData: FormData,
): Promise<CreateShopState> {
  const result = shopFieldsSchema.safeParse({
    shopifyShopId: formData.get("shopifyShopId"),
    name: formData.get("name"),
    introduction: formData.get("introduction"),
    particular: formData.get("particular"),
  });

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors };
  }

  const { shopifyShopId, name, introduction, particular } = result.data;

  const client = await createAuthenticatedApiClient();
  const { error, response } = await client.POST("/api/admin/shops", {
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
    return { error: "店舗の登録に失敗しました。" };
  }

  revalidatePath("/shops");
  return { success: true };
}

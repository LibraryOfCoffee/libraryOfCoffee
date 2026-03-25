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
    shopUrl: (formData.get("shopUrl") as string) ?? "",
  };

  const result = editShopSchema.safeParse({
    id: formData.get("id"),
    ...values,
  });

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors, values };
  }

  const imageTypes = formData.getAll("imageTypes") as string[];
  const imageFiles = formData.getAll("images") as File[];
  const hasNewImages = imageFiles.some((file) => file.size > 0);
  if (hasNewImages) {
    const hasLogoImage = imageTypes.some(
      (type, i) => type === "LOGO" && imageFiles[i]?.size > 0,
    );
    if (!hasLogoImage) {
      return { error: "画像を変更する場合、ロゴ画像は必須です。", values };
    }
  }

  const { id, shopifyShopId, name, introduction, particular, shopUrl } =
    result.data;

  const response = await multipartRequest(
    `/api/admin/shops/${id}`,
    "PUT",
    { shopifyShopId, name, introduction, particular, shopUrl },
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

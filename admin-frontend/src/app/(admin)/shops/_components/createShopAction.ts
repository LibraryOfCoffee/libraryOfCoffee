"use server";

import { revalidatePath } from "next/cache";
import { multipartRequest } from "@/api/client";
import {
  type ShopFormState,
  shopFieldsSchema,
} from "@/app/(admin)/shops/_lib/shopFormSchema";

export type CreateShopState = ShopFormState;

export async function createShopAction(
  _prevState: CreateShopState,
  formData: FormData,
): Promise<CreateShopState> {
  const values = {
    shopifyShopId: (formData.get("shopifyShopId") as string) ?? "",
    name: (formData.get("name") as string) ?? "",
    introduction: (formData.get("introduction") as string) ?? "",
    particular: (formData.get("particular") as string) ?? "",
    shopUrl: (formData.get("shopUrl") as string) ?? "",
    prefecture: (formData.get("prefecture") as string) ?? "",
    publishStatus: isChecked(formData, "publishStatus") ? "PUBLISHED" : "DRAFT",
  };

  const result = shopFieldsSchema.safeParse(values);

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors, values };
  }

  const imageTypes = formData.getAll("imageTypes") as string[];
  const imageFiles = formData.getAll("images") as File[];
  const hasLogoImage = imageTypes.some(
    (type, i) => type === "LOGO" && imageFiles[i]?.size > 0,
  );
  if (!hasLogoImage) {
    return { error: "ロゴ画像は必須です。", values };
  }

  const {
    shopifyShopId,
    name,
    introduction,
    particular,
    shopUrl,
    prefecture,
    publishStatus,
  } = result.data;

  const response = await multipartRequest(
    "/api/admin/shops",
    "POST",
    {
      shopifyShopId,
      name,
      introduction,
      particular,
      shopUrl,
      prefecture,
      publishStatus,
    },
    formData,
  );

  if (!response.ok) {
    if (response.status === 409) {
      return {
        error: "このShopify Shop IDは既に登録されています。",
        values,
      };
    }
    return { error: "店舗の登録に失敗しました。", values };
  }

  revalidatePath("/shops");
  return { success: true };
}

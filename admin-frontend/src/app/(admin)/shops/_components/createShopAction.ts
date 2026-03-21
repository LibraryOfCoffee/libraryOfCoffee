"use server";

import { revalidatePath } from "next/cache";
import { z } from "zod/v4";
import { createAuthenticatedApiClient } from "@/api/client";

const createShopSchema = z.object({
  shopifyShopId: z
    .string()
    .min(1, "Shopify Shop IDを入力してください。")
    .max(255, "255文字以内で入力してください。"),
  name: z
    .string()
    .min(1, "店舗名を入力してください。")
    .max(255, "255文字以内で入力してください。"),
  introduction: z
    .string()
    .max(10000, "10,000文字以内で入力してください。")
    .transform((v) => v || undefined)
    .optional(),
  particular: z
    .string()
    .max(10000, "10,000文字以内で入力してください。")
    .transform((v) => v || undefined)
    .optional(),
});

export type CreateShopState = {
  success?: boolean;
  error?: string;
  fieldErrors?: {
    shopifyShopId?: string[];
    name?: string[];
    introduction?: string[];
    particular?: string[];
  };
};

export async function createShopAction(
  _prevState: CreateShopState,
  formData: FormData,
): Promise<CreateShopState> {
  const result = createShopSchema.safeParse({
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

"use server";

import { revalidatePath } from "next/cache";
import { z } from "zod/v4";
import { createAuthenticatedApiClient } from "@/api/client";
import {
  type CoffeeBeanFormState,
  coffeeBeanFieldsSchema,
} from "@/app/(admin)/coffee-beans/_lib/coffeeBeanFormSchema";

const createCoffeeBeanSchema = coffeeBeanFieldsSchema.extend({
  shopId: z.string().min(1, "店舗を選択してください。"),
});

export type CreateCoffeeBeanState = Omit<CoffeeBeanFormState, "fieldErrors"> & {
  fieldErrors?: CoffeeBeanFormState["fieldErrors"] & {
    shopId?: string[];
  };
};

export async function createCoffeeBeanAction(
  _prevState: CreateCoffeeBeanState,
  formData: FormData,
): Promise<CreateCoffeeBeanState> {
  const result = createCoffeeBeanSchema.safeParse({
    shopId: formData.get("shopId"),
    shopifyBeanId: formData.get("shopifyBeanId"),
    name: formData.get("name"),
    description: formData.get("description"),
    origin: formData.get("origin"),
    farm: formData.get("farm"),
    roastLevel: formData.get("roastLevel"),
    processingMethod: formData.get("processingMethod"),
    isSpecialty: formData.get("isSpecialty"),
  });

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors };
  }

  const { shopId, isSpecialty, ...fields } = result.data;

  const client = await createAuthenticatedApiClient();
  const { error, response } = await client.POST("/api/admin/coffee-beans", {
    body: {
      shopId,
      ...fields,
      specialty: isSpecialty,
      images: [],
      tastes: [],
    },
  });

  if (error) {
    if (response.status === 409) {
      return { error: "このShopify Bean IDは既に登録されています。" };
    }
    return { error: "コーヒー豆の登録に失敗しました。" };
  }

  revalidatePath("/coffee-beans");
  return { success: true };
}

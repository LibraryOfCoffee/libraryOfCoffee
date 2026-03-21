"use server";

import { revalidatePath } from "next/cache";
import { z } from "zod/v4";
import { createAuthenticatedApiClient } from "@/api/client";
import {
  type CoffeeBeanFormState,
  coffeeBeanFieldsSchema,
} from "@/app/(admin)/coffee-beans/_lib/coffeeBeanFormSchema";

const editCoffeeBeanSchema = coffeeBeanFieldsSchema.extend({
  id: z.string(),
});

export type EditCoffeeBeanState = CoffeeBeanFormState;

export async function editCoffeeBeanAction(
  _prevState: EditCoffeeBeanState,
  formData: FormData,
): Promise<EditCoffeeBeanState> {
  const result = editCoffeeBeanSchema.safeParse({
    id: formData.get("id"),
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

  const { id, ...fields } = result.data;

  const client = await createAuthenticatedApiClient();

  const { data: current } = await client.GET("/api/admin/coffee-beans/{id}", {
    params: { path: { id } },
  });

  if (!current) {
    return { error: "コーヒー豆が見つかりません。" };
  }

  const { error, response } = await client.PUT("/api/admin/coffee-beans/{id}", {
    params: { path: { id } },
    body: {
      ...fields,
      specialty: fields.isSpecialty,
      images: (current.images ?? []).map((img) => ({
        type: img.type,
        imageUrl: img.imageUrl,
      })),
      tastes: (current.tastes ?? []).map((t) => ({
        tasteId: t.tasteId,
        evaluationValue: t.evaluationValue,
      })),
    },
  });

  if (error) {
    if (response.status === 409) {
      return { error: "このShopify Bean IDは既に登録されています。" };
    }
    return { error: "コーヒー豆の更新に失敗しました。" };
  }

  revalidatePath(`/coffee-beans/${id}`);
  revalidatePath("/coffee-beans");
  return { success: true };
}

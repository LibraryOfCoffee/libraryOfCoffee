"use server";

import { revalidatePath } from "next/cache";
import { z } from "zod/v4";
import { multipartRequest } from "@/api/client";
import {
  type CoffeeBeanFormState,
  coffeeBeanFieldsSchema,
} from "@/app/(admin)/coffee-beans/_lib/coffeeBeanFormSchema";
import { parseTastesFromFormData } from "@/app/(admin)/coffee-beans/_lib/parseTastes";

const editCoffeeBeanSchema = coffeeBeanFieldsSchema.extend({
  id: z.string(),
});

export type EditCoffeeBeanState = CoffeeBeanFormState;

export async function editCoffeeBeanAction(
  _prevState: EditCoffeeBeanState,
  formData: FormData,
): Promise<EditCoffeeBeanState> {
  const values = {
    shopId: (formData.get("shopId") as string) ?? "",
    shopifyBeanId: (formData.get("shopifyBeanId") as string) ?? "",
    name: (formData.get("name") as string) ?? "",
    description: (formData.get("description") as string) ?? "",
    origin: (formData.get("origin") as string) ?? "",
    farm: (formData.get("farm") as string) ?? "",
    roastLevel: (formData.get("roastLevel") as string) ?? "",
    processingMethod: (formData.get("processingMethod") as string) ?? "",
    isSpecialty: (formData.get("isSpecialty") as string) ?? "false",
    publishStatus: (formData.get("publishStatus") as string) ?? "",
  };

  const result = editCoffeeBeanSchema.safeParse({
    id: formData.get("id"),
    ...values,
  });

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors, values };
  }

  const { id, ...fields } = result.data;

  const tastes = parseTastesFromFormData(formData);

  const response = await multipartRequest(
    `/api/admin/coffee-beans/${id}`,
    "PUT",
    {
      ...fields,
      tastes,
    },
    formData,
  );

  if (!response.ok) {
    if (response.status === 409) {
      return {
        error: "このShopify Bean IDは既に登録されています。",
        values,
      };
    }
    const body = await response.json().catch(() => null);
    const message = body?.message ?? "コーヒー豆の更新に失敗しました。";
    return { error: message, values };
  }

  revalidatePath(`/coffee-beans/${id}`);
  revalidatePath("/coffee-beans");
  return { success: true };
}

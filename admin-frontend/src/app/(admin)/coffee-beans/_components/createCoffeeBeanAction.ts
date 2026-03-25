"use server";

import { revalidatePath } from "next/cache";
import { multipartRequest } from "@/api/client";
import {
  type CoffeeBeanFormState,
  coffeeBeanFieldsSchema,
} from "@/app/(admin)/coffee-beans/_lib/coffeeBeanFormSchema";

export type CreateCoffeeBeanState = CoffeeBeanFormState;

export async function createCoffeeBeanAction(
  _prevState: CreateCoffeeBeanState,
  formData: FormData,
): Promise<CreateCoffeeBeanState> {
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
  };

  const result = coffeeBeanFieldsSchema.safeParse(values);

  if (!result.success) {
    return {
      fieldErrors: result.error.flatten().fieldErrors,
      values,
    };
  }

  const { shopId: parsedShopId, ...fields } = result.data;

  const response = await multipartRequest(
    "/api/admin/coffee-beans",
    "POST",
    { shopId: parsedShopId, ...fields, tastes: [] },
    formData,
  );

  if (!response.ok) {
    if (response.status === 409) {
      return {
        error: "このShopify Bean IDは既に登録されています。",
        values,
      };
    }
    return {
      error: "コーヒー豆の登録に失敗しました。",
      values,
    };
  }

  revalidatePath("/coffee-beans");
  return { success: true };
}

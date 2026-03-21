"use server";

import { revalidatePath } from "next/cache";
import { z } from "zod/v4";
import { multipartRequest } from "@/api/client";
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
  const values = {
    shopifyBeanId: (formData.get("shopifyBeanId") as string) ?? "",
    name: (formData.get("name") as string) ?? "",
    description: (formData.get("description") as string) ?? "",
    origin: (formData.get("origin") as string) ?? "",
    farm: (formData.get("farm") as string) ?? "",
    roastLevel: (formData.get("roastLevel") as string) ?? "",
    processingMethod: (formData.get("processingMethod") as string) ?? "",
    isSpecialty: (formData.get("isSpecialty") as string) ?? "false",
  };

  const shopId = (formData.get("shopId") as string) ?? "";

  const result = createCoffeeBeanSchema.safeParse({
    shopId,
    ...values,
  });

  if (!result.success) {
    return {
      fieldErrors: result.error.flatten().fieldErrors,
      values: { ...values, shopId } as CreateCoffeeBeanState["values"],
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
        values: { ...values, shopId } as CreateCoffeeBeanState["values"],
      };
    }
    return {
      error: "コーヒー豆の登録に失敗しました。",
      values: { ...values, shopId } as CreateCoffeeBeanState["values"],
    };
  }

  revalidatePath("/coffee-beans");
  return { success: true };
}

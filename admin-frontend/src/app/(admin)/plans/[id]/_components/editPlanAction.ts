"use server";

import { revalidatePath } from "next/cache";
import { createAuthenticatedApiClient } from "@/api/client";
import { planFormSchema } from "@/app/(admin)/plans/_lib/planFormSchema";
import { isChecked } from "@/lib/formData";

export type EditPlanState = {
  success?: boolean;
  error?: string;
  fieldErrors?: Record<string, string[]>;
};

export async function editPlanAction(
  _prevState: EditPlanState,
  formData: FormData,
): Promise<EditPlanState> {
  const id = formData.get("id") as string;
  const rawData = {
    shopifyPlanId: formData.get("shopifyPlanId") as string,
    label: formData.get("label") as string,
    gramWeight: formData.get("gramWeight") as string,
    beanQuantity: formData.get("beanQuantity") as string,
    price: formData.get("price") as string,
    type: formData.get("type") as string,
    isRecommended: isChecked(formData, "isRecommended"),
  };

  const parsed = planFormSchema.safeParse(rawData);
  if (!parsed.success) {
    return {
      fieldErrors: parsed.error.flatten().fieldErrors,
    };
  }

  const client = await createAuthenticatedApiClient();
  const { error } = await client.PUT("/api/admin/plans/{id}", {
    params: { path: { id } },
    body: {
      ...parsed.data,
      type: parsed.data.type as "SUBSCRIPTION" | "SINGLE",
    },
  });

  if (error) {
    return { error: "プランの更新に失敗しました" };
  }

  revalidatePath(`/plans/${id}`);
  revalidatePath(`/plans`);
  return { success: true };
}

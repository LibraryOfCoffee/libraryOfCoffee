import { z } from "zod";
import { BEAN_QUANTITY_OPTIONS, GRAM_WEIGHT_OPTIONS } from "./planLabels";

export const planFormSchema = z.object({
  shopifyPlanId: z
    .string()
    .min(1, "ShopifyプランIDは必須です")
    .max(255, "ShopifyプランIDは255文字以内で入力してください"),
  label: z
    .string()
    .min(1, "プラン表示名は必須です")
    .max(50, "プラン表示名は50文字以内で入力してください"),
  gramWeight: z.coerce
    .number()
    .refine(
      (v) => (GRAM_WEIGHT_OPTIONS as readonly number[]).includes(v),
      "30 / 60 / 90 のいずれかを選択してください",
    ),
  beanQuantity: z.coerce
    .number()
    .refine(
      (v) => (BEAN_QUANTITY_OPTIONS as readonly number[]).includes(v),
      "3 / 4 / 5 のいずれかを選択してください",
    ),
  price: z.coerce
    .number()
    .int("価格は整数で入力してください")
    .min(0, "価格は0以上で入力してください"),
  type: z.enum(["SUBSCRIPTION", "SINGLE"], {
    message: "プラン種別を選択してください",
  }),
  isRecommended: z.boolean(),
});

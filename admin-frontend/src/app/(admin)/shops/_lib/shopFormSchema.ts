import { z } from "zod/v4";

export const shopFieldsSchema = z.object({
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

export type ShopFormValues = {
  shopifyShopId?: string;
  name?: string;
  introduction?: string;
  particular?: string;
};

export type ShopFormState = {
  success?: boolean;
  error?: string;
  fieldErrors?: {
    shopifyShopId?: string[];
    name?: string[];
    introduction?: string[];
    particular?: string[];
  };
  values?: ShopFormValues;
};

import { z } from "zod/v4";
import { PROCESSING_METHODS, ROAST_LEVELS } from "./coffeeBeanLabels";

export const coffeeBeanFieldsSchema = z.object({
  shopId: z.string().min(1, "店舗を選択してください。"),
  shopifyBeanId: z
    .string()
    .min(1, "Shopify Bean IDを入力してください。")
    .max(255, "255文字以内で入力してください。"),
  name: z
    .string()
    .min(1, "名前を入力してください。")
    .max(255, "255文字以内で入力してください。"),
  description: z
    .string()
    .min(1, "説明を入力してください。")
    .max(10000, "10,000文字以内で入力してください。"),
  origin: z
    .string()
    .min(1, "産地を入力してください。")
    .max(255, "255文字以内で入力してください。"),
  farm: z
    .string()
    .max(255, "255文字以内で入力してください。")
    .transform((v) => v || undefined)
    .optional(),
  roastLevel: z.enum(ROAST_LEVELS, {
    error: "焙煎度を選択してください。",
  }),
  processingMethod: z.enum(PROCESSING_METHODS, {
    error: "精製方法を選択してください。",
  }),
  isSpecialty: z
    .enum(["true", "false"], {
      error: "スペシャルティを選択してください。",
    })
    .transform((v) => v === "true"),
});

export type CoffeeBeanFormValues = {
  shopId?: string;
  shopifyBeanId?: string;
  name?: string;
  description?: string;
  origin?: string;
  farm?: string;
  roastLevel?: string;
  processingMethod?: string;
  isSpecialty?: string;
};

export type CoffeeBeanFormState = {
  success?: boolean;
  error?: string;
  fieldErrors?: {
    shopId?: string[];
    shopifyBeanId?: string[];
    name?: string[];
    description?: string[];
    origin?: string[];
    farm?: string[];
    roastLevel?: string[];
    processingMethod?: string[];
    isSpecialty?: string[];
  };
  values?: CoffeeBeanFormValues;
};

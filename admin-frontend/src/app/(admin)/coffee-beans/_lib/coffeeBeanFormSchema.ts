import { z } from "zod/v4";
import { ROAST_LEVELS } from "./coffeeBeanLabels";

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
  // 有効値はAdmin APIの精製方法一覧が正本。不正値はAPI側で400になる
  processingMethod: z.string().min(1, "精製方法を選択してください。"),
  isSpecialty: z
    .enum(["true", "false"], {
      error: "スペシャルティを選択してください。",
    })
    .transform((v) => v === "true"),
  publishStatus: z.enum(["DRAFT", "PUBLISHED"], {
    error: "公開状態を選択してください。",
  }),
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
  publishStatus?: string;
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
    publishStatus?: string[];
  };
  values?: CoffeeBeanFormValues;
};

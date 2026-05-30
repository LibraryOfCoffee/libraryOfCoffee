import { z } from "zod/v4";
import { PREFECTURE_OPTIONS } from "./prefecture";

const prefectureValues = PREFECTURE_OPTIONS.map((p) => p.value) as [
  string,
  ...string[],
];

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
  shopUrl: z
    .string()
    .min(1, "店舗URLを入力してください。")
    .max(2048, "2,048文字以内で入力してください。")
    .url("有効なURLを入力してください。"),
  prefecture: z.enum(prefectureValues, {
    error: "都道府県を選択してください。",
  }),
  publishStatus: z.enum(["DRAFT", "PUBLISHED"], {
    error: "公開状態を選択してください。",
  }),
});

export type ShopFormValues = {
  shopifyShopId?: string;
  name?: string;
  introduction?: string;
  particular?: string;
  shopUrl?: string;
  prefecture?: string;
  publishStatus?: string;
};

export type ShopFormState = {
  success?: boolean;
  error?: string;
  fieldErrors?: {
    shopifyShopId?: string[];
    name?: string[];
    introduction?: string[];
    particular?: string[];
    shopUrl?: string[];
    prefecture?: string[];
    publishStatus?: string[];
  };
  values?: ShopFormValues;
};

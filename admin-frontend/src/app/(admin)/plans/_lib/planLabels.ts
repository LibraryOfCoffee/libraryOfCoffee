export const PLAN_TYPE_OPTIONS = [
  { value: "SUBSCRIPTION", label: "定期便" },
  { value: "SINGLE", label: "単品" },
] as const;

export const PLAN_TYPE_LABELS: Record<string, string> = Object.fromEntries(
  PLAN_TYPE_OPTIONS.map((option) => [option.value, option.label]),
);

export const GRAM_WEIGHT_OPTIONS = [30, 60, 90] as const;

export const BEAN_QUANTITY_OPTIONS = [3, 4, 5] as const;

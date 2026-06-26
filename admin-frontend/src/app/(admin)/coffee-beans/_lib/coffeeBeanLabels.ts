export const ROAST_LEVELS = [
  "LIGHT",
  "CINNAMON",
  "MEDIUM",
  "CITY",
  "FRENCH",
] as const;
export type RoastLevel = (typeof ROAST_LEVELS)[number];

export const ROAST_LEVEL_LABELS: Record<RoastLevel, string> = {
  LIGHT: "浅煎り",
  CINNAMON: "中浅煎り",
  MEDIUM: "中煎り",
  CITY: "中深煎り",
  FRENCH: "深煎り",
};

export const PROCESSING_METHODS = [
  "FULLY_WASHED",
  "WASHED",
  "THERMAL_SHOCK_NATURAL",
  "NATURAL",
  "ANAEROBIC_NATURAL",
  "DRY_ON_TREE_NATURAL",
  "WET_HULLING",
  "HONEY",
] as const;
export type ProcessingMethod = (typeof PROCESSING_METHODS)[number];

export const PROCESSING_METHOD_LABELS: Record<ProcessingMethod, string> = {
  FULLY_WASHED: "フリーウォッシュド",
  WASHED: "ウォッシュド",
  THERMAL_SHOCK_NATURAL: "サーマルショック・ナチュラル",
  NATURAL: "ナチュラル",
  ANAEROBIC_NATURAL: "アナエロビックナチュラル",
  DRY_ON_TREE_NATURAL: "ドライオンツリーナチュラル",
  WET_HULLING: "スマトラ式",
  HONEY: "ハニー",
};

export function getRoastLevelLabel(value: string): string {
  return ROAST_LEVEL_LABELS[value as RoastLevel] ?? value;
}

export function getProcessingMethodLabel(value: string): string {
  return PROCESSING_METHOD_LABELS[value as ProcessingMethod] ?? value;
}

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

export function getRoastLevelLabel(value: string): string {
  return ROAST_LEVEL_LABELS[value as RoastLevel] ?? value;
}

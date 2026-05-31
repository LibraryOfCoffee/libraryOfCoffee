export const NAV_ITEMS = [
  { label: "ダッシュボード", path: "/", icon: "📊" },
  { label: "店舗管理", path: "/shops", icon: "🏪" },
  { label: "コーヒー豆管理", path: "/coffee-beans", icon: "☕" },
  { label: "プラン管理", path: "/plans", icon: "📦" },
] as const;

export const PATH_LABELS: Record<string, string> = Object.fromEntries([
  ["", "ダッシュボード"],
  ...NAV_ITEMS.filter((item) => item.path !== "/").map((item) => [
    item.path.slice(1),
    item.label,
  ]),
]);

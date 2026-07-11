export const PUBLISH_STATUSES = ["DRAFT", "PUBLISHED"] as const;
export type PublishStatus = (typeof PUBLISH_STATUSES)[number];

export const PUBLISH_STATUS_LABELS: Record<PublishStatus, string> = {
  DRAFT: "下書き",
  PUBLISHED: "公開",
};

/**
 * セレクトの選択肢順（デフォルトの下書きを先頭に）。
 */
export const PUBLISH_STATUS_OPTIONS: { value: PublishStatus; label: string }[] =
  [
    { value: "DRAFT", label: PUBLISH_STATUS_LABELS.DRAFT },
    { value: "PUBLISHED", label: PUBLISH_STATUS_LABELS.PUBLISHED },
  ];

export function getPublishStatusLabel(value: string): string {
  return PUBLISH_STATUS_LABELS[value as PublishStatus] ?? value;
}

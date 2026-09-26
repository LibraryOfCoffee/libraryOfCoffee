type PublishStatus = "DRAFT" | "PUBLISHED";

const PUBLISH_STATUS_LABELS: Record<PublishStatus, string> = {
  DRAFT: "下書き",
  PUBLISHED: "公開",
};

export function getPublishStatusLabel(value: string): string {
  return PUBLISH_STATUS_LABELS[value as PublishStatus] ?? value;
}

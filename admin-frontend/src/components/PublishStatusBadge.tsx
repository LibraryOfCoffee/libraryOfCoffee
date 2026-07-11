import styles from "./PublishStatusBadge.module.css";
import { getPublishStatusLabel } from "./publishStatus";

export function PublishStatusBadge({ status }: { status: string }) {
  const isPublished = status === "PUBLISHED";
  return (
    <span
      className={`${styles.badge} ${
        isPublished ? styles.published : styles.draft
      }`}
    >
      {getPublishStatusLabel(status)}
    </span>
  );
}

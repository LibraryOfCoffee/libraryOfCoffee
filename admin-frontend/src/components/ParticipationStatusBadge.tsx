import styles from "./ParticipationStatusBadge.module.css";
import { getParticipationStatusLabel } from "./participationStatus";

export function ParticipationStatusBadge({ status }: { status: string }) {
  const badgeClass =
    status === "PARTICIPATING"
      ? styles.participating
      : status === "DROPPED"
        ? styles.dropped
        : styles.beforeParticipation;
  return (
    <span className={`${styles.badge} ${badgeClass}`}>
      {getParticipationStatusLabel(status)}
    </span>
  );
}

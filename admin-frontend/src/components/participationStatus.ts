export const PARTICIPATION_STATUSES = [
  "BEFORE_PARTICIPATION",
  "PARTICIPATING",
  "DROPPED",
] as const;
export type ParticipationStatus = (typeof PARTICIPATION_STATUSES)[number];

export const PARTICIPATION_STATUS_LABELS: Record<ParticipationStatus, string> =
  {
    BEFORE_PARTICIPATION: "参画前",
    PARTICIPATING: "参画中",
    DROPPED: "参画落ち",
  };

export const PARTICIPATION_STATUS_OPTIONS: {
  value: ParticipationStatus;
  label: string;
}[] = [
  {
    value: "BEFORE_PARTICIPATION",
    label: PARTICIPATION_STATUS_LABELS.BEFORE_PARTICIPATION,
  },
  {
    value: "PARTICIPATING",
    label: PARTICIPATION_STATUS_LABELS.PARTICIPATING,
  },
];

export function getParticipationStatusLabel(value: string): string {
  return PARTICIPATION_STATUS_LABELS[value as ParticipationStatus] ?? value;
}

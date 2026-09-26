import { ROAST_ORDER } from "../../_lib/coffeeBeanApi";
import styles from "./roastTag.module.css";

/** ROAST_ORDER と同じ並び（浅煎り → 深煎り） */
const ROAST_CLASSES = [
  styles.light,
  styles.mediumLight,
  styles.medium,
  styles.mediumDark,
  styles.dark,
];

interface RoastTagProps {
  tag: string;
}

export default function RoastTag({ tag }: RoastTagProps) {
  const roastClass = ROAST_CLASSES[ROAST_ORDER.indexOf(tag)] ?? styles.medium;
  return <span className={`${styles.tag} ${roastClass}`}>{tag}</span>;
}

import Link from "next/link";
import styles from "./statusPage.module.css";

interface StatusPageProps {
  code: string;
  label: string;
  message: string;
}

export default function StatusPage({ code, label, message }: StatusPageProps) {
  return (
    <main className={styles.page}>
      <div className={styles.inner}>
        <h1 className={styles.code}>{code}</h1>
        <p className={styles.label}>{label}</p>
        <p className={styles.message}>{message}</p>
        <Link href="/lp" className={styles.link}>
          トップに戻る
        </Link>
      </div>
    </main>
  );
}

"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { PATH_LABELS } from "@/lib/navigation";
import styles from "../layout.module.css";

export function Breadcrumb() {
  const pathname = usePathname();
  const segments = pathname.split("/").filter(Boolean);

  if (segments.length === 0) {
    return (
      <div className={styles.breadcrumb}>
        <span className={styles.breadcrumbCurrent}>ダッシュボード</span>
      </div>
    );
  }

  return (
    <div className={styles.breadcrumb}>
      <Link href="/" className={styles.breadcrumbLink}>
        ダッシュボード
      </Link>
      {segments.map((segment, index) => {
        const path = `/${segments.slice(0, index + 1).join("/")}`;
        const label = PATH_LABELS[segment] ?? segment;
        const isLast = index === segments.length - 1;
        return (
          <span key={path} style={{ display: "contents" }}>
            <span className={styles.breadcrumbSeparator}>&gt;</span>
            {isLast ? (
              <span className={styles.breadcrumbCurrent}>{label}</span>
            ) : (
              <Link href={path} className={styles.breadcrumbLink}>
                {label}
              </Link>
            )}
          </span>
        );
      })}
    </div>
  );
}

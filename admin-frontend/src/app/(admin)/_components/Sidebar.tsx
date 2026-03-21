"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { NAV_ITEMS } from "@/lib/navigation";
import styles from "../layout.module.css";

export function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className={styles.sidebar}>
      <div className={styles.logo}>
        <span className={styles.logoIcon}>☕</span>
        <span className={styles.logoText}>Library of Coffee</span>
      </div>
      <nav className={styles.nav}>
        {NAV_ITEMS.map((item) => {
          const isActive =
            item.path === "/"
              ? pathname === "/"
              : pathname.startsWith(item.path);
          return (
            <Link
              key={item.path}
              href={item.path}
              className={`${styles.navLink} ${isActive ? styles.navLinkActive : ""}`}
            >
              <span className={styles.navIcon}>{item.icon}</span>
              {item.label}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}

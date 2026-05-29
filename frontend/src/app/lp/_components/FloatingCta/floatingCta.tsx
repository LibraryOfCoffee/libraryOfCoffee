"use client";

import { useEffect, useState } from "react";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./floatingCta.module.css";

export default function FloatingCta() {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const onScroll = () => {
      const scrolledPastTop = window.scrollY > 200;
      const nearBottom =
        window.scrollY + window.innerHeight >=
        document.documentElement.scrollHeight - 80;
      setVisible(scrolledPastTop && !nearBottom);
    };
    window.addEventListener("scroll", onScroll, { passive: true });
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  return (
    <div className={`${styles.wrapper} ${visible ? styles.visible : ""}`}>
      <div className={styles.inner}>
        <LinkWithLoading href={getPlanPagePath()} className={styles.cta}>
          <span className={styles.label}>豆を選ぶ</span>
          <span className={styles.arrow}>→</span>
        </LinkWithLoading>
      </div>
    </div>
  );
}

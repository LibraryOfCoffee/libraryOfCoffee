import Link from "next/link";
import type { CoffeeBeanListItem } from "@/api/coffee-beans";
import type { PagedResponse } from "@/api/types";
import styles from "@/components/list-page.module.css";
import { Pagination } from "@/components/Pagination";

const ROAST_LEVEL_LABELS: Record<string, string> = {
  LIGHT: "ライト",
  MEDIUM: "ミディアム",
  CITY: "シティ",
  FRENCH: "フレンチ",
};

const PROCESSING_METHOD_LABELS: Record<string, string> = {
  FULLY_WASHED: "フリーウォッシュド",
  WASHED: "ウォッシュド",
  THERMAL_SHOCK_NATURAL: "サーマルショック・ナチュラル",
  NATURAL: "ナチュラル",
  WET_HULLING: "ウェットハリング",
  HONEY: "ハニー",
};

export function CoffeeBeanListTable({
  coffeeBeans,
  currentPage,
}: {
  coffeeBeans: PagedResponse<CoffeeBeanListItem>;
  currentPage: number;
}) {
  const totalPages = Math.ceil(coffeeBeans.totalCount / coffeeBeans.size);
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.titleGroup}>
          <h1 className={styles.title}>コーヒー豆一覧</h1>
          <p className={styles.count}>{coffeeBeans.totalCount}件のコーヒー豆</p>
        </div>
        <Link href="/coffee-beans/new" className={styles.createButton}>
          + 新規作成
        </Link>
      </div>

      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>名前</th>
              <th>産地</th>
              <th>農園</th>
              <th>焙煎度</th>
              <th>精製方法</th>
              <th>スペシャルティ</th>
            </tr>
          </thead>
          <tbody>
            {coffeeBeans.items.map((bean) => (
              <tr key={bean.id} className={styles.clickableRow}>
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    {bean.name}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    {bean.origin}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    {bean.farm ?? ""}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    {ROAST_LEVEL_LABELS[bean.roastLevel] ?? bean.roastLevel}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    {PROCESSING_METHOD_LABELS[bean.processingMethod] ??
                      bean.processingMethod}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    {bean.isSpecialty ? "あり" : "なし"}
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        basePath="/coffee-beans"
      />
    </div>
  );
}

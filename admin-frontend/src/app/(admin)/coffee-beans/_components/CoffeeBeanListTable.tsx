import Link from "next/link";
import type { CoffeeBeanListResponse } from "@/api/coffee-beans";
import styles from "@/components/list-page.module.css";
import { Pagination } from "@/components/Pagination";
import { PublishStatusBadge } from "@/components/PublishStatusBadge";
import { getRoastLevelLabel } from "../_lib/coffeeBeanLabels";
import { CreateCoffeeBeanButton } from "./CreateCoffeeBeanButton";

export function CoffeeBeanListTable({
  coffeeBeans,
  currentPage,
}: {
  coffeeBeans: CoffeeBeanListResponse;
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
        <CreateCoffeeBeanButton />
      </div>

      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>名前</th>
              <th>店舗</th>
              <th>産地</th>
              <th>農園</th>
              <th>焙煎度</th>
              <th>精製方法</th>
              <th>スペシャルティ</th>
              <th>公開状態</th>
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
                    {bean.shopName}
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
                    {getRoastLevelLabel(bean.roastLevel)}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    {bean.processingMethodName}
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
                <td>
                  <Link
                    href={`/coffee-beans/${bean.id}`}
                    className={styles.rowLink}
                  >
                    <PublishStatusBadge status={bean.publishStatus} />
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

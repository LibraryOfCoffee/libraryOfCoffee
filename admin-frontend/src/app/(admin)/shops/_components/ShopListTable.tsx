import Link from "next/link";
import type { ShopListResponse } from "@/api/shops";
import styles from "@/components/list-page.module.css";
import { Pagination } from "@/components/Pagination";
import { PublishStatusBadge } from "@/components/PublishStatusBadge";
import { CreateShopButton } from "./CreateShopButton";

export function ShopListTable({
  shops,
  currentPage,
}: {
  shops: ShopListResponse;
  currentPage: number;
}) {
  const totalPages = Math.ceil(shops.totalCount / shops.size);
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.titleGroup}>
          <h1 className={styles.title}>店舗一覧</h1>
          <p className={styles.count}>{shops.totalCount}件の店舗</p>
        </div>
        <CreateShopButton />
      </div>

      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>店舗名</th>
              <th>Shopify Shop ID</th>
              <th>紹介文</th>
              <th>こだわり</th>
              <th>店舗URL</th>
              <th>公開状態</th>
            </tr>
          </thead>
          <tbody>
            {shops.items.map((shop) => (
              <tr key={shop.id} className={styles.clickableRow}>
                <td>
                  <Link href={`/shops/${shop.id}`} className={styles.rowLink}>
                    {shop.name}
                  </Link>
                </td>
                <td>
                  <Link href={`/shops/${shop.id}`} className={styles.rowLink}>
                    {shop.shopifyShopId}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/shops/${shop.id}`}
                    className={`${styles.rowLink} ${styles.truncatedCell}`}
                  >
                    {shop.introduction ?? ""}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/shops/${shop.id}`}
                    className={`${styles.rowLink} ${styles.truncatedCell}`}
                  >
                    {shop.particular ?? ""}
                  </Link>
                </td>
                <td>
                  <Link
                    href={`/shops/${shop.id}`}
                    className={`${styles.rowLink} ${styles.truncatedCell}`}
                  >
                    {shop.shopUrl}
                  </Link>
                </td>
                <td>
                  <Link href={`/shops/${shop.id}`} className={styles.rowLink}>
                    <PublishStatusBadge status={shop.publishStatus} />
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
        basePath="/shops"
      />
    </div>
  );
}

import Link from "next/link";
import type { PagedResponse, ShopListItem } from "@/api/shops";
import styles from "../page.module.css";

export function ShopListTable({
  shops,
  currentPage,
}: {
  shops: PagedResponse<ShopListItem>;
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
        <Link href="/shops/new" className={styles.createButton}>
          + 新規作成
        </Link>
      </div>

      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>店舗名</th>
              <th>Shopify Shop ID</th>
              <th>紹介文</th>
              <th>こだわり</th>
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
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className={styles.pagination}>
          {currentPage > 0 && (
            <Link
              href={`/shops?page=${currentPage - 1}`}
              className={styles.pageLink}
            >
              &lt; 前へ
            </Link>
          )}
          {(() => {
            const maxVisible = 7;
            const half = Math.floor(maxVisible / 2);
            const start = Math.max(
              0,
              Math.min(currentPage - half, totalPages - maxVisible),
            );
            const end = Math.min(totalPages, start + maxVisible);
            return Array.from({ length: end - start }, (_, i) => {
              const page = start + i;
              return (
                <Link
                  key={`page-${page}`}
                  href={`/shops?page=${page}`}
                  className={
                    page === currentPage
                      ? `${styles.pageLink} ${styles.pageLinkActive}`
                      : styles.pageLink
                  }
                >
                  {page + 1}
                </Link>
              );
            });
          })()}
          {currentPage < totalPages - 1 && (
            <Link
              href={`/shops?page=${currentPage + 1}`}
              className={styles.pageLink}
            >
              次へ &gt;
            </Link>
          )}
        </div>
      )}
    </div>
  );
}

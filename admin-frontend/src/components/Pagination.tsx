import Link from "next/link";
import styles from "./Pagination.module.css";

export function Pagination({
  currentPage,
  totalPages,
  basePath,
  query,
}: {
  currentPage: number;
  totalPages: number;
  basePath: string;
  query?: Record<string, string | undefined>;
}) {
  if (totalPages <= 1) return null;

  const maxVisible = 7;
  const half = Math.floor(maxVisible / 2);
  const start = Math.max(
    0,
    Math.min(currentPage - half, totalPages - maxVisible),
  );
  const end = Math.min(totalPages, start + maxVisible);

  // 全ページリンクで共通のクエリ部分は一度だけ組み立てる。
  const extraQuery = Object.entries(query ?? {})
    .filter(([, value]) => value)
    .map(([key, value]) => `&${key}=${encodeURIComponent(value as string)}`)
    .join("");
  const hrefForPage = (page: number) => `${basePath}?page=${page}${extraQuery}`;

  return (
    <div className={styles.pagination}>
      {currentPage > 0 && (
        <Link href={hrefForPage(currentPage - 1)} className={styles.pageLink}>
          &lt; 前へ
        </Link>
      )}
      {Array.from({ length: end - start }, (_, i) => {
        const page = start + i;
        return (
          <Link
            key={`page-${page}`}
            href={hrefForPage(page)}
            className={
              page === currentPage
                ? `${styles.pageLink} ${styles.pageLinkActive}`
                : styles.pageLink
            }
          >
            {page + 1}
          </Link>
        );
      })}
      {currentPage < totalPages - 1 && (
        <Link href={hrefForPage(currentPage + 1)} className={styles.pageLink}>
          次へ &gt;
        </Link>
      )}
    </div>
  );
}

import Image from "next/image";
import Link from "next/link";
import type { ShopDetail as ShopDetailType } from "@/api/shops";
import { ShopActions } from "./ShopActions";
import styles from "./ShopDetail.module.css";

export function ShopDetail({ shop }: { shop: ShopDetailType }) {
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1 className={styles.title}>{shop.name}</h1>
          <Link href="/shops" className={styles.backLink}>
            一覧に戻る
          </Link>
        </div>
        <ShopActions shop={shop} />
      </div>

      <div className={styles.content}>
        <dl className={styles.fieldList}>
          <div className={styles.field}>
            <dt className={styles.fieldLabel}>Shopify Shop ID</dt>
            <dd className={styles.fieldValue}>{shop.shopifyShopId}</dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>紹介文</dt>
            <dd className={styles.fieldValue}>
              {shop.introduction ?? (
                <span className={styles.empty}>未設定</span>
              )}
            </dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>こだわり</dt>
            <dd className={styles.fieldValue}>
              {shop.particular ?? <span className={styles.empty}>未設定</span>}
            </dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>店舗URL</dt>
            <dd className={styles.fieldValue}>
              <a href={shop.shopUrl} target="_blank" rel="noopener noreferrer">
                {shop.shopUrl}
              </a>
            </dd>
          </div>
        </dl>

        {shop.images.length > 0 && (
          <div className={styles.imagesSection}>
            <h2 className={styles.sectionTitle}>画像</h2>
            <div className={styles.imageGrid}>
              {shop.images.map((image) => (
                <div key={image.id} className={styles.imageCard}>
                  <Image
                    src={image.imageUrl}
                    alt={`${shop.name}の画像`}
                    width={400}
                    height={300}
                    className={styles.image}
                    unoptimized
                  />
                  <span className={styles.imageType}>{image.type}</span>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

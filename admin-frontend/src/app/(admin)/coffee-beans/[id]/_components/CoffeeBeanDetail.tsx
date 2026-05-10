import Image from "next/image";
import Link from "next/link";
import type { CoffeeBeanDetail as CoffeeBeanDetailType } from "@/api/coffee-beans";
import {
  getProcessingMethodLabel,
  getRoastLevelLabel,
} from "../../_lib/coffeeBeanLabels";
import { CoffeeBeanActions } from "./CoffeeBeanActions";
import styles from "./CoffeeBeanDetail.module.css";

export function CoffeeBeanDetail({
  coffeeBean,
  initialShops,
}: {
  coffeeBean: CoffeeBeanDetailType;
  initialShops: { id: string; name: string }[];
}) {
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1 className={styles.title}>{coffeeBean.name}</h1>
          <Link href="/coffee-beans" className={styles.backLink}>
            一覧に戻る
          </Link>
        </div>
        <CoffeeBeanActions
          coffeeBean={coffeeBean}
          initialShops={initialShops}
        />
      </div>

      <div className={styles.content}>
        <dl className={styles.fieldList}>
          <div className={styles.field}>
            <dt className={styles.fieldLabel}>Shopify Bean ID</dt>
            <dd className={styles.fieldValue}>{coffeeBean.shopifyBeanId}</dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>説明</dt>
            <dd className={styles.fieldValue}>{coffeeBean.description}</dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>産地</dt>
            <dd className={styles.fieldValue}>{coffeeBean.origin}</dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>農園</dt>
            <dd className={styles.fieldValue}>
              {coffeeBean.farm ?? <span className={styles.empty}>未設定</span>}
            </dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>焙煎度</dt>
            <dd className={styles.fieldValue}>
              {getRoastLevelLabel(coffeeBean.roastLevel)}
            </dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>精製方法</dt>
            <dd className={styles.fieldValue}>
              {getProcessingMethodLabel(coffeeBean.processingMethod)}
            </dd>
          </div>

          <div className={styles.field}>
            <dt className={styles.fieldLabel}>スペシャルティ</dt>
            <dd className={styles.fieldValue}>
              {coffeeBean.isSpecialty ? "あり" : "なし"}
            </dd>
          </div>
        </dl>

        {coffeeBean.tastes.length > 0 && (
          <div className={styles.tastesSection}>
            <h2 className={styles.sectionTitle}>テイスト評価</h2>
            <div className={styles.tasteList}>
              {coffeeBean.tastes.map((taste) => (
                <div key={taste.id} className={styles.tasteItem}>
                  <span className={styles.tasteName}>{taste.tasteName}</span>
                  <div className={styles.tasteBar}>
                    <div
                      className={styles.tasteFill}
                      style={{ width: `${(taste.evaluationValue / 5) * 100}%` }}
                    />
                  </div>
                  <span className={styles.tasteValue}>
                    {taste.evaluationValue}
                  </span>
                </div>
              ))}
            </div>
          </div>
        )}

        {coffeeBean.images.length > 0 && (
          <div className={styles.imagesSection}>
            <h2 className={styles.sectionTitle}>画像</h2>
            <div className={styles.imageGrid}>
              {coffeeBean.images.map((image) => (
                <div key={image.id} className={styles.imageCard}>
                  <Image
                    src={image.imageUrl}
                    alt={`${coffeeBean.name}の画像`}
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

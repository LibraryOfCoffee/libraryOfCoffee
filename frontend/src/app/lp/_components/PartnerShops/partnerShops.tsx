import Image from "next/image";
import type { Shop } from "../../_lib/shopApi";
import styles from "./partnerShops.module.css";

interface PartnerShopsProps {
  shops: Shop[];
}

export default function PartnerShops({ shops }: PartnerShopsProps) {
  return (
    <section className={styles.section}>
      <p className={styles.eyebrow}>— ROASTERS</p>
      <h2 className={styles.headline}>参加店舗</h2>
      <p className={styles.subtext}>様々な自家焙煎店舗が参加しています</p>
      <div className={styles.grid}>
        {shops.map((shop) => (
          <a
            key={shop.id}
            href={shop.shopUrl}
            target="_blank"
            rel="noopener noreferrer"
            className={styles.item}
          >
            <Image
              src={shop.logoImageUrl}
              alt={shop.name}
              width={70}
              height={70}
              unoptimized
              className={styles.logo}
            />
          </a>
        ))}
      </div>
    </section>
  );
}

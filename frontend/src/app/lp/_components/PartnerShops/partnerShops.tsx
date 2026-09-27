import Image from "next/image";
import type { Shop } from "../../_lib/shopApi";
import SectionHeading from "../SectionHeading/sectionHeading";
import styles from "./partnerShops.module.css";

const COLUMNS = 3;

interface PartnerShopsProps {
  shops: Shop[];
}

export default function PartnerShops({ shops }: PartnerShopsProps) {
  const fillerCount = (COLUMNS - (shops.length % COLUMNS)) % COLUMNS;

  return (
    <section className={styles.section}>
      <SectionHeading num="08" label="Roasters" title="参加店舗" />
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
              width={62}
              height={62}
              unoptimized
              className={styles.logo}
            />
          </a>
        ))}
        {Array.from({ length: fillerCount }, (_, i) => (
          <div key={i} className={styles.filler} aria-hidden="true" />
        ))}
      </div>
    </section>
  );
}

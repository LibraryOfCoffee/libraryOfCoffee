import Image from "next/image";
import styles from "./partnerShops.module.css";

const shops = [
  {
    name: "NORTHNODE COFFEE",
    logoUrl: "/shopLogos/northNodeCoffee.jpg",
    websiteUrl: "https://northnode.base.shop/",
  },
  {
    name: "ゆるり珈琲",
    logoUrl: "/shopLogos/yururi.png",
    websiteUrl: "https://yururicoffee.shopselect.net/",
  },
  {
    name: "MOSHIMOSHI COFFEE",
    logoUrl: "/shopLogos/moshimoshiCoffee.png",
    websiteUrl: "https://moshimoshi.buyshop.jp/",
  },
  {
    name: "LUSH-COFFEE",
    logoUrl: "/shopLogos/lushCoffee.png",
    websiteUrl: "https://lush-coffee.com/",
  },
  {
    name: "Tama Coffee Roaster",
    logoUrl: "/shopLogos/TamaCoffeeRoaster.png",
    websiteUrl: "https://www.tamacoffeeroaster.com/",
  },
  {
    name: "maruca coffee",
    logoUrl: "/shopLogos/marucaCoffee.png",
    websiteUrl: "https://marucacoffee.com/",
  },
  {
    name: "+ninth coffee",
    logoUrl: "/shopLogos/addNinthCoffee.png",
    websiteUrl: "https://www.addninthcoffee.com/",
  },
  {
    name: "Black Sloth Coffee",
    logoUrl: "/shopLogos/blackSlothCoffee.png",
    websiteUrl: "https://bscnet.base.shop/",
  },
  {
    name: "FIVE COFFEE STAND&ROASTERY",
    logoUrl: "/shopLogos/fiveCoffeeStandAndRoastery.png",
    websiteUrl: "https://www.fivecoffee.jp/",
  },
];

export default function PartnerShops() {
  return (
    <section className={styles.section}>
      <p className={styles.eyebrow}>— ROASTERS</p>
      <h2 className={styles.headline}>参加店舗</h2>
      <p className={styles.subtext}>様々な自家焙煎店舗が参加しています</p>
      <div className={styles.grid}>
        {shops.map((shop) => (
          <a
            key={shop.name}
            href={shop.websiteUrl}
            target="_blank"
            rel="noopener noreferrer"
            className={styles.item}
          >
            <Image
              src={shop.logoUrl}
              alt={shop.name}
              width={70}
              height={70}
              className={styles.logo}
            />
          </a>
        ))}
      </div>
    </section>
  );
}

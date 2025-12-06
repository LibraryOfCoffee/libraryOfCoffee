"use client"

import Image from "next/image";
import './shopSection.css';

interface Shop {
  name: string;
  logoUrl: string;
  websiteUrl: string;
}

const shops: Shop[] = [
  { name: "NORTHNODE COFFEE", logoUrl: "/shopLogos/northNodeCoffee.jpg", websiteUrl: "https://northnode.base.shop/" },
  { name: "ゆるり珈琲", logoUrl: "/shopLogos/yururi.png", websiteUrl: "https://yururicoffee.shopselect.net/" },
  { name: "MOSHIMOSHI COFFEE", logoUrl: "/shopLogos/moshimoshiCoffee.png", websiteUrl: "https://moshimoshi.buyshop.jp/" },
  { name: "LUSH COFFEE", logoUrl: "/shopLogos/lushCoffee.png", websiteUrl: "https://lush-coffee.com/" },
  { name: "Tama Coffee Roaster", logoUrl: "/shopLogos/TamaCoffeeRoaster.png", websiteUrl: "https://www.tamacoffeeroaster.com/" },
  { name: "maruca coffee", logoUrl: "/shopLogos/marucaCoffee.png", websiteUrl: "https://marucacoffee.com/" },
];

export default function ShopSection() {
  return (
    <div className="shop-list-container">

      <div className="shop-list-header">
        <div className="shop-list-header-icon">
          {/* TODO: 仮置き */}
          <Image
            src="/logo.svg"
            alt="logo"
            height={40}
            width={80}
          />
        </div>
        <h3 className="shop-list-title">
          様々な珈琲店舗との出会い
        </h3>
      </div>

      <div className="shop-list-grid">
        {shops.map((shop, index) => (
          <a
            key={index}
            href={shop.websiteUrl}
            target="_blank"
            rel="noopener noreferrer"
            className="shop-item"
          >
            <div className="shop-logo-wrapper">
              <Image
                src={shop.logoUrl}
                alt={shop.name}
                width={70}
                height={70}
                className="shop-logo"
              />
            </div>
          </a>
        ))}
      </div>
      <div className="shop-coming-soon-container">
        <div className="shop-coming-soon">
          <div className="shop-coming-soon-line"></div>
          <span className="shop-coming-soon-text">Coming Soon</span>
          <div className="shop-coming-soon-line"></div>
        </div>
        <p className="shop-coming-soon-subtitle">新しい店舗が続々追加予定</p>
      </div>
    </div>
  );
}

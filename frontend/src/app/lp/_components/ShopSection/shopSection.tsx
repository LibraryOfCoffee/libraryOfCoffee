"use client"

import Image from "next/image";
import './shopSection.css';

interface Shop {
  name: string;
  logoUrl: string;
  websiteUrl: string;
}

const shops: Shop[] = [
  { name: "ROASTED COFFEE LABORATORY", logoUrl: "/shopLogos/shop.png", websiteUrl: "https://example.com/roasted" },
  { name: "19 COFFEE ROASTERS", logoUrl: "/shopLogos/shop.png", websiteUrl: "https://example.com/19coffee" },
  { name: "COFFEE", logoUrl: "/shopLogos/shop.png", websiteUrl: "https://example.com/kawasaki" },
  { name: "Arabica", logoUrl: "/shopLogos/shop.png", websiteUrl: "https://example.com/arabica" },
  { name: "A COFFEE", logoUrl: "/shopLogos/shop.png", websiteUrl: "https://example.com/acoffee" },
  { name: "THE COFFEE FIGHTERS", logoUrl: "/shopLogos/shop.png", websiteUrl: "https://example.com/fighters" },
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
          様々な<br />珈琲店舗との出会い。
        </h3>
        <p className="shop-list-description">
          珈琲を楽しむ新しい形。<br />XXXXXXXX珈琲を。
        </p>

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
      <p className="shop-comming-soon">Comming Soon...</p>
    </div>
  );
}

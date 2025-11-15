'use client'
import Image from "next/image";
import './header.css';

export default function Header() {


  return (
    <header className="lp-header">
      <nav className="lp-nav">
        <div className="lp-nav-content">
          <div>
            <a className="lp-logo" href={"/lp"}>
              {/* FIXME 仮置き */}
              <Image
                src="/logo.svg"
                alt="logo"
                height={40}
                width={80}
              />
            </a>
          </div>
          <div className="lp-publish-link">
            <a href='https://zcgqx8-tr.myshopify.com/' >
              <p>豆を購入する</p>
            </a>
          </div>
        </div>
      </nav>
    </header>
  );
}

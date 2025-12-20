"use client";
import Image from "next/image";
import "./header.css";
import moveToShopify from "../../_lib/PurchaseLinkUtil/purchaseLinkUtil";

export default function Header() {
  return (
    <header className="lp-header">
      <nav className="lp-nav">
        <div className="lp-nav-content">
          <div>
            <a className="lp-logo" href={"/lp"}>
              <Image src="/logo.svg" alt="logo" height={40} width={80} />
            </a>
          </div>
          <div className="lp-publish-link">
            <div className="lp-publish-link-login">
              <a onClick={() => moveToShopify()}>
                <p>ログイン</p>
              </a>
            </div>
            <div className="lp-publish-link-register">
              <a onClick={() => moveToShopify()}>
                <p>事前登録する</p>
              </a>
            </div>
          </div>
        </div>
      </nav>
    </header>
  );
}

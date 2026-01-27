"use client";
import Image from "next/image";
import "./header.css";
import { moveToLoginPage, moveToCoffeeBeanListPage} from "../../_lib/PurchaseLinkUtil/purchaseLinkUtil";

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
              <button type="button" onClick={() => moveToLoginPage()}>
                ログイン
              </button>
            </div>
            <div className="lp-publish-link-register">
              <button type="button" onClick={() => moveToCoffeeBeanListPage()}>
                今すぐ始める
              </button>
            </div>
          </div>
        </div>
      </nav>
    </header>
  );
}

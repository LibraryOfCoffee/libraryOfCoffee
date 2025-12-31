"use client";
import Image from "next/image";
import "./footer.css";

export default function Footer() {
  return (
    <footer className="lp-footer">
      <div className="lp-footer-container">
        <div className="lp-footer-top">
          <div className="lp-footer-brand">
            <Image
              src="/logo_white.svg"
              alt="豆図書ロゴ"
              height={32}
              width={64}
            />
            <p className="lp-footer-tagline">色々な珈琲と出会える豆図書</p>
          </div>

          <nav className="lp-footer-nav">
            <a href="#features">豆図書のメリット</a>
            <a href="#testimonials">利用者の声</a>
            <a href="#flow">ご利用の流れ</a>
            <a href="#pricing">料金プラン</a>
          </nav>
        </div>

        <div className="lp-footer-divider"></div>

        <div className="lp-footer-bottom">
          <p className="lp-footer-contact">
            お問い合わせ：
            <a
              href="mailto:inquiry@m.mametosho.com"
              className="lp-footer-email"
            >
              inquiry@m.mametosho.com
            </a>
          </p>
          <p className="lp-footer-copyright">
            &copy; 2025 豆図書. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
}

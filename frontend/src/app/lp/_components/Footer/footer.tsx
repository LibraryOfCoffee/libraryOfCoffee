import Image from "next/image";
import styles from "./footer.module.css";

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.inner}>
        <div className={styles.top}>
          <div className={styles.brand}>
            <Image
              src="/logo.svg"
              alt="豆図書"
              width={80}
              height={26}
              className={styles.logo}
            />
            <p className={styles.tagline}>色々な珈琲と出会える豆図書</p>
          </div>
          <nav className={styles.links}>
            <a href="#features" className={styles.link}>
              選ばれる理由
            </a>
            <a href="#how-it-works" className={styles.link}>
              ご利用の流れ
            </a>
            <a href="#beans" className={styles.link}>
              今月のおすすめ豆
            </a>
            <a href="#pricing" className={styles.link}>
              料金プラン
            </a>
            <a href="#testimonials" className={styles.link}>
              お客様の声
            </a>
          </nav>
        </div>
        <hr className={styles.divider} />
        <div className={styles.bottom}>
          <p className={styles.contact}>
            お問い合わせ：
            <a href="mailto:inquiry@m.mametosho.com" className={styles.email}>
              inquiry@m.mametosho.com
            </a>
          </p>
          <p className={styles.copy}>© 2025 豆図書. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}

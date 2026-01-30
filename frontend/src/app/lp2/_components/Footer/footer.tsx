import styles from "./footer.module.css";

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.inner}>
        <span className={styles.logo}>豆図書</span>
        <nav className={styles.links}>
          <a href="/company" className={styles.link}>
            会社概要
          </a>
          <a href="/terms" className={styles.link}>
            利用規約
          </a>
          <a href="/contact" className={styles.link}>
            お問い合わせ
          </a>
        </nav>
        <p className={styles.copy}>© 2025 豆図書 All Rights Reserved.</p>
      </div>
    </footer>
  );
}

import Image from "next/image";
import { COFFEE_BEAN_LIST_URL, LOGIN_URL } from "../../_lib/purchaseLinkUtil";
import styles from "./header.module.css";

export default function Header() {
  return (
    <header className={styles.header}>
      <div className={styles.inner}>
        <Image
          src="/logo.svg"
          alt="豆図書"
          width={80}
          height={26}
          className={styles.logo}
        />
        <div className={styles.actions}>
          <a href={LOGIN_URL} className={styles.login}>
            ログイン
          </a>
          <a href={COFFEE_BEAN_LIST_URL} className={styles.cta}>
            今すぐ始める
          </a>
        </div>
      </div>
    </header>
  );
}

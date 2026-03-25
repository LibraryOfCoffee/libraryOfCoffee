import { Breadcrumb } from "./_components/Breadcrumb";
import { Sidebar } from "./_components/Sidebar";
import styles from "./layout.module.css";

export default function AdminLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className={styles.container}>
      <Sidebar />
      <div className={styles.main}>
        <header className={styles.header}>
          <Breadcrumb />
        </header>
        <main className={styles.content}>{children}</main>
      </div>
    </div>
  );
}

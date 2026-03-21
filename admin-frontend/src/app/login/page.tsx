"use client";

import { useActionState, useId } from "react";
import { type LoginState, loginAction } from "./actions";
import styles from "./page.module.css";

const initialState: LoginState = {};

export default function LoginPage() {
  const [state, formAction, isPending] = useActionState(
    loginAction,
    initialState,
  );
  const emailId = useId();
  const passwordId = useId();

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <div className={styles.logo}>
          <span className={styles.logoIcon}>☕</span>
          <h1 className={styles.title}>Library of Coffee</h1>
          <p className={styles.subtitle}>管理画面</p>
        </div>

        <form action={formAction} className={styles.form}>
          {state.error && <div className={styles.error}>{state.error}</div>}

          <div className={styles.field}>
            <label htmlFor={emailId} className={styles.label}>
              メールアドレス
            </label>
            <input
              id={emailId}
              name="email"
              type="email"
              autoComplete="email"
              className={styles.input}
              placeholder="admin@example.com"
            />
            {state.fieldErrors?.email?.map((msg) => (
              <span key={msg} className={styles.fieldError}>
                {msg}
              </span>
            ))}
          </div>

          <div className={styles.field}>
            <label htmlFor={passwordId} className={styles.label}>
              パスワード
            </label>
            <input
              id={passwordId}
              name="password"
              type="password"
              autoComplete="current-password"
              className={styles.input}
              placeholder="••••••••"
            />
            {state.fieldErrors?.password?.map((msg) => (
              <span key={msg} className={styles.fieldError}>
                {msg}
              </span>
            ))}
          </div>

          <button type="submit" disabled={isPending} className={styles.button}>
            {isPending ? "ログイン中..." : "ログイン"}
          </button>
        </form>
      </div>
    </div>
  );
}

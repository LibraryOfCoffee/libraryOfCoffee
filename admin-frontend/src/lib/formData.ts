/**
 * チェックボックス（トグル含む）の送信値を boolean に変換する。
 *
 * HTMLのチェックボックスは ON のとき "on" を送信し、OFF のときキー自体を
 * 送信しない。この仕様依存を各 server action に散らさず一箇所に集約する。
 */
export function isChecked(formData: FormData, name: string): boolean {
  return formData.get(name) === "on";
}

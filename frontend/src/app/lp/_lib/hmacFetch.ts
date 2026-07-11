import crypto from "node:crypto";

const CLIENT_ID = "ssr-frontend";

/**
 * CS API へHMAC-SHA256署名付きでリクエストするServer Component専用のfetchラッパー。
 *
 * node:crypto を使うためClient Bundleには含められない（= 秘密鍵はブラウザに露出しない）。
 * 署名対象文字列はバックエンドの HmacSignatureFilter と一致させる:
 *   {METHOD}\n{パス+クエリ}\n{X-Timestamp}\n{SHA256_HEX(body)}
 *
 * @param path baseUrl以降のパス（クエリ込み。例 "/api/shops?page=0&size=100"）
 */
export async function hmacFetch(
  path: string,
  init?: RequestInit,
): Promise<Response> {
  const baseUrl = process.env.CS_API_BASE_URL ?? "http://localhost:8080";
  const secret = process.env.CS_API_HMAC_KEY_SSR ?? "";

  const method = (init?.method ?? "GET").toUpperCase();
  const timestamp = Math.floor(Date.now() / 1000).toString();
  const body = typeof init?.body === "string" ? init.body : "";
  const bodyHash = crypto.createHash("sha256").update(body).digest("hex");

  const stringToSign = [method, path, timestamp, bodyHash].join("\n");
  const signature = crypto
    .createHmac("sha256", secret)
    .update(stringToSign)
    .digest("hex");

  const headers = new Headers(init?.headers);
  headers.set("X-Client-Id", CLIENT_ID);
  headers.set("X-Timestamp", timestamp);
  headers.set("X-Signature", signature);

  return fetch(`${baseUrl}${path}`, { ...init, headers });
}

import { ImageResponse } from "next/og";

export const alt = "豆図書 - 色々な珈琲と出会える定額サブスク";
export const size = { width: 1200, height: 630 };
export const contentType = "image/png";

export default function OgImage() {
  return new ImageResponse(
    <div
      style={{
        width: "100%",
        height: "100%",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        background:
          "linear-gradient(135deg, #3E2723 0%, #5D4037 50%, #795548 100%)",
        color: "#FFFFFF",
        fontFamily: "sans-serif",
      }}
    >
      <div
        style={{
          fontSize: 80,
          fontWeight: 700,
          marginBottom: 24,
          display: "flex",
        }}
      >
        豆図書
      </div>
      <div
        style={{
          fontSize: 36,
          opacity: 0.9,
          marginBottom: 40,
          display: "flex",
        }}
      >
        色々な珈琲と出会える定額サブスク
      </div>
      <div
        style={{
          fontSize: 28,
          opacity: 0.8,
          display: "flex",
          alignItems: "baseline",
          gap: 8,
        }}
      >
        <span style={{ fontSize: 48, fontWeight: 700 }}>¥1,500</span>
        <span>/月（税込・送料無料）</span>
      </div>
    </div>,
    { ...size },
  );
}

import type { MetadataRoute } from "next";

export default function robots(): MetadataRoute.Robots {
  return {
    rules: [
      {
        userAgent: "*",
        allow: ["/lp", "/favicon.ico"],
        disallow: "/api/",
      },
    ],
    sitemap: "https://mametosho.com/sitemap.xml",
  };
}

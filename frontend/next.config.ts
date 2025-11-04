import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  reactCompiler: true,
  /* FIXME: 一時的にgithub pagesに公開する都合上のもの、のちに削除 */
  ...(process.env.NODE_ENV === "development"
    ? {}
    : {
      output: "export",
      basePath: "/frontend",
      assetPrefix: "/libraryOfCoffee",
      distDir: "docs",
    }),
  /* ここまで */
};

export default nextConfig;

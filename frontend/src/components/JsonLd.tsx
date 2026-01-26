export function OrganizationJsonLd() {
  const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || "https://mamezusho.com";

  const jsonLd = {
    "@context": "https://schema.org",
    "@type": "Organization",
    name: "豆図書",
    url: baseUrl,
    logo: `${baseUrl}/heroImage.png`,
    description:
      "様々な店舗の中から気になる珈琲豆を選んで、30g×3種類が届くサービス。",
    sameAs: [],
  };

  return (
    <script
      type="application/ld+json"
      // biome-ignore lint/security/noDangerouslySetInnerHtml: JSON-LD requires innerHTML for structured data
      dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }}
    />
  );
}

export function ProductJsonLd() {
  const baseUrl = process.env.NEXT_PUBLIC_BASE_URL || "https://mamezusho.com";

  const jsonLd = {
    "@context": "https://schema.org",
    "@type": "Product",
    name: "豆図書 スタンダードプラン",
    description:
      "様々な珈琲店舗の豆を毎月3種類体験できるプラン。注文後焙煎の新鮮な豆をお届け。",
    image: `${baseUrl}/heroImage.png`,
    brand: {
      "@type": "Brand",
      name: "豆図書",
    },
    offers: {
      "@type": "Offer",
      price: "1500",
      priceCurrency: "JPY",
      availability: "https://schema.org/InStock",
      priceValidUntil: new Date(
        new Date().getFullYear() + 1,
        11,
        31,
      ).toISOString(),
    },
  };

  return (
    <script
      type="application/ld+json"
      // biome-ignore lint/security/noDangerouslySetInnerHtml: JSON-LD requires innerHTML for structured data
      dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }}
    />
  );
}

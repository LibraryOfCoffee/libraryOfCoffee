type JsonLdData = Record<string, unknown> & { "@context": string };

export function JsonLd({ data }: { data: JsonLdData }) {
  return (
    <script
      type="application/ld+json"
      // biome-ignore lint/security/noDangerouslySetInnerHtml: JSON-LD requires dangerouslySetInnerHTML
      dangerouslySetInnerHTML={{ __html: JSON.stringify(data) }}
    />
  );
}

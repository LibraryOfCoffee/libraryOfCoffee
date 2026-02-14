type JsonLdData = Record<string, unknown> & { "@context": string };

export function JsonLd({ data }: { data: JsonLdData }) {
  return <script type="application/ld+json">{JSON.stringify(data)}</script>;
}

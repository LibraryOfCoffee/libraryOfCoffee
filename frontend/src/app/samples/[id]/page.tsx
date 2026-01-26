import type { Metadata } from "next";
import "./sample.css";

type SampleItem = {
  id: number;
  name: string;
};

type Props = {
  params: Promise<{ id: string }>;
};

async function fetchSampleItem(id: string): Promise<SampleItem> {
  const apiUrl = process.env.API_URL || "http://localhost:8080";
  const res = await fetch(`${apiUrl}/api/samples/${id}`, {
    cache: "no-store",
  });

  if (!res.ok) {
    throw new Error("Failed to fetch sample item");
  }

  return res.json();
}

export async function generateMetadata({ params }: Props): Promise<Metadata> {
  const { id } = await params;

  try {
    const item = await fetchSampleItem(id);
    return {
      title: item.name,
      description: `サンプルアイテム: ${item.name}`,
      robots: {
        index: false,
        follow: false,
      },
    };
  } catch {
    return {
      title: "サンプル",
      description: "サンプルページ",
      robots: {
        index: false,
        follow: false,
      },
    };
  }
}

export default async function SamplePage({ params }: Props) {
  const { id } = await params;
  const item = await fetchSampleItem(id);

  return (
    <main className="sample-container">
      <h1 className="sample-title">Sample Page (SSR)</h1>
      <div className="sample-card">
        <div className="sample-card-header">API Response</div>
        <div className="sample-item">
          <span className="sample-label">ID</span>
          <span className="sample-value">{item.id}</span>
        </div>
        <div className="sample-item">
          <span className="sample-label">Name</span>
          <span className="sample-value">{item.name}</span>
        </div>
      </div>
    </main>
  );
}

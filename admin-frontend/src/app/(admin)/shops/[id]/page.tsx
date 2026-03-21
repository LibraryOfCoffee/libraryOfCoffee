import { fetchShop } from "@/api/shops";
import { ShopDetail } from "./_components/ShopDetail";

export default async function ShopDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const shop = await fetchShop(id);

  return <ShopDetail shop={shop} />;
}

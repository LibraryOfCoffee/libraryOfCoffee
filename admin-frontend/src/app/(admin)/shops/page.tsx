import { fetchShops } from "@/api/shops";
import { ShopListTable } from "./_components/ShopListTable";

export const metadata = {
  title: "店舗管理",
};

export default async function ShopsPage({
  searchParams,
}: {
  searchParams: Promise<{ page?: string }>;
}) {
  const { page: pageParam } = await searchParams;
  const page = pageParam ? Number.parseInt(pageParam, 10) : 0;

  const shops = await fetchShops(page);

  return <ShopListTable shops={shops} currentPage={page} />;
}

import { fetchCoffeeBean } from "@/api/coffee-beans";
import { fetchShops } from "@/api/shops";
import { CoffeeBeanDetail } from "./_components/CoffeeBeanDetail";

export default async function CoffeeBeanDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const [coffeeBean, shops] = await Promise.all([
    fetchCoffeeBean(id),
    fetchShops(0, 10),
  ]);
  const initialShops = shops.items.map((s) => ({ id: s.id, name: s.name }));
  return (
    <CoffeeBeanDetail coffeeBean={coffeeBean} initialShops={initialShops} />
  );
}

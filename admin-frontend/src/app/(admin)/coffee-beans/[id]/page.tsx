import { fetchCoffeeBean, fetchProcessingMethods } from "@/api/coffee-beans";
import { fetchShops } from "@/api/shops";
import { fetchTastes } from "@/api/tastes";
import { CoffeeBeanDetail } from "./_components/CoffeeBeanDetail";

export default async function CoffeeBeanDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const [coffeeBean, shops, tastes, processingMethods] = await Promise.all([
    fetchCoffeeBean(id),
    fetchShops(0, 10),
    fetchTastes().catch(() => []),
    fetchProcessingMethods(),
  ]);
  const initialShops = shops.items.map((s) => ({ id: s.id, name: s.name }));
  return (
    <CoffeeBeanDetail
      coffeeBean={coffeeBean}
      initialShops={initialShops}
      tastes={tastes}
      processingMethods={processingMethods}
    />
  );
}

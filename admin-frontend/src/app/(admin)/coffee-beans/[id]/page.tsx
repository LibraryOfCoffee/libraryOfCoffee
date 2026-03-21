import { fetchCoffeeBean } from "@/api/coffee-beans";
import { CoffeeBeanDetail } from "./_components/CoffeeBeanDetail";

export default async function CoffeeBeanDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const coffeeBean = await fetchCoffeeBean(id);
  return <CoffeeBeanDetail coffeeBean={coffeeBean} />;
}

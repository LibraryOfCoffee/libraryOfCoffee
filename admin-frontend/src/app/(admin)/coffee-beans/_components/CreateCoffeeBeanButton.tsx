import { fetchProcessingMethods } from "@/api/coffee-beans";
import { fetchShops } from "@/api/shops";
import { fetchTastes } from "@/api/tastes";
import { CreateCoffeeBeanButtonClient } from "./CreateCoffeeBeanButtonClient";

export async function CreateCoffeeBeanButton() {
  const [shops, tastes, processingMethods] = await Promise.all([
    fetchShops(0, 10),
    fetchTastes().catch(() => []),
    fetchProcessingMethods(),
  ]);
  return (
    <CreateCoffeeBeanButtonClient
      shops={shops.items}
      tastes={tastes}
      processingMethods={processingMethods}
    />
  );
}

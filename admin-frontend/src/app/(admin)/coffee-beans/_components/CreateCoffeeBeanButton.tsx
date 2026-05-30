import { fetchShops } from "@/api/shops";
import { fetchTastes } from "@/api/tastes";
import { CreateCoffeeBeanButtonClient } from "./CreateCoffeeBeanButtonClient";

export async function CreateCoffeeBeanButton() {
  const [shops, tastes] = await Promise.all([fetchShops(0, 10), fetchTastes().catch(() => [])]);
  return <CreateCoffeeBeanButtonClient shops={shops.items} tastes={tastes} />;
}

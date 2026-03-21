import { fetchShops } from "@/api/shops";
import { CreateCoffeeBeanButtonClient } from "./CreateCoffeeBeanButtonClient";

export async function CreateCoffeeBeanButton() {
  const shops = await fetchShops(0, 1000);
  return <CreateCoffeeBeanButtonClient shops={shops.items} />;
}

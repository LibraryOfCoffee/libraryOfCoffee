import { Suspense } from "react";

export const dynamic = "force-dynamic";

import { fetchCoffeeBeans } from "../_lib/coffeeBeanApi";
import { fetchPlans } from "../_lib/planApi";
import "../globals.css";
import { CatalogContent } from "./CatalogClient";

export default async function CatalogPage() {
  const [beans, planGroups] = await Promise.all([
    fetchCoffeeBeans(),
    fetchPlans(),
  ]);
  return (
    <Suspense>
      <CatalogContent beans={beans} planGroups={planGroups} />
    </Suspense>
  );
}

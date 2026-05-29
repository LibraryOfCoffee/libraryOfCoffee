import { Suspense } from "react";
import { fetchCoffeeBeans } from "../_lib/coffeeBeanApi";
import { fetchPlans } from "../_lib/planApi";
import BeansContent from "./BeansContent";

export default async function BeansPage() {
  const [beans, planGroups] = await Promise.all([
    fetchCoffeeBeans(),
    fetchPlans(),
  ]);

  return (
    <Suspense>
      <BeansContent beans={beans} planGroups={planGroups} />
    </Suspense>
  );
}

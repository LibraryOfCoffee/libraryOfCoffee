import { fetchCoffeeBeans } from "@/api/coffee-beans";
import { CoffeeBeanListTable } from "./_components/CoffeeBeanListTable";

export const metadata = {
  title: "コーヒー豆管理",
};

export default async function CoffeeBeansPage({
  searchParams,
}: {
  searchParams: Promise<{ page?: string }>;
}) {
  const { page: pageParam } = await searchParams;
  const page = pageParam ? Number.parseInt(pageParam, 10) : 0;

  const coffeeBeans = await fetchCoffeeBeans(page);

  return <CoffeeBeanListTable coffeeBeans={coffeeBeans} currentPage={page} />;
}

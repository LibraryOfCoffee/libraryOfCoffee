import { notFound } from "next/navigation";
import { fetchPlan } from "@/api/plans";
import { PlanDetail } from "./_components/PlanDetail";

export default async function PlanDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  const plan = await fetchPlan(id);

  if (!plan) {
    notFound();
  }

  return <PlanDetail plan={plan} />;
}

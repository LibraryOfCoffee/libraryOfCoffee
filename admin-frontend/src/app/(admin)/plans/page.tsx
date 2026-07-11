import { Suspense } from "react";
import { fetchPlans } from "@/api/plans";
import { Pagination } from "@/components/Pagination";
import { PlanListTable } from "./_components/PlanListTable";

type SearchParams = {
  page?: string;
  keyword?: string;
};

export default async function PlansPage({
  searchParams,
}: {
  searchParams: Promise<SearchParams>;
}) {
  const params = await searchParams;
  const page = Number.parseInt(params.page ?? "0", 10);
  const keyword = params.keyword;

  return (
    <div>
      <Suspense fallback={<div>読み込み中...</div>}>
        <PlanListContent page={page} keyword={keyword} />
      </Suspense>
    </div>
  );
}

async function PlanListContent({
  page,
  keyword,
}: {
  page: number;
  keyword?: string;
}) {
  const data = await fetchPlans(page, 20, keyword);
  const totalPages = Math.ceil(data.totalCount / data.size);

  return (
    <>
      <PlanListTable plans={data.items} total={data.totalCount} />
      <Pagination
        currentPage={page}
        totalPages={totalPages}
        basePath="/plans"
        query={{ keyword }}
      />
    </>
  );
}

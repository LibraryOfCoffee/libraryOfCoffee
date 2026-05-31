"use client";

import Link from "next/link";
import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import type { PlanListItem } from "@/api/plans";
import listStyles from "@/components/list-page.module.css";
import { PLAN_TYPE_LABELS } from "../_lib/planLabels";

export function PlanListTable({
  plans,
  total,
}: {
  plans: PlanListItem[];
  total: number;
}) {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [keyword, setKeyword] = useState(searchParams.get("keyword") ?? "");

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (keyword) {
      params.set("keyword", keyword);
    }
    router.push(`/plans?${params.toString()}`);
  };

  return (
    <div className={listStyles.container}>
      <div className={listStyles.header}>
        <div className={listStyles.titleGroup}>
          <h1 className={listStyles.title}>プラン管理</h1>
          <p className={listStyles.count}>{total}件のプラン</p>
        </div>
      </div>

      <form onSubmit={handleSearch} className={listStyles.searchForm}>
        <input
          type="text"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          placeholder="プラン表示名で検索"
          className={listStyles.searchInput}
        />
        <button type="submit" className={listStyles.searchButton}>
          検索
        </button>
      </form>

      <div className={listStyles.tableWrapper}>
        <table className={listStyles.table}>
          <thead>
            <tr>
              <th>プラン表示名</th>
              <th>種別</th>
              <th>グラム数</th>
              <th>種類数</th>
              <th>価格</th>
              <th>おすすめ</th>
            </tr>
          </thead>
          <tbody>
            {plans.map((plan) => (
              <tr key={plan.id} className={listStyles.clickableRow}>
                <td>
                  <Link
                    href={`/plans/${plan.id}`}
                    className={listStyles.rowLink}
                  >
                    {plan.label}
                  </Link>
                </td>
                <td>{PLAN_TYPE_LABELS[plan.type] ?? plan.type}</td>
                <td>{plan.gramWeight}g</td>
                <td>{plan.beanQuantity}種</td>
                <td>¥{plan.price.toLocaleString()}</td>
                <td>{plan.isRecommended ? "★" : "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

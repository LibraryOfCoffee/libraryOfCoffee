"use client";

import Link from "next/link";
import type { PlanDetail as PlanDetailType } from "@/api/plans";
import { PLAN_TYPE_LABELS } from "../../_lib/planLabels";
import { PlanActions } from "./PlanActions";
import detailStyles from "./PlanDetail.module.css";

export function PlanDetail({ plan }: { plan: PlanDetailType }) {
  return (
    <div className={detailStyles.container}>
      <div className={detailStyles.header}>
        <Link href="/plans" className={detailStyles.backLink}>
          ← 一覧に戻る
        </Link>
        <PlanActions plan={plan} />
      </div>

      <div className={detailStyles.card}>
        <div className={detailStyles.titleRow}>
          <h1 className={detailStyles.title}>{plan.label}</h1>
          {plan.isRecommended && (
            <span className={detailStyles.badge}>おすすめ</span>
          )}
        </div>

        <dl className={detailStyles.fields}>
          <div className={detailStyles.field}>
            <dt className={detailStyles.label}>ShopifyプランID</dt>
            <dd className={detailStyles.value}>{plan.shopifyPlanId}</dd>
          </div>
          <div className={detailStyles.field}>
            <dt className={detailStyles.label}>プラン表示名</dt>
            <dd className={detailStyles.value}>{plan.label}</dd>
          </div>
          <div className={detailStyles.field}>
            <dt className={detailStyles.label}>プラン種別</dt>
            <dd className={detailStyles.value}>
              {PLAN_TYPE_LABELS[plan.type] ?? plan.type}
            </dd>
          </div>
          <div className={detailStyles.field}>
            <dt className={detailStyles.label}>1種あたりのグラム数</dt>
            <dd className={detailStyles.value}>{plan.gramWeight}g</dd>
          </div>
          <div className={detailStyles.field}>
            <dt className={detailStyles.label}>豆の種類数</dt>
            <dd className={detailStyles.value}>{plan.beanQuantity}種</dd>
          </div>
          <div className={detailStyles.field}>
            <dt className={detailStyles.label}>価格</dt>
            <dd className={detailStyles.value}>
              ¥{plan.price.toLocaleString()}
            </dd>
          </div>
          <div className={detailStyles.field}>
            <dt className={detailStyles.label}>おすすめバッジ</dt>
            <dd className={detailStyles.value}>
              {plan.isRecommended ? "あり" : "なし"}
            </dd>
          </div>
        </dl>
      </div>
    </div>
  );
}

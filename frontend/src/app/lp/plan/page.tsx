"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import AppHeader from "../_components/AppHeader/appHeader";
import CtaFooter from "../_components/CtaFooter/ctaFooter";
import LoadingOverlay from "../_components/LoadingOverlay/loadingOverlay";
import PlanCard from "../_components/PlanCard/planCard";
import StepIndicator from "../_components/StepIndicator/stepIndicator";
import { formatPrice, getPlanById, type PlanId, plans } from "../_lib/planData";
import "../globals.css";
import sharedStyles from "../shared.module.css";
import styles from "./plan.module.css";

function PlanPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const beanId = searchParams.get("beanId");
  const [selectedPlan, setSelectedPlan] = useState<PlanId | null>(null);
  const [loading, setLoading] = useState(false);

  const handleNavigate = (planId: PlanId) => {
    setLoading(true);
    const query = new URLSearchParams();
    query.set("planId", planId);
    if (beanId) query.set("beanId", beanId);
    router.push(`/lp/beans?${query.toString()}`);
  };

  const currentPlan = selectedPlan ? getPlanById(selectedPlan) : undefined;

  return (
    <div className={`${sharedStyles.container} ${sharedStyles.containerStep}`}>
      {loading && <LoadingOverlay />}
      <AppHeader onBack={() => router.push("/lp")} />
      <StepIndicator currentStep={1} />
      <div className={styles.content}>
        <h1 className={styles.title}>プランを選択してください</h1>
        <p className={styles.desc}>
          焙煎したての新鮮な豆を送料無料でお届け。いつでも変更・解約OKです。
        </p>
        <div className={styles.cards}>
          {plans.map((plan) => (
            <PlanCard
              key={plan.id}
              plan={plan}
              selected={selectedPlan === plan.id}
              onSelect={() => setSelectedPlan(plan.id)}
            />
          ))}
        </div>
      </div>
      <CtaFooter
        summaryLabel="お支払い（税込）"
        summaryValue={currentPlan ? `¥${formatPrice(currentPlan.price)}` : "未選択"}
        ctaText="豆を選ぶ"
        onCtaClick={() => {
          if (selectedPlan) handleNavigate(selectedPlan);
        }}
        showIcon={false}
        disabled={!selectedPlan}
      />
    </div>
  );
}

export default function PlanPage() {
  return (
    <Suspense>
      <PlanPageContent />
    </Suspense>
  );
}

"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import AppHeader from "../_components/AppHeader/appHeader";
import CtaFooter from "../_components/CtaFooter/ctaFooter";
import LoadingOverlay from "../_components/LoadingOverlay/loadingOverlay";
import PlanCard from "../_components/PlanCard/planCard";
import StepIndicator from "../_components/StepIndicator/stepIndicator";
import "../globals.css";
import sharedStyles from "../shared.module.css";
import styles from "./plan.module.css";

function PlanPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const beanId = searchParams.get("beanId");
  const [selectedPlan, setSelectedPlan] = useState("cbl-flat-1500");
  const [loading, setLoading] = useState(false);

  const handleNext = () => {
    setLoading(true);
    const query = new URLSearchParams();
    query.set("planId", selectedPlan);
    if (beanId) query.set("beanId", beanId);
    router.push(`/lp/beans?${query.toString()}`);
  };

  return (
    <div className={`${sharedStyles.container} ${sharedStyles.containerStep}`}>
      {loading && <LoadingOverlay />}
      <AppHeader onBack={() => router.push("/lp")} />
      <StepIndicator currentStep={1} />
      <div className={styles.content}>
        <h1 className={styles.title}>プランを選択してください</h1>
        <p className={styles.desc}>
          いつでも変更・解約OK。送料無料でお届けします。
        </p>
        <div className={styles.cards}>
          <PlanCard
            name="月額プラン"
            price="1,500"
            description="30g × 3種類 / 毎月届く"
            selected={selectedPlan === "cbl-flat-1500"}
            onSelect={() => setSelectedPlan("cbl-flat-1500")}
          />
        </div>
      </div>
      <CtaFooter
        summaryLabel="お支払い（税込）"
        summaryValue="¥1,500"
        ctaText="豆を選ぶ"
        onCtaClick={handleNext}
        showIcon={false}
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

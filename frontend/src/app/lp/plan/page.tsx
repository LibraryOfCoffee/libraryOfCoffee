"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import AppHeader from "../_components/AppHeader/appHeader";
import CtaFooter from "../_components/CtaFooter/ctaFooter";
import PlanCard from "../_components/PlanCard/planCard";
import StepIndicator from "../_components/StepIndicator/stepIndicator";
import "../globals.css";
import sharedStyles from "../shared.module.css";
import styles from "./plan.module.css";

function PlanPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const beanId = searchParams.get("bean");
  const [selectedPlan, setSelectedPlan] = useState("monthly");

  const handleNext = () => {
    const params = beanId ? `?bean=${beanId}` : "";
    router.push(`/lp/beans${params}`);
  };

  return (
    <div className={`${sharedStyles.container} ${sharedStyles.containerStep}`}>
      <AppHeader onBack={() => router.push("/lp")} />
      <StepIndicator currentStep={1} />
      <div className={styles.content}>
        <h1 className={styles.title}>プランを選択してください</h1>
        <p className={styles.desc}>
          いつでも変更・解約OK。送料無料でお届けします。
        </p>
        <div className={styles.cards}>
          <PlanCard
            badge="おすすめ"
            name="月額プラン"
            price="1,500"
            description="30g × 3種類 / 毎月届く"
            selected={selectedPlan === "monthly"}
            onSelect={() => setSelectedPlan("monthly")}
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

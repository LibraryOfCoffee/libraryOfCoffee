"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { Suspense, useState } from "react";
import AppHeader from "../_components/AppHeader/appHeader";
import BeanDetailModal from "../_components/BeanDetailModal/beanDetailModal";
import BeanSelectCard from "../_components/BeanSelectCard/beanSelectCard";
import CtaFooter from "../_components/CtaFooter/ctaFooter";
import StepIndicator from "../_components/StepIndicator/stepIndicator";
import { type BeanDetail, beans } from "../_lib/beanData";
import { moveToCoffeeBeanListPage } from "../_lib/purchaseLinkUtil";
import "../globals.css";
import sharedStyles from "../shared.module.css";
import styles from "./beans.module.css";

const MAX_SELECTION = 4;

function BeansPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const planId = searchParams.get("planId");
  const preselectedBeanId = searchParams.get("beanId");

  const [selectedIds, setSelectedIds] = useState<string[]>(() => {
    if (preselectedBeanId && beans.some((b) => b.id === preselectedBeanId)) {
      return [preselectedBeanId];
    }
    return [];
  });
  const [detailBean, setDetailBean] = useState<BeanDetail | null>(null);

  const toggleBean = (id: string) => {
    setSelectedIds((prev) => {
      if (prev.includes(id)) {
        return prev.filter((x) => x !== id);
      }
      if (prev.length >= MAX_SELECTION) return prev;
      return [...prev, id];
    });
  };

  return (
    <div className={`${sharedStyles.container} ${sharedStyles.containerStep}`}>
      <AppHeader onBack={() => router.back()} />
      <StepIndicator currentStep={2} />
      <div className={styles.content}>
        <div className={styles.omakaseSection}>
          <p className={styles.omakaseTerms}>
            <a
              href="https://zcgqx8-tr.myshopify.com/pages/terms_of_service_for_lp"
              target="_blank"
              rel="noopener noreferrer"
            >
              利用規約
            </a>
            に同意して
          </p>
          <button
            type="button"
            className={styles.omakaseButton}
            onClick={() => {
              setSelectedIds([]);
              moveToCoffeeBeanListPage(planId ?? undefined, []);
            }}
          >
            おまかせでお申し込み
            <span>→</span>
          </button>
          <div className={styles.omakaseDivider}>または</div>
        </div>
        <h1 className={styles.title}>
          希望する豆を{MAX_SELECTION}種類選んでください
        </h1>
        <div className={styles.counter}>
          <span className={styles.counterLabel}>選択中</span>
          <div className={styles.counterValue}>
            <span className={styles.counterNum}>{selectedIds.length}</span>
            <span className={styles.counterSlash}>/ {MAX_SELECTION}種類</span>
          </div>
        </div>
        <div className={styles.list}>
          {beans.map((bean) => (
            <BeanSelectCard
              key={bean.id}
              bean={bean}
              selected={selectedIds.includes(bean.id)}
              onToggle={() => toggleBean(bean.id)}
              onDetail={() => setDetailBean(bean)}
            />
          ))}
        </div>
      </div>
      <CtaFooter
        summaryLabel="選択した豆"
        summaryValue={`${selectedIds.length} / ${MAX_SELECTION}種類`}
        subText={
          <>
            <a
              href="https://zcgqx8-tr.myshopify.com/pages/terms_of_service_for_lp"
              target="_blank"
              rel="noopener noreferrer"
            >
              利用規約
            </a>
            に同意して
          </>
        }
        ctaText="アカウント登録に進む"
        onCtaClick={() =>
          moveToCoffeeBeanListPage(planId ?? undefined, selectedIds)
        }
        showIcon={false}
        disabled={selectedIds.length < MAX_SELECTION}
      />

      {detailBean && (
        <BeanDetailModal
          bean={detailBean}
          onClose={() => setDetailBean(null)}
          onSelect={(bean) => {
            if (!selectedIds.includes(bean.id)) {
              toggleBean(bean.id);
            }
            setDetailBean(null);
          }}
        />
      )}
    </div>
  );
}

export default function BeansPage() {
  return (
    <Suspense>
      <BeansPageContent />
    </Suspense>
  );
}

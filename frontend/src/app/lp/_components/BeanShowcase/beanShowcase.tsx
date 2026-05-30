"use client";

import { useState } from "react";
import type { BeanDetail } from "../../_lib/coffeeBeanApi";
import type { PlanGroup } from "../../_lib/planApi";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import BeanCard from "../BeanCard/beanCard";
import BeanDetailModal from "../BeanDetailModal/beanDetailModal";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./beanShowcase.module.css";

const KANTO_PREFECTURES = new Set([
  "東京都",
  "神奈川県",
  "埼玉県",
  "千葉県",
  "茨城県",
  "栃木県",
  "群馬県",
]);

const PREVIEW_COUNT = 6;

interface BeanShowcaseProps {
  beans: BeanDetail[];
  plans: PlanGroup[];
}

export default function BeanShowcase({ beans, plans }: BeanShowcaseProps) {
  const [prefFilter, setPrefFilter] = useState("");
  const [countryFilter, setCountryFilter] = useState("");
  const [roastFilter, setRoastFilter] = useState("");
  const [selectedBean, setSelectedBean] = useState<BeanDetail | null>(null);

  const prefectures = Array.from(
    new Set(
      beans
        .map((b) => b.prefecture)
        .filter(
          (pref): pref is string => !!pref && KANTO_PREFECTURES.has(pref),
        ),
    ),
  );
  const countries = Array.from(new Set(beans.map((b) => b.name)));
  const roastLevels = Array.from(new Set(beans.map((b) => b.tag)));

  const filtered = beans.filter((b) => {
    if (prefFilter && b.prefecture !== prefFilter) return false;
    if (countryFilter && b.name !== countryFilter) return false;
    if (roastFilter && b.tag !== roastFilter) return false;
    return true;
  });

  const visible = filtered.slice(0, PREVIEW_COUNT);
  const remaining = filtered.length - visible.length;
  const anyFilter = prefFilter || countryFilter || roastFilter;

  const resetFilters = () => {
    setPrefFilter("");
    setCountryFilter("");
    setRoastFilter("");
  };

  const minSubPrice =
    plans.length > 0
      ? Math.min(...plans.map((p) => p.subscriptionPrice))
      : null;
  const minSinglePrice =
    plans.length > 0 ? Math.min(...plans.map((p) => p.singlePrice)) : null;

  const purchaseMethods = [
    {
      en: "MONTHLY",
      ja: "定期便",
      sub: "毎月・送料無料・いつでも解約OK",
      price: minSubPrice != null ? `¥${minSubPrice.toLocaleString()}〜` : "",
      unit: "/月",
      primary: true,
    },
    {
      en: "ONE-TIME",
      ja: "単品購入",
      sub: "1回だけのお試しにも",
      price:
        minSinglePrice != null ? `¥${minSinglePrice.toLocaleString()}〜` : "",
      unit: "",
      primary: false,
    },
  ];

  return (
    <section id="beans" className={styles.section}>
      <p className={styles.eyebrow}>— LINEUP</p>
      <h2 className={styles.headline}>好きな豆を、自由に選ぶ。</h2>
      <p className={styles.subtext}>
        様々な珈琲豆から、お好みの組み合わせを。
        <br />
        ♔はスペシャリティコーヒーを示します。
      </p>

      <div className={styles.purchaseMethods}>
        {purchaseMethods.map((m) => (
          <LinkWithLoading
            key={m.en}
            href={getPlanPagePath()}
            className={`${styles.methodCard} ${m.primary ? styles.methodCardPrimary : styles.methodCardSecondary}`}
          >
            <span className={styles.methodEn}>{m.en}</span>
            <div className={styles.methodJa}>{m.ja}</div>
            <div className={styles.methodSub}>{m.sub}</div>
            <div className={styles.methodPrice}>
              <span className={styles.methodPriceNum}>
                {m.price}
                <span className={styles.methodPriceUnit}>{m.unit}</span>
              </span>
              <span className={styles.methodCta}>選ぶ →</span>
            </div>
          </LinkWithLoading>
        ))}
      </div>

      <div className={styles.previewLabel}>
        <span>— PREVIEW</span>
        {anyFilter && (
          <button
            type="button"
            className={styles.clearBtn}
            onClick={resetFilters}
          >
            ✕ クリア
          </button>
        )}
      </div>

      <div className={styles.filters}>
        {[
          {
            label: "焙煎度",
            value: roastFilter,
            set: setRoastFilter,
            opts: roastLevels,
          },
          {
            label: "産地",
            value: countryFilter,
            set: setCountryFilter,
            opts: countries,
          },
          {
            label: "ロースター所在地",
            value: prefFilter,
            set: setPrefFilter,
            opts: prefectures,
          },
        ].map((f) => (
          <div
            key={f.label}
            className={`${styles.filterWrap} ${f.value ? styles.filterWrapActive : styles.filterWrapInactive}`}
          >
            <select
              value={f.value}
              onChange={(e) => f.set(e.target.value)}
              className={`${styles.filterSelect} ${f.value ? styles.filterSelectActive : styles.filterSelectInactive}`}
            >
              <option value="">{f.label}</option>
              {f.opts.map((o) => (
                <option key={o} value={o}>
                  {o}
                </option>
              ))}
            </select>
            <span className={styles.filterArrow}>▾</span>
          </div>
        ))}
      </div>

      <div className={styles.beanList}>
        {visible.length === 0 ? (
          <div className={styles.empty}>
            <p className={styles.emptyTitle}>該当する豆はありません</p>
            <p className={styles.emptyDesc}>
              条件を変えてもう一度お試しください
            </p>
          </div>
        ) : (
          visible.map((b) => (
            <BeanCard
              key={b.id}
              imageSrc={b.imageSrc}
              tag={b.tag}
              name={b.name}
              region={b.region}
              description={b.description}
              roaster={b.roaster}
              isSpecialty={b.isSpecialty}
              onClick={() => setSelectedBean(b)}
            />
          ))
        )}
      </div>

      {remaining > 0 && <div className={styles.andMore}>and more..</div>}

      {selectedBean && (
        <BeanDetailModal
          bean={selectedBean}
          onClose={() => setSelectedBean(null)}
          selectHref={getPlanPagePath(selectedBean.id)}
        />
      )}
    </section>
  );
}

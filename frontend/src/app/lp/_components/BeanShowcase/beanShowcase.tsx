"use client";

import { useState } from "react";
import type { BeanDetail } from "../../_lib/beanData";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import BeanCard from "../BeanCard/beanCard";
import BeanDetailModal from "../BeanDetailModal/beanDetailModal";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import LoadingOverlay from "../LoadingOverlay/loadingOverlay";
import styles from "./beanShowcase.module.css";

const ROASTER_PREFECTURES: Record<string, string> = {
  "+ninth coffee": "東京都",
  "LUSH-COFFEE": "神奈川県",
  "MOSHIMOSHI COFFEE": "東京都",
  marucacoffee: "大阪府",
  "Tama Coffee Roaster": "東京都",
  "NORTH NODE COFFEE": "北海道",
  ゆるり珈琲: "京都府",
  "Black Sloth Coffee": "福岡県",
  "FIVE COFFEE STAND&ROASTERY": "愛知県",
};

const PREVIEW_COUNT = 6;

interface BeanShowcaseProps {
  beans: BeanDetail[];
}

export default function BeanShowcase({ beans }: BeanShowcaseProps) {
  const [prefFilter, setPrefFilter] = useState("");
  const [countryFilter, setCountryFilter] = useState("");
  const [roastFilter, setRoastFilter] = useState("");
  const [selectedBean, setSelectedBean] = useState<BeanDetail | null>(null);
  const [loading, setLoading] = useState(false);

  const prefectures = Array.from(
    new Set(beans.map((b) => ROASTER_PREFECTURES[b.roaster]).filter(Boolean)),
  );
  const countries = Array.from(new Set(beans.map((b) => b.name)));
  const roastLevels = Array.from(new Set(beans.map((b) => b.tag)));

  const filtered = beans.filter((b) => {
    if (prefFilter && ROASTER_PREFECTURES[b.roaster] !== prefFilter)
      return false;
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

  return (
    <section id="beans" className={styles.section}>
      {loading && <LoadingOverlay />}
      <p className={styles.eyebrow}>— LINEUP</p>
      <h2 className={styles.headline}>好きな豆を、自由に選ぶ。</h2>
      <p className={styles.subtext}>
        様々な珈琲豆から、お好みの組み合わせを。
        <br />
        ♔はスペシャリティコーヒーを示します。
      </p>

      <div className={styles.purchaseMethods}>
        {[
          {
            en: "MONTHLY",
            ja: "定期便",
            sub: "毎月・送料無料・いつでも解約OK",
            price: "¥1,500〜",
            unit: "/月",
            primary: true,
          },
          {
            en: "ONE-TIME",
            ja: "単品購入",
            sub: "1回だけのお試しにも",
            price: "¥1,650〜",
            unit: "",
            primary: false,
          },
        ].map((m) => (
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
          onSelect={(bean) => {
            setLoading(true);
            window.location.href = getPlanPagePath(bean.id);
          }}
        />
      )}
    </section>
  );
}

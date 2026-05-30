"use client";

import Image from "next/image";
import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import BeanDetailModal from "../_components/BeanDetailModal/beanDetailModal";
import type { BeanDetail } from "../_lib/coffeeBeanApi";
import {
  groupPlansByGram,
  type PlanGroup,
  type WeightGrams,
} from "../_lib/planApi";
import { moveToCoffeeBeanListPage } from "../_lib/purchaseLinkUtil";
import "../globals.css";
import styles from "./catalog.module.css";

const ROAST_PILL_CLASS: Record<string, string> = {
  浅煎り: styles.pillLight,
  中煎り: styles.pillMedium,
  中深煎り: styles.pillMediumDark,
  深煎り: styles.pillDark,
};

const ROAST_DOT_CLASS: Record<string, string> = {
  浅煎り: styles.dotLight,
  中煎り: styles.dotMedium,
  中深煎り: styles.dotMediumDark,
  深煎り: styles.dotDark,
};

const GRID_CLASS: Record<number, string> = {
  3: styles.omakaseSlotsGrid3,
  4: styles.omakaseSlotsGrid4,
  5: styles.omakaseSlotsGrid5,
};

const GRAM_LABELS: Record<number, string> = {
  30: "お試し",
  60: "標準",
  90: "たっぷり",
};

function CatalogContent({
  beans,
  planGroups,
}: {
  beans: BeanDetail[];
  planGroups: PlanGroup[];
}) {
  const prefectures = Array.from(
    new Set(beans.map((b) => b.prefecture).filter((p): p is string => !!p)),
  );
  const countries = Array.from(new Set(beans.map((b) => b.name)));
  const roastLevels = Array.from(new Set(beans.map((b) => b.tag)));

  const router = useRouter();
  const searchParams = useSearchParams();
  const preselectedBeanId = searchParams.get("beanId");
  const preselectedPlanId = searchParams.get("planId");

  const initialPlan = preselectedPlanId
    ? (planGroups.find((p) => p.subscriptionId === preselectedPlanId) ?? null)
    : null;

  const [mode, setMode] = useState<"sub" | "single">("sub");
  const [gram, setGram] = useState<WeightGrams>(initialPlan?.gramWeight ?? 30);
  const [selectedPlan, setSelectedPlan] = useState<PlanGroup | null>(
    initialPlan,
  );
  const [planChosen, setPlanChosen] = useState(initialPlan !== null);
  const [omakase, setOmakase] = useState(false);
  const [prefFilter, setPrefFilter] = useState("");
  const [countryFilter, setCountryFilter] = useState("");
  const [roastFilter, setRoastFilter] = useState("");
  const [selected, setSelected] = useState<Set<string>>(
    () =>
      new Set(
        preselectedBeanId && beans.some((b) => b.id === preselectedBeanId)
          ? [preselectedBeanId]
          : [],
      ),
  );
  const [activeBeanId, setActiveBeanId] = useState<string | null>(null);
  const [planSheetOpen, setPlanSheetOpen] = useState(false);

  // デフォルトプラン: 定番 30g
  const plan =
    selectedPlan ??
    groupPlansByGram(planGroups, gram).find((p) => p.isRecommended) ??
    groupPlansByGram(planGroups, gram)[0];

  const currentPrice = plan
    ? mode === "sub"
      ? plan.subscriptionPrice
      : plan.singlePrice
    : 0;

  const filtered = beans.filter((b) => {
    if (prefFilter && b.prefecture !== prefFilter) return false;
    if (countryFilter && b.name !== countryFilter) return false;
    if (roastFilter && b.tag !== roastFilter) return false;
    return true;
  });

  const anyFilter = prefFilter || countryFilter || roastFilter;

  const resetFilters = () => {
    setPrefFilter("");
    setCountryFilter("");
    setRoastFilter("");
  };

  const toggleSel = (id: string) => {
    if (!plan) return;
    const next = new Set(selected);
    if (next.has(id)) {
      next.delete(id);
    } else if (next.size < plan.maxSelection) {
      next.add(id);
    }
    setSelected(next);
  };

  const handlePurchase = () => {
    if (!plan) return;
    const planId = mode === "sub" ? plan.subscriptionId : plan.singleId;
    moveToCoffeeBeanListPage(planId, omakase ? [] : [...selected]);
  };

  const handleSelectPlan = (p: PlanGroup) => {
    setSelectedPlan(p);
    setGram(p.gramWeight);
    setPlanChosen(true);
  };

  const activeBean = activeBeanId
    ? (beans.find((b) => b.id === activeBeanId) ?? null)
    : null;

  return (
    <div className={styles.root}>
      {/* sticky header */}
      <div className={styles.header}>
        <div className={styles.headerTop}>
          <button
            type="button"
            className={styles.backBtn}
            onClick={() => router.push("/lp")}
          >
            <span className={styles.backArrow}>‹</span>
            <span>戻る</span>
          </button>
          <div className={styles.headerLogo}>
            <Image
              src="/logo.svg"
              alt="豆図書"
              width={70}
              height={22}
              className={styles.headerLogoImg}
            />
            <span className={styles.catalogLabel}>Catalog</span>
          </div>
          <div className={styles.headerSpacer} />
        </div>

        {planChosen && plan ? (
          <button
            type="button"
            className={styles.planSummary}
            onClick={() => setPlanSheetOpen(true)}
          >
            <div className={styles.planSummaryText}>
              <span className={styles.planSummaryMode}>
                {mode === "sub" ? "定期便" : "単品購入"}
              </span>
              <span className={styles.planSummaryDot}>·</span>
              <span className={styles.planSummaryName}>
                {plan.label}プラン {plan.gramWeight}g×{plan.beanQuantity}種
              </span>
            </div>
            <span className={styles.planSummaryChange}>
              変更<span className={styles.planSummaryChevron}>▾</span>
            </span>
          </button>
        ) : (
          <InlinePlanPicker
            mode={mode}
            gram={gram}
            planGroups={planGroups}
            onChangeMode={setMode}
            onChangeGram={setGram}
            onSelectPlan={handleSelectPlan}
          />
        )}

        {planChosen && (
          <div className={styles.omakaseTabs}>
            {[
              { k: false, label: "自分で選ぶ", sub: "組み合わせを決める" },
              { k: true, label: "すべておまかせ", sub: "豆図書におまかせ" },
            ].map((opt) => (
              <button
                key={String(opt.k)}
                type="button"
                onClick={() => setOmakase(opt.k)}
                className={`${styles.omakaseTab} ${omakase === opt.k ? styles.omakaseTabActive : styles.omakaseTabInactive}`}
              >
                <div className={styles.omakaseTabLabel}>{opt.label}</div>
                <div className={styles.omakaseTabSub}>{opt.sub}</div>
              </button>
            ))}
          </div>
        )}
      </div>

      {/* main scroll content */}
      <div className={styles.scroll}>
        {planChosen && plan && omakase && (
          <OmakaseBlock plan={plan} mode={mode} />
        )}

        {planChosen && plan && !omakase && (
          <>
            {/* filters */}
            <div className={styles.filterArea}>
              <div className={styles.filterRow}>
                {[
                  {
                    label: "焙煎度",
                    value: roastFilter,
                    set: setRoastFilter,
                    opts: roastLevels,
                  },
                  {
                    label: "豆の産地",
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
                  <label key={f.label} className={styles.filterLabel}>
                    <div
                      className={`${styles.filterSelectWrap} ${f.value ? styles.filterSelectWrapActive : ""}`}
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
                  </label>
                ))}
              </div>
              {anyFilter && (
                <div className={styles.filterClearRow}>
                  <button
                    type="button"
                    className={styles.clearBtn}
                    onClick={resetFilters}
                  >
                    <span>✕</span>
                    <span>フィルタをクリア</span>
                  </button>
                </div>
              )}
            </div>

            {/* counter */}
            <div className={styles.counter}>
              <span className={styles.counterText}>
                選択 <span className={styles.counterNum}>{selected.size}</span>
                <span className={styles.counterMax}>
                  {" "}
                  / {plan.maxSelection}種
                </span>
                <span className={styles.counterOmakase}>
                  {"  "}・{"  "}おまかせ {plan.beanQuantity - selected.size}種
                </span>
              </span>
              <span className={styles.counterBeans}>
                {filtered.length} beans
              </span>
            </div>
            <p className={styles.counterNote}>
              ※ 最低1種は豆図書おまかせのため、選べるのは最大{" "}
              <strong>{plan.maxSelection}種</strong> まで（合計{" "}
              {plan.beanQuantity}種）
            </p>

            {/* bean list */}
            <div className={styles.beanList}>
              {filtered.length === 0 ? (
                <div className={styles.empty}>
                  <p className={styles.emptyTitle}>該当する豆はありません</p>
                  <p className={styles.emptyDesc}>
                    フィルタを変更してお試しください
                  </p>
                  <button
                    type="button"
                    className={styles.emptyClear}
                    onClick={resetFilters}
                  >
                    すべての条件をクリア
                  </button>
                </div>
              ) : (
                filtered.map((b, idx) => (
                  <LibraryCard
                    key={b.id}
                    bean={b}
                    selected={selected.has(b.id)}
                    onToggle={() => toggleSel(b.id)}
                    onTap={() => setActiveBeanId(b.id)}
                    isFirst={idx === 0}
                  />
                ))
              )}
            </div>
          </>
        )}
      </div>

      {/* fixed footer CTA */}
      {planChosen && plan && (
        <div className={styles.footerCta}>
          <div className={styles.footerCtaProgress}>
            <span>
              {omakase ? (
                <>
                  <span className={styles.footerCtaLabel}>すべておまかせ</span>
                  <span className={styles.footerCtaSub}>
                    {" "}
                    · 全 {plan.beanQuantity}種
                  </span>
                </>
              ) : (
                <>
                  選択{" "}
                  <span className={styles.footerCtaNum}>{selected.size}</span>
                  <span className={styles.footerCtaSub}>
                    {" "}
                    / {plan.maxSelection}種 ・ おまかせ{" "}
                    {plan.beanQuantity - selected.size}種
                  </span>
                </>
              )}
            </span>
            <span className={styles.footerCtaPrice}>
              ¥{currentPrice.toLocaleString()}
              {mode === "sub" && (
                <span className={styles.footerCtaPriceUnit}>/月</span>
              )}
            </span>
          </div>
          <button
            type="button"
            className={styles.footerCtaBtn}
            onClick={handlePurchase}
          >
            ご購入手続きへ
            <span className={styles.footerCtaArrow}>{"  →"}</span>
          </button>
        </div>
      )}

      {/* modals */}
      {activeBean && (
        <BeanDetailModal
          bean={activeBean}
          onClose={() => setActiveBeanId(null)}
          onSelect={(b) => {
            toggleSel(b.id);
            setActiveBeanId(null);
          }}
        />
      )}

      {planSheetOpen && plan && (
        <PlanSheet
          mode={mode}
          selectedPlan={plan}
          gram={gram}
          planGroups={planGroups}
          onChangeMode={setMode}
          onChangePlan={(p) => {
            setSelectedPlan(p);
            setGram(p.gramWeight);
          }}
          onChangeGram={setGram}
          onClose={() => setPlanSheetOpen(false)}
        />
      )}
    </div>
  );
}

function InlinePlanPicker({
  mode,
  gram,
  planGroups,
  onChangeMode,
  onChangeGram,
  onSelectPlan,
}: {
  mode: "sub" | "single";
  gram: WeightGrams;
  planGroups: PlanGroup[];
  onChangeMode: (m: "sub" | "single") => void;
  onChangeGram: (g: WeightGrams) => void;
  onSelectPlan: (p: PlanGroup) => void;
}) {
  const plans = groupPlansByGram(planGroups, gram);
  return (
    <div className={styles.inlinePicker}>
      <div className={styles.pickerEyebrow}>Step 1</div>
      <h2 className={styles.pickerTitle}>
        まずは購入方法とプランを
        <br />
        お選びください
      </h2>

      <div className={styles.pickerSectionLabel}>購入方法</div>
      <div className={styles.modeSegment}>
        {(
          [
            { k: "sub", label: "定期便", sub: "毎月お届け" },
            { k: "single", label: "単品購入", sub: "1回だけ" },
          ] as const
        ).map((opt) => (
          <button
            key={opt.k}
            type="button"
            onClick={() => onChangeMode(opt.k)}
            className={`${styles.segBtn} ${mode === opt.k ? styles.segBtnActive : styles.segBtnInactive}`}
          >
            <div className={styles.segBtnLabel}>{opt.label}</div>
            <div className={styles.segBtnSub}>{opt.sub}</div>
          </button>
        ))}
      </div>

      <div className={styles.pickerSectionLabel}>1種あたりの容量</div>
      <div className={styles.gramSegment}>
        {([30, 60, 90] as WeightGrams[]).map((g) => (
          <button
            key={g}
            type="button"
            onClick={() => onChangeGram(g)}
            className={`${styles.gramBtn} ${gram === g ? styles.gramBtnActive : styles.gramBtnInactive}`}
          >
            <div className={styles.gramBtnNum}>
              {g}
              <span className={styles.gramBtnUnit}>g</span>
            </div>
            <div className={styles.gramBtnLabel}>{GRAM_LABELS[g]}</div>
          </button>
        ))}
      </div>

      <div className={styles.pickerSectionLabel}>プラン</div>
      <div className={styles.planList}>
        {plans.map((p) => {
          const price = mode === "sub" ? p.subscriptionPrice : p.singlePrice;
          return (
            <button
              key={p.subscriptionId}
              type="button"
              onClick={() => onSelectPlan(p)}
              className={styles.planPickBtn}
            >
              {p.isRecommended && (
                <span className={styles.planPickBadge}>おすすめ</span>
              )}
              <div>
                <div className={styles.planPickName}>{p.label}プラン</div>
                <div className={styles.planPickDesc}>
                  {p.gramWeight}g × {p.beanQuantity}種
                </div>
              </div>
              <div className={styles.planPickRight}>
                <div className={styles.planPickPrice}>
                  ¥{price.toLocaleString()}
                  <span className={styles.planPickUnit}>
                    {mode === "sub" ? "/月" : ""}
                  </span>
                </div>
                <span className={styles.planPickArrow}>→</span>
              </div>
            </button>
          );
        })}
      </div>
      <p className={styles.pickerHint}>プランを選ぶと、豆の選択に進めます</p>
    </div>
  );
}

function OmakaseBlock({
  plan,
  mode,
}: {
  plan: PlanGroup;
  mode: "sub" | "single";
}) {
  return (
    <div className={styles.omakaseBlock}>
      <div className={styles.omakaseCard}>
        <div className={styles.omakaseHatch} />
        <div className={styles.omakaseCardBody}>
          <div className={styles.omakaseEyebrow}>
            Omakase · {plan.beanQuantity}/{plan.beanQuantity}
          </div>
          <h2 className={styles.omakaseTitle}>すべて、豆図書におまかせ。</h2>
          <p className={styles.omakaseDesc}>
            {plan.label}プラン（{plan.gramWeight}g × 全{plan.beanQuantity}
            種）を、豆図書が厳選してお選びします。
            {mode === "sub"
              ? "毎月、開封してからのお楽しみ。"
              : "届くまでのお楽しみ。"}
          </p>
        </div>
        <div
          className={`${styles.omakaseSlots} ${GRID_CLASS[plan.beanQuantity] ?? styles.omakaseSlotsGrid4}`}
        >
          {Array.from({ length: plan.beanQuantity }).map((_, i) => (
            <div key={i} className={styles.omakaseSlot}>
              ?
            </div>
          ))}
        </div>
      </div>
      <div className={styles.omakaseFeatures}>
        {[
          {
            t: "バリエーション豊かに",
            d: "焙煎度・産地・ロースターをバランスよくお届け",
          },
          { t: "出会いを大切に", d: "自分では選ばない一杯との出会いを" },
        ].map((f, i) => (
          <div
            key={f.t}
            className={`${styles.omakaseFeature} ${i ? styles.omakaseFeatureSep : ""}`}
          >
            <div className={styles.omakaseFeatureTitle}>{f.t}</div>
            <div className={styles.omakaseFeatureDesc}>{f.d}</div>
          </div>
        ))}
      </div>
    </div>
  );
}

function LibraryCard({
  bean,
  selected,
  onToggle,
  onTap,
  isFirst,
}: {
  bean: BeanDetail;
  selected: boolean;
  onToggle: () => void;
  onTap: () => void;
  isFirst: boolean;
}) {
  const pillClass = ROAST_PILL_CLASS[bean.tag] ?? styles.pillMedium;
  const dotClass = ROAST_DOT_CLASS[bean.tag] ?? styles.dotMedium;

  return (
    // biome-ignore lint/a11y/noStaticElementInteractions: card opens detail modal; inner button handles selection
    // biome-ignore lint/a11y/useKeyWithClickEvents: card is supplementary; inner button is the primary a11y control
    <div
      className={`${styles.libCard} ${selected ? styles.libCardSelected : ""} ${isFirst ? "" : styles.libCardNotFirst}`}
      onClick={onTap}
    >
      <div className={styles.libCardPunchBar} />
      <div className={styles.libCardBody}>
        <div className={styles.libCardImgWrap}>
          {bean.imageSrc && (
            <Image
              src={bean.imageSrc}
              alt={bean.name}
              fill
              sizes="72px"
              unoptimized
              className={styles.libCardImg}
            />
          )}
          {bean.isSpecialty && (
            <span
              className={styles.libCardCrown}
              role="img"
              aria-label="スペシャリティコーヒー"
            >
              ♔
            </span>
          )}
        </div>
        <div className={styles.libCardContent}>
          <div className={styles.libCardPillRow}>
            <span className={`${styles.libCardPill} ${pillClass}`}>
              <span className={`${styles.libCardPillDot} ${dotClass}`} />
              {bean.tag}
            </span>
          </div>
          <h3 className={styles.libCardName}>
            {bean.name}
            <span className={styles.libCardRegion}>{bean.region}</span>
          </h3>
          <p className={styles.libCardDesc}>{bean.description}</p>
          <div className={styles.libCardFooter}>
            <span className={styles.libCardRoaster}>
              提供店舗{" "}
              <span className={styles.libCardRoasterName}>{bean.roaster}</span>
            </span>
            <button
              type="button"
              className={`${styles.libCardToggle} ${selected ? styles.libCardToggleSelected : ""}`}
              onClick={(e) => {
                e.stopPropagation();
                onToggle();
              }}
            >
              {selected ? "✓ 選択中" : "+ 追加"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

function PlanSheet({
  mode,
  selectedPlan,
  gram,
  planGroups,
  onChangeMode,
  onChangePlan,
  onChangeGram,
  onClose,
}: {
  mode: "sub" | "single";
  selectedPlan: PlanGroup;
  gram: WeightGrams;
  planGroups: PlanGroup[];
  onChangeMode: (m: "sub" | "single") => void;
  onChangePlan: (p: PlanGroup) => void;
  onChangeGram: (g: WeightGrams) => void;
  onClose: () => void;
}) {
  const plans = groupPlansByGram(planGroups, gram);
  return (
    <div className={styles.sheetBackdrop}>
      {/* biome-ignore lint/a11y/noStaticElementInteractions: modal content stops backdrop click */}
      <div
        className={styles.sheetModal}
        onClick={(e) => e.stopPropagation()}
        onKeyDown={(e) => e.stopPropagation()}
      >
        <div className={styles.sheetHeader}>
          <span className={styles.sheetTitle}>プランを変更</span>
          <button type="button" className={styles.sheetClose} onClick={onClose}>
            ×
          </button>
        </div>

        <div className={styles.sheetBody}>
          <div className={styles.sheetSectionLabel}>購入方法</div>
          <div className={styles.modeSegment}>
            {(
              [
                { k: "sub", label: "定期便", sub: "毎月お届け" },
                { k: "single", label: "単品購入", sub: "1回だけ" },
              ] as const
            ).map((opt) => (
              <button
                key={opt.k}
                type="button"
                onClick={() => onChangeMode(opt.k)}
                className={`${styles.segBtn} ${mode === opt.k ? styles.segBtnActive : styles.segBtnInactive}`}
              >
                <div className={styles.segBtnLabel}>{opt.label}</div>
                <div className={styles.segBtnSub}>{opt.sub}</div>
              </button>
            ))}
          </div>

          <div
            className={`${styles.sheetSectionLabel} ${styles.sheetSectionLabelMt}`}
          >
            1種あたりの容量
          </div>
          <div className={styles.gramSegment}>
            {([30, 60, 90] as WeightGrams[]).map((g) => (
              <button
                key={g}
                type="button"
                onClick={() => onChangeGram(g)}
                className={`${styles.gramBtn} ${gram === g ? styles.gramBtnActive : styles.gramBtnInactive}`}
              >
                <div className={styles.gramBtnNum}>
                  {g}
                  <span className={styles.gramBtnUnit}>g</span>
                </div>
                <div className={styles.gramBtnLabel}>{GRAM_LABELS[g]}</div>
              </button>
            ))}
          </div>

          <div
            className={`${styles.sheetSectionLabel} ${styles.sheetSectionLabelMt}`}
          >
            プラン
          </div>
          <div className={styles.planList}>
            {plans.map((p) => {
              const active = selectedPlan.subscriptionId === p.subscriptionId;
              const price =
                mode === "sub" ? p.subscriptionPrice : p.singlePrice;
              return (
                <button
                  key={p.subscriptionId}
                  type="button"
                  onClick={() => onChangePlan(p)}
                  className={`${styles.sheetPlanBtn} ${active ? styles.sheetPlanBtnActive : ""}`}
                >
                  <div>
                    <div className={styles.sheetPlanNameRow}>
                      <span className={styles.sheetPlanName}>
                        {p.label}プラン
                      </span>
                      {p.isRecommended && (
                        <span className={styles.sheetPlanBadge}>おすすめ</span>
                      )}
                    </div>
                    <div className={styles.sheetPlanDesc}>
                      {p.gramWeight}g × {p.beanQuantity}種
                    </div>
                  </div>
                  <div className={styles.sheetPlanPrice}>
                    ¥{price.toLocaleString()}
                    <span className={styles.sheetPlanUnit}>
                      {mode === "sub" ? "/月" : ""}
                    </span>
                  </div>
                </button>
              );
            })}
          </div>
        </div>

        <div className={styles.sheetFooter}>
          <button
            type="button"
            className={styles.sheetConfirm}
            onClick={onClose}
          >
            確定する
          </button>
        </div>
      </div>
    </div>
  );
}

export { CatalogContent };

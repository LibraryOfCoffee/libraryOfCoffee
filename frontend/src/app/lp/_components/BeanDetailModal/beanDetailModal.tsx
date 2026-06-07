"use client";

import Image from "next/image";
import { useEffect } from "react";
import { LuCrown, LuPlus, LuX } from "react-icons/lu";
import { type BeanDetail, SPECIALTY_TAG_COLOR } from "../../_lib/coffeeBeanApi";
import LinkWithLoading from "../LinkWithLoading/linkWithLoading";
import styles from "./beanDetailModal.module.css";

type BeanDetailModalProps = {
  bean: BeanDetail;
  onClose: () => void;
} & (
  | { selectHref: string; onSelect?: never }
  | { onSelect: (bean: BeanDetail) => void; selectHref?: never }
);

export default function BeanDetailModal({
  bean,
  onClose,
  selectHref,
  onSelect,
}: BeanDetailModalProps & {
  selectHref?: string;
  onSelect?: (bean: BeanDetail) => void;
}) {
  useEffect(() => {
    document.body.style.overflow = "hidden";
    return () => {
      document.body.style.overflow = "";
    };
  }, []);

  return (
    <div
      className={styles.overlay}
      role="dialog"
      aria-modal="true"
      tabIndex={-1}
      onKeyDown={(e) => {
        if (e.key === "Escape") onClose();
      }}
    >
      <div className={styles.dialog}>
        <div className={styles.header}>
          <h2>豆の詳細</h2>
          <button
            type="button"
            className={styles.close}
            onClick={onClose}
            aria-label="閉じる"
          >
            <LuX size={18} />
          </button>
        </div>

        <div className={styles.body}>
          {bean.imageSrc && (
            <div className={styles.heroWrap}>
              <Image
                src={bean.imageSrc}
                alt={bean.name}
                fill
                sizes="(max-width: 440px) 100vw, 400px"
                unoptimized
                className={styles.hero}
              />
              {bean.isSpecialty && (
                <span className={styles.crown}>
                  <LuCrown size={16} color={SPECIALTY_TAG_COLOR} />
                </span>
              )}
            </div>
          )}

          <div className={styles.titleRow}>
            <h3>{bean.name}</h3>
            <span className={styles.tag} style={{ background: bean.tagColor }}>
              {bean.tag}
            </span>
          </div>

          {bean.subName && <p className={styles.subtitle}>{bean.subName}</p>}

          <p className={styles.desc}>{bean.detailDescription}</p>

          <div className={styles.separator} />

          <div className={styles.infoGrid}>
            <div className={styles.infoRow}>
              <span className={styles.infoLabel}>産地</span>
              <span className={styles.infoValue}>{bean.origin}</span>
            </div>
            {bean.farm && (
              <div className={styles.infoRow}>
                <span className={styles.infoLabel}>農園</span>
                <span className={styles.infoValue}>{bean.farm}</span>
              </div>
            )}
            <div className={styles.infoRow}>
              <span className={styles.infoLabel}>焙煎度</span>
              <span className={styles.infoValue}>{bean.roastLevel}</span>
            </div>
            <div className={styles.infoRow}>
              <span className={styles.infoLabel}>精製方法</span>
              <span className={styles.infoValue}>{bean.processing}</span>
            </div>
            <div className={styles.infoRow}>
              <span className={styles.infoLabel}>提供ロースター</span>
              <span className={styles.infoValue}>
                <a
                  href={bean.roasterLink}
                  target="_blank"
                  rel="noopener noreferrer"
                  className={styles.roasterLink}
                >
                  {bean.roaster}
                </a>
              </span>
            </div>
          </div>

          <div className={styles.separator} />

          <h4 className={styles.tasteTitle}>テイストプロファイル</h4>
          <div className={styles.tasteGrid}>
            {bean.tasteProfile.map((t) => (
              <div key={t.name} className={styles.tasteItem}>
                <span className={styles.tasteLabel}>{t.name}</span>
                <div className={styles.tasteBarBg}>
                  <div
                    className={styles.tasteBarFill}
                    style={{ width: `${(t.value / 5) * 100}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className={styles.footer}>
          {onSelect ? (
            <button
              type="button"
              className={styles.selectBtn}
              onClick={() => onSelect(bean)}
            >
              この豆を選ぶ
              <LuPlus size={18} />
            </button>
          ) : (
            <LinkWithLoading
              href={selectHref ?? ""}
              className={styles.selectBtn}
            >
              この豆を選ぶ
              <LuPlus size={18} />
            </LinkWithLoading>
          )}
        </div>
      </div>
    </div>
  );
}

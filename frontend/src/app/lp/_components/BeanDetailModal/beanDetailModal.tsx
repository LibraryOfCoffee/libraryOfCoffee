"use client";

import { useEffect } from "react";
import { LuPlus, LuX } from "react-icons/lu";
import type { BeanDetail } from "../../_lib/beanData";
import styles from "./beanDetailModal.module.css";

interface BeanDetailModalProps {
  bean: BeanDetail;
  onClose: () => void;
  onSelect?: (bean: BeanDetail) => void;
}

export default function BeanDetailModal({
  bean,
  onClose,
  onSelect,
}: BeanDetailModalProps) {
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
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
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
          <div
            className={styles.hero}
            style={{ backgroundImage: `url(${bean.imageSrc})` }}
          />

          <div className={styles.titleRow}>
            <h3>{bean.name}</h3>
            <span className={styles.tag} style={{ background: bean.tagColor }}>
              {bean.tag}
            </span>
          </div>

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
              <div key={t.label} className={styles.tasteItem}>
                <span className={styles.tasteLabel}>{t.label}</span>
                <div className={styles.tasteBarBg}>
                  <div
                    className={styles.tasteBarFill}
                    style={{ width: `${t.value}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className={styles.footer}>
          <button
            type="button"
            className={styles.selectBtn}
            onClick={() => onSelect?.(bean)}
          >
            この豆を選ぶ
            <LuPlus size={18} />
          </button>
        </div>
      </div>
    </div>
  );
}

import "./pricingCard.css";

interface InitialSetItem {
  text: string;
}

interface PricingCardProps {
  planName: string;
  planSubtitle: string;
  price: number;
  taxIncluded: boolean;
  freeShipping: boolean;
  coffeeTypes: number;
  cupsPerType: number;
  totalCups: number;
  initialSetItems: InitialSetItem[];
  ctaText: string;
  onCtaClick: () => void;
}

export default function PricingCard({
  planName,
  planSubtitle,
  price,
  taxIncluded = true,
  freeShipping = false,
  coffeeTypes,
  cupsPerType,
  totalCups,
  initialSetItems,
  ctaText,
  onCtaClick,
}: PricingCardProps) {
  return (
    <div className="pricing-card">
      <div className="pricing-card-header">
        <h3 className="pricing-card-plan-name">{planName}</h3>
        <div className="pricing-card-badge">新プラン追加予定！</div>
      </div>
      <p className="pricing-card-subtitle">{planSubtitle}</p>

      <div className="pricing-card-price">
        <span className="pricing-card-price-amount">
          ¥{price.toLocaleString()}
        </span>
        {taxIncluded && <span className="pricing-card-price-tax">(税込)</span>}
      </div>

      {freeShipping && (
        <div className="pricing-card-free-shipping">送料無料</div>
      )}

      <div>
        <div className="pricing-card-divider"></div>
        <div className="pricing-card-summary">
          <span className="pricing-card-summary-text">{coffeeTypes}</span>
          <span className="pricing-card-summary-label">種類</span>
          <span className="pricing-card-summary-text">×{cupsPerType}</span>
          <span className="pricing-card-summary-label">杯</span>
          <span className="pricing-card-summary-label">約</span>
          <span className="pricing-card-summary-total">{totalCups}</span>
          <span className="pricing-card-summary-label">杯分</span>
        </div>
        <div className="pricing-card-summary-note">※1杯あたり10gとした場合</div>

        <div className="pricing-card-divider"></div>
      </div>

      <div className="pricing-card-initial-set">
        <ul className="pricing-card-initial-set-list">
          {initialSetItems.map((item, index) => (
            <li key={index} className="pricing-card-initial-set-item">
              <span className="pricing-card-initial-set-check">✓</span>
              <span className="pricing-card-initial-set-text">{item.text}</span>
            </li>
          ))}
        </ul>
      </div>

      <button className="pricing-card-cta" onClick={onCtaClick}>
        {ctaText}
      </button>
      {/* FIXME: サービス開始時には削除 */}
      <div>
        <span className="notice">
          ※事前登録では料金は発生しません。2026/01/05
          サービス開始に向けて準備中です。
        </span>
      </div>
    </div>
  );
}

import './featureCard.css';

interface FeatureCardProps {
  number: string;
  mainTitle: string;
  iconElement?: React.ReactNode;
  heading: string;
  features: string[];
}

export default function FeatureCard({ number, mainTitle, iconElement, heading, features }: FeatureCardProps) {
  return (
    <div className="lp-feature-card">
      <div className="feature-card-number">メリット {number}</div>
      <h3 className="feature-card-main-title">{mainTitle}</h3>
      {iconElement && (
        <div className="feature-card-icon">
          {iconElement}
        </div>
      )}
      <h4 className="feature-card-heading">{heading}</h4>
      <ul className="feature-card-list">
        {features.map((feature, index) => (
          <li key={index} className="feature-card-list-item">
            <span className="feature-card-check">✓</span>
            <span>{feature}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

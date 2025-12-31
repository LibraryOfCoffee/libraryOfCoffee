import type { PropsWithChildren } from "react";
import "./section.css";

interface SectionProps {
  id?: string;
  title: string;
  subtitle: string;
}

export default function Section({
  id,
  title,
  subtitle,
  children,
}: PropsWithChildren<SectionProps>) {
  return (
    <section id={id} className="lp-section">
      <div className="lp-section-container">
        <div className="lp-section-header">
          <h2 className="lp-section-title">{title}</h2>
          <p className="lp-section-main-text">{subtitle}</p>
        </div>
        {children}
      </div>
    </section>
  );
}

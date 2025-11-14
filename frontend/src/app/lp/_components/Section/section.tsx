import { PropsWithChildren } from 'react';
import Image from "next/image";
import './section.css';

interface SectionProps {
  title: string;
  subtitle: string;
}

export default function Section({ title, subtitle, children }: PropsWithChildren<SectionProps>) {
  return (
    <section className="lp-section">
      <div className="lp-section-container">
        <div className="lp-section-header">
          <h2 className="lp-section-title">
            {title}
          </h2>
          <div className="lp-section-icon">
            <Image
              src="/leafLine.png"
              alt="leafLine"
              height={25}
              width={80}
            />
          </div>
          <p className="lp-section-main-text">
            {subtitle}
          </p>
        </div>
        {children}
      </div>
    </section>
  );
}

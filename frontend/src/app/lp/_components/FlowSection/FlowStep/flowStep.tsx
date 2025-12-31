import type React from "react";
import "./flowStep.css";

interface FlowStepProps {
  title: string;
  description: string[];
  icon: React.ReactNode;
  label?: string;
}

export default function FlowStep({
  title,
  description,
  icon,
  label,
}: FlowStepProps) {
  return (
    <div className="flow-step">
      <div className="flow-step-card">
        {label && <div className="flow-step-label">{label}</div>}
        <div className="flow-step-icon">{icon}</div>
        <div className="flow-step-content">
          <h3 className="flow-step-title">{title}</h3>
          <ul className="flow-step-description">
            {description.map((item, index) => (
              <li key={index} className={item.startsWith("※") ? "note" : ""}>
                {item}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}

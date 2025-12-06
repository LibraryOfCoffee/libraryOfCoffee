import React from "react";
import Image from "next/image";
import FlowStep from "./FlowStep/flowStep";
import "./flowSection.css";

interface Step {
  title: string;
  label?: string;
  description: string[];
  icon: React.ReactNode;
}

export default function FlowSection() {
  const steps: Step[] = [
    {
      title: "会員登録",
      label: "初回は無料で会員登録",
      description: [
        "メールアドレスで簡単登録",
        "プラン購入前に、今月の珈琲豆リストをチェック",
      ],
      icon: (
        <Image
          src="/lpUseCases/1.png"
          alt="会員登録"
          height={100}
          width={100}
        />
      ),
    },
    {
      title: "珈琲豆を登録",
      description: [
        "珈琲豆・店舗情報をチェック",
        "気になる4種を登録",
        "※初回以降は毎月登録となります。その月の登録期間を過ぎると、自動で「月のおすすめ」が４種登録されます。",
      ],
      icon: (
        <Image
          src="/lpUseCases/2.png"
          alt="珈琲豆を登録"
          height={100}
          width={100}
        />
      ),
    },
    {
      title: "プラン購入",
      label: "初回のみ",
      description: ["選択した料金プランを購入"],
      icon: (
        <Image
          src="/lpUseCases/3.png"
          alt="プラン購入"
          height={100}
          width={100}
        />
      ),
    },
    {
      title: "珈琲豆が届く",
      description: [
        "登録した豆から２種＋「月のおすすめ」から１種が届く",
        "※必ず異なる３種類の豆が届きます。",
      ],
      icon: (
        <Image
          src="/lpUseCases/4.png"
          alt="珈琲豆が届く"
          height={100}
          width={100}
        />
      ),
    },
    {
      title: "様々な珈琲体験",
      description: [
        "新しい珈琲との出会い",
        "新鮮な珈琲を味わう",
        "珈琲豆の特徴を知る",
        "店舗の味やこだわりを体験"
      ],
      icon: (
        <Image
          src="/lpUseCases/5.png"
          alt="様々な珈琲体験"
          height={100}
          width={100}
        />
      ),
    },
  ];

  return (
    <div className="flow-section">
      <div className="flow-steps-container">
        {steps.map((step, index) => (
          <React.Fragment key={index}>
            <FlowStep
              title={step.title}
              description={step.description}
              icon={step.icon}
              label={step.label}
            />
            {index < steps.length - 1 && (
              <div className="flow-step-arrow">
                <svg viewBox="0 0 40 40" fill="none">
                  <path
                    d="M15 10L25 20L15 30"
                    stroke="currentColor"
                    strokeWidth="3"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
              </div>
            )}
          </React.Fragment>
        ))}
      </div>
    </div>
  );
}

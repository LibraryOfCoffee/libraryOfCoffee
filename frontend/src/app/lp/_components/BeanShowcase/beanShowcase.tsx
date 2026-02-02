"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import type { BeanDetail } from "../../_lib/beanData";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import BeanCard from "../BeanCard/beanCard";
import BeanDetailModal from "../BeanDetailModal/beanDetailModal";

interface BeanShowcaseProps {
  beans: BeanDetail[];
}

export default function BeanShowcase({ beans }: BeanShowcaseProps) {
  const router = useRouter();
  const [selectedBean, setSelectedBean] = useState<BeanDetail | null>(null);

  return (
    <>
      <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
        {beans.map((b) => (
          <BeanCard
            key={b.name}
            imageSrc={b.imageSrc}
            tag={b.tag}
            tagColor={b.tagColor}
            name={b.name}
            description={b.description}
            tastes={b.tasteProfile.map((t) => t.label)}
            onClick={() => setSelectedBean(b)}
          />
        ))}
      </div>
      {selectedBean && (
        <BeanDetailModal
          bean={selectedBean}
          onClose={() => setSelectedBean(null)}
          onSelect={(bean) => router.push(getPlanPagePath(bean.id))}
        />
      )}
    </>
  );
}

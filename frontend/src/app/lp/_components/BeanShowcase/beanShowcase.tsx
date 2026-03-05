"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import type { BeanDetail } from "../../_lib/beanData";
import { getPlanPagePath } from "../../_lib/purchaseLinkUtil";
import BeanCard from "../BeanCard/beanCard";
import BeanDetailModal from "../BeanDetailModal/beanDetailModal";
import LoadingOverlay from "../LoadingOverlay/loadingOverlay";

interface BeanShowcaseProps {
  beans: BeanDetail[];
}

export default function BeanShowcase({ beans }: BeanShowcaseProps) {
  const router = useRouter();
  const [selectedBean, setSelectedBean] = useState<BeanDetail | null>(null);
  const [loading, setLoading] = useState(false);

  return (
    <>
      {loading && <LoadingOverlay />}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(2, 1fr)",
          gap: 12,
          alignItems: "stretch",
        }}
      >
        {beans.map((b) => (
          <BeanCard
            key={b.id}
            imageSrc={b.imageSrc}
            tag={b.tag}
            tagColor={b.tagColor}
            name={b.name}
            description={b.description}
            roaster={b.roaster}
            isSpecialty={b.isSpecialty}
            onClick={() => setSelectedBean(b)}
          />
        ))}
      </div>
      {selectedBean && (
        <BeanDetailModal
          bean={selectedBean}
          onClose={() => setSelectedBean(null)}
          onSelect={(bean) => {
            setLoading(true);
            router.push(getPlanPagePath(bean.id));
          }}
        />
      )}
    </>
  );
}

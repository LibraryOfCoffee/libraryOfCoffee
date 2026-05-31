"use client";

import { useState } from "react";
import type { PlanDetail } from "@/api/plans";
import actionStyles from "@/components/actions.module.css";
import { EditPlanModal } from "./EditPlanModal";

export function PlanActions({ plan }: { plan: PlanDetail }) {
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [editKey, setEditKey] = useState(0);

  const handleEditOpen = () => {
    setEditKey((prev) => prev + 1);
    setIsEditOpen(true);
  };

  return (
    <div className={actionStyles.actions}>
      <button
        type="button"
        onClick={handleEditOpen}
        className={actionStyles.editButton}
      >
        編集
      </button>
      <EditPlanModal
        key={`edit-${editKey}`}
        plan={plan}
        isOpen={isEditOpen}
        onClose={() => setIsEditOpen(false)}
      />
    </div>
  );
}

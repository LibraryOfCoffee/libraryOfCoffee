"use client";

import { useState } from "react";
import type { PlanDetail } from "@/api/plans";
import actionStyles from "@/components/actions.module.css";
import { EditPlanModal } from "./EditPlanModal";

export function PlanActions({ plan }: { plan: PlanDetail }) {
  const [isEditOpen, setIsEditOpen] = useState(false);

  return (
    <div className={actionStyles.actions}>
      <button
        type="button"
        onClick={() => setIsEditOpen(true)}
        className={actionStyles.editButton}
      >
        編集
      </button>
      <EditPlanModal
        plan={plan}
        isOpen={isEditOpen}
        onClose={() => setIsEditOpen(false)}
      />
    </div>
  );
}

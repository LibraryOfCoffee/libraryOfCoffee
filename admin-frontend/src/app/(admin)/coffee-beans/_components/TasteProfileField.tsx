"use client";

import type { TasteListItem } from "@/api/tastes";
import modalStyles from "@/components/modal.module.css";

export function TasteProfileField({
  tastes,
  getDefaultValue = () => 0,
}: {
  tastes: TasteListItem[];
  getDefaultValue?: (taste: TasteListItem) => number;
}) {
  return (
    <div className={modalStyles.field}>
      <span className={modalStyles.label}>テイストプロファイル</span>
      {tastes.map((taste) => (
        <div key={taste.id}>
          <input type="hidden" name="tasteIds" value={taste.id} />
          <label>
            {taste.name}
            <input
              type="number"
              name={`tasteValue_${taste.id}`}
              min="0"
              max="5"
              defaultValue={getDefaultValue(taste)}
              className={modalStyles.input}
            />
          </label>
        </div>
      ))}
    </div>
  );
}

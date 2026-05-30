import type { TasteListItem } from "@/api/tastes";
import modalStyles from "@/components/modal.module.css";

const TASTE_VALUES = [1, 2, 3, 4, 5] as const;

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
        <div key={taste.id} className={modalStyles.tasteRatingRow}>
          <input type="hidden" name="tasteIds" value={taste.id} />
          <span className={modalStyles.tasteName}>{taste.name}</span>
          <div className={modalStyles.tasteOptions}>
            {TASTE_VALUES.map((value) => (
              <label key={value} className={modalStyles.tasteOption}>
                <input
                  type="radio"
                  name={`tasteValue_${taste.id}`}
                  value={value}
                  defaultChecked={getDefaultValue(taste) === value}
                  className={modalStyles.tasteOptionInput}
                />
                {value}
              </label>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

import modalStyles from "@/components/modal.module.css";

/**
 * トグルスイッチ形式の真偽値入力フィールド。
 *
 * 内部はネイティブの checkbox で、送信値は checked 時に "on"。
 * server action 側で必要なドメイン値へ変換する。
 */
export function ToggleField({
  id,
  name,
  label,
  defaultChecked,
  required = false,
  errors,
}: {
  id: string;
  name: string;
  label: string;
  defaultChecked: boolean;
  required?: boolean;
  errors?: string[];
}) {
  return (
    <div className={modalStyles.toggleField}>
      <label htmlFor={id} className={modalStyles.label}>
        {label}
        {required && <span className={modalStyles.required}>*</span>}
      </label>
      <label className={modalStyles.toggleSwitch}>
        <input
          id={id}
          name={name}
          type="checkbox"
          defaultChecked={defaultChecked}
        />
        <span className={modalStyles.toggleSlider} />
      </label>
      {errors?.map((msg) => (
        <span key={msg} className={modalStyles.fieldError}>
          {msg}
        </span>
      ))}
    </div>
  );
}

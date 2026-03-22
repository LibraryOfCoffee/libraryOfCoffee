"use client";

import { useId, useRef, useState, useTransition } from "react";
import { searchShopsAction } from "@/app/(admin)/coffee-beans/_components/searchShopsAction";
import modalStyles from "@/components/modal.module.css";
import styles from "./shop-search-select.module.css";

type ShopOption = { id: string; name: string };

export function ShopSearchSelect({
  initialShops,
  defaultValue,
  fieldErrors,
}: {
  initialShops: ShopOption[];
  defaultValue?: string;
  fieldErrors?: string[];
}) {
  const inputId = useId();
  const defaultShop = defaultValue
    ? initialShops.find((s) => s.id === defaultValue)
    : undefined;

  const [query, setQuery] = useState(defaultShop?.name ?? "");
  const [selectedId, setSelectedId] = useState(defaultValue ?? "");
  const [shops, setShops] = useState<ShopOption[]>(initialShops);
  const [isOpen, setIsOpen] = useState(false);
  const [isPending, startTransition] = useTransition();
  const debounceRef = useRef<ReturnType<typeof setTimeout>>(null);

  const handleInputChange = (value: string) => {
    setQuery(value);
    setSelectedId("");
    setIsOpen(true);

    if (debounceRef.current) clearTimeout(debounceRef.current);
    debounceRef.current = setTimeout(() => {
      startTransition(async () => {
        const results = await searchShopsAction(value || undefined);
        setShops(results);
      });
    }, 300);
  };

  const handleSelect = (shop: ShopOption) => {
    setSelectedId(shop.id);
    setQuery(shop.name);
    setIsOpen(false);
  };

  return (
    <div className={modalStyles.field}>
      <label htmlFor={inputId} className={modalStyles.label}>
        店舗
        <span className={modalStyles.required}>*</span>
      </label>
      <div className={styles.wrapper}>
        <input type="hidden" name="shopId" value={selectedId} />
        <input
          id={inputId}
          type="text"
          className={modalStyles.input}
          placeholder="店舗名で検索..."
          value={query}
          onChange={(e) => handleInputChange(e.target.value)}
          onFocus={() => setIsOpen(true)}
          onBlur={() => setIsOpen(false)}
        />
        {isOpen && (
          <ul className={styles.dropdown}>
            {shops.length === 0 ? (
              <li className={styles.empty}>
                {isPending ? "検索中..." : "該当する店舗がありません"}
              </li>
            ) : (
              shops.map((shop) => (
                <li key={shop.id} className={styles.dropdownItem}>
                  <button
                    type="button"
                    className={styles.dropdownItemButton}
                    onMouseDown={(e) => {
                      e.preventDefault();
                      handleSelect(shop);
                    }}
                  >
                    {shop.name}
                  </button>
                </li>
              ))
            )}
          </ul>
        )}
      </div>
      {fieldErrors?.map((msg) => (
        <span key={msg} className={modalStyles.fieldError}>
          {msg}
        </span>
      ))}
    </div>
  );
}

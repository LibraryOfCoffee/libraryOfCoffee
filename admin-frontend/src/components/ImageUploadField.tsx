"use client";

import Image from "next/image";
import type { ChangeEvent } from "react";
import { useRef, useState } from "react";
import styles from "@/components/modal.module.css";

const MAX_IMAGE_FILE_SIZE = 1 * 1024 * 1024;

type ImageTypeOption = {
  value: string;
  label: string;
};

type ExistingImage = {
  id: string;
  type: string;
  imageUrl: string;
};

type ImageEntry = {
  key: number;
  type: string;
  replacingImageId?: string;
};

/** 同一typeは1枚のみ許可する種別 */
const SINGLE_ONLY_TYPES = new Set(["MAIN"]);

export function ImageUploadField({
  imageTypes,
  existingImages,
  required,
}: {
  imageTypes: ImageTypeOption[];
  existingImages?: ExistingImage[];
  required?: boolean;
}) {
  const [entries, setEntries] = useState<ImageEntry[]>([]);
  const [sizeErrors, setSizeErrors] = useState<Record<number, string>>({});
  const nextKey = useRef(0);

  const replacingImageIds = new Set(
    entries
      .map((e) => e.replacingImageId)
      .filter((id): id is string => id !== undefined),
  );

  const usedTypes = new Set([
    ...(existingImages ?? [])
      .filter((img) => !replacingImageIds.has(img.id))
      .map((img) => img.type),
    ...entries.map((e) => e.type),
  ]);

  const availableTypes = imageTypes.filter(
    (t) => !SINGLE_ONLY_TYPES.has(t.value) || !usedTypes.has(t.value),
  );

  const addEntry = (replacement?: { imageId: string; imageType: string }) => {
    if (!replacement && availableTypes.length === 0) return;
    const type = replacement?.imageType ?? availableTypes[0].value;
    nextKey.current += 1;
    setEntries((prev) => [
      ...prev,
      { key: nextKey.current, type, replacingImageId: replacement?.imageId },
    ]);
  };

  const clearError = (key: number) => {
    setSizeErrors((prev) => {
      const next = { ...prev };
      delete next[key];
      return next;
    });
  };

  const handleFileChange = (key: number, e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file && file.size > MAX_IMAGE_FILE_SIZE) {
      setSizeErrors((prev) => ({
        ...prev,
        [key]: "1MB以下の画像を選択してください",
      }));
      e.target.value = "";
    } else {
      clearError(key);
    }
  };

  const removeEntry = (key: number) => {
    setEntries((prev) => prev.filter((e) => e.key !== key));
    clearError(key);
  };

  const updateEntryType = (key: number, type: string) => {
    setEntries((prev) => prev.map((e) => (e.key === key ? { ...e, type } : e)));
  };

  return (
    <div className={styles.imageField}>
      <span className={styles.label}>
        画像
        {required && <span className={styles.required}>*</span>}
      </span>

      {existingImages && existingImages.length > 0 && (
        <div className={styles.imagePreview}>
          {existingImages.map((image) =>
            replacingImageIds.has(image.id) ? (
              <div key={image.id} className={styles.previewItem}>
                <Image
                  src={image.imageUrl}
                  alt=""
                  width={80}
                  height={80}
                  className={styles.previewImage}
                  style={{ opacity: 0.4 }}
                  unoptimized
                />
                <span className={styles.previewType}>{image.type}: 変更中</span>
              </div>
            ) : (
              <div key={image.id} className={styles.previewItem}>
                <input type="hidden" name="keepImageIds" value={image.id} />
                <Image
                  src={image.imageUrl}
                  alt=""
                  width={80}
                  height={80}
                  className={styles.previewImage}
                  unoptimized
                />
                <span className={styles.previewType}>{image.type}</span>
                <button
                  type="button"
                  className={styles.changeButton}
                  onClick={() =>
                    addEntry({ imageId: image.id, imageType: image.type })
                  }
                >
                  変更
                </button>
              </div>
            ),
          )}
        </div>
      )}

      {entries.map((entry) => {
        const selectableTypes = imageTypes.filter(
          (t) =>
            !SINGLE_ONLY_TYPES.has(t.value) ||
            !usedTypes.has(t.value) ||
            t.value === entry.type,
        );
        return (
          <div key={entry.key}>
            <div className={styles.imageEntry}>
              <input
                name="images"
                type="file"
                accept="image/jpeg,image/png,image/webp"
                className={styles.fileInput}
                onChange={(e) => handleFileChange(entry.key, e)}
              />
              {entry.replacingImageId ? (
                <input type="hidden" name="imageTypes" value={entry.type} />
              ) : (
                <select
                  name="imageTypes"
                  value={entry.type}
                  onChange={(e) => updateEntryType(entry.key, e.target.value)}
                  className={styles.typeSelect}
                >
                  {selectableTypes.map((t) => (
                    <option key={t.value} value={t.value}>
                      {t.label}
                    </option>
                  ))}
                </select>
              )}
              <button
                type="button"
                className={styles.removeButton}
                onClick={() => removeEntry(entry.key)}
              >
                &times;
              </button>
            </div>
            {sizeErrors[entry.key] && (
              <span className={styles.fieldError}>{sizeErrors[entry.key]}</span>
            )}
          </div>
        );
      })}

      {availableTypes.length > 0 && (
        <button
          type="button"
          className={styles.addImageButton}
          onClick={() => addEntry()}
        >
          + 画像を追加
        </button>
      )}
    </div>
  );
}

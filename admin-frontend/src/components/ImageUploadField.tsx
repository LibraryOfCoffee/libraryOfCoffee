"use client";

import Image from "next/image";
import { useRef, useState } from "react";
import styles from "@/components/modal.module.css";

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
};

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
  const nextKey = useRef(0);

  const addEntry = () => {
    nextKey.current += 1;
    setEntries((prev) => [...prev, { key: nextKey.current }]);
  };

  const removeEntry = (key: number) => {
    setEntries((prev) => prev.filter((e) => e.key !== key));
  };

  return (
    <div className={styles.imageField}>
      <span className={styles.label}>
        画像
        {required && <span className={styles.required}>*</span>}
      </span>

      {existingImages && existingImages.length > 0 && (
        <div className={styles.imagePreview}>
          {existingImages.map((image) => (
            <div key={image.id} className={styles.previewItem}>
              <Image
                src={image.imageUrl}
                alt=""
                width={80}
                height={80}
                className={styles.previewImage}
                unoptimized
              />
              <span className={styles.previewType}>{image.type}</span>
            </div>
          ))}
        </div>
      )}

      {entries.map((entry) => (
        <div key={entry.key} className={styles.imageEntry}>
          <input
            name="images"
            type="file"
            accept="image/jpeg,image/png,image/webp"
            className={styles.fileInput}
          />
          <select
            name="imageTypes"
            defaultValue={imageTypes[0]?.value}
            className={styles.typeSelect}
          >
            {imageTypes.map((t) => (
              <option key={t.value} value={t.value}>
                {t.label}
              </option>
            ))}
          </select>
          <button
            type="button"
            className={styles.removeButton}
            onClick={() => removeEntry(entry.key)}
          >
            &times;
          </button>
        </div>
      ))}

      <button
        type="button"
        className={styles.addImageButton}
        onClick={addEntry}
      >
        + 画像を追加
      </button>
    </div>
  );
}

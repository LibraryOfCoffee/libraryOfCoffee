import Image from "next/image";
import styles from "./beanCard.module.css";

const ROAST_PILL_CLASS: Record<string, string> = {
  浅煎り: styles.roastLight,
  中煎り: styles.roastMedium,
  中深煎り: styles.roastMediumDark,
  深煎り: styles.roastDark,
};

const ROAST_DOT_CLASS: Record<string, string> = {
  浅煎り: styles.dotLight,
  中煎り: styles.dotMedium,
  中深煎り: styles.dotMediumDark,
  深煎り: styles.dotDark,
};

interface BeanCardProps {
  imageSrc: string;
  tag: string;
  name: string;
  region: string;
  description: string;
  roaster: string;
  isSpecialty?: boolean;
  onClick?: () => void;
}

export default function BeanCard({
  imageSrc,
  tag,
  name,
  region,
  description,
  roaster,
  isSpecialty,
  onClick,
}: BeanCardProps) {
  const pillClass = ROAST_PILL_CLASS[tag] ?? styles.roastMedium;
  const dotClass = ROAST_DOT_CLASS[tag] ?? styles.dotMedium;

  return (
    <button type="button" className={styles.card} onClick={onClick}>
      <div className={styles.punchBar} />
      <div className={styles.body}>
        <div className={styles.imgWrap}>
          {imageSrc && (
            <Image
              src={imageSrc}
              alt={name}
              fill
              sizes="72px"
              className={styles.imgInner}
            />
          )}
          {isSpecialty && (
            <span
              className={styles.crown}
              role="img"
              aria-label="スペシャリティコーヒー"
            >
              ♔
            </span>
          )}
        </div>
        <div className={styles.content}>
          <div className={styles.pillRow}>
            <span className={`${styles.roastPill} ${pillClass}`}>
              <span className={`${styles.roastDot} ${dotClass}`} />
              {tag}
            </span>
          </div>
          <h3 className={styles.name}>
            {name}
            <span className={styles.region}>{region}</span>
          </h3>
          <p className={styles.desc}>{description}</p>
          <div className={styles.footer}>
            <span>
              提供店舗<span className={styles.roaster}>{roaster}</span>
            </span>
            <span className={styles.detailLink}>詳細 ›</span>
          </div>
        </div>
      </div>
    </button>
  );
}

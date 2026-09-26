import Image from "next/image";
import RoastTag from "../RoastTag/roastTag";
import SpecialtyBadge from "../SpecialtyBadge/specialtyBadge";
import styles from "./beanCard.module.css";

interface BeanCardProps {
  imageSrc: string;
  tag: string;
  name: string;
  description: string;
  roaster: string;
  isSpecialty?: boolean;
  onClick?: () => void;
}

export default function BeanCard({
  imageSrc,
  tag,
  name,
  description,
  roaster,
  isSpecialty,
  onClick,
}: BeanCardProps) {
  return (
    <button type="button" className={styles.card} onClick={onClick}>
      <div className={styles.body}>
        <div className={styles.imgWrap}>
          {imageSrc && (
            <Image
              src={imageSrc}
              alt={name}
              fill
              sizes="92px"
              unoptimized
              className={styles.imgInner}
            />
          )}
          {isSpecialty && <SpecialtyBadge />}
        </div>
        <div className={styles.content}>
          <div className={styles.headRow}>
            <div className={styles.headText}>
              <div className={styles.roaster}>{roaster}</div>
              <h3 className={styles.name}>{name}</h3>
            </div>
            <RoastTag tag={tag} />
          </div>
          <p className={styles.desc}>{description}</p>
        </div>
      </div>
      <div className={styles.footer}>
        <span className={styles.detailLink}>
          詳細 <span className={styles.detailArrow}>›</span>
        </span>
      </div>
    </button>
  );
}

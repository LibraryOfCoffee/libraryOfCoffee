import styles from "./beanCard.module.css";

interface BeanCardProps {
  imageSrc: string;
  tag: string;
  tagColor: string;
  name: string;
  description: string;
  features?: string[];
  roaster?: string;
  onClick?: () => void;
}

export default function BeanCard({
  imageSrc,
  tag,
  tagColor,
  name,
  description,
  features,
  roaster,
  onClick,
}: BeanCardProps) {
  return (
    <button type="button" className={styles.card} onClick={onClick}>
      <div
        className={styles.img}
        style={{ backgroundImage: `url(${imageSrc})` }}
      >
        {roaster && <p className={styles.roasterName}>{roaster}</p>}
      </div>
      <div className={styles.content}>
        <span className={styles.tag} style={{ background: tagColor }}>
          {tag}
        </span>
        <h3 className={styles.name}>{name}</h3>
        <p className={styles.desc}>{description}</p>
        {features && features.length > 0 && (
          <div className={styles.features}>
            {features.map((f) => (
              <span key={f} className={styles.featureItem}>
                {f}
              </span>
            ))}
          </div>
        )}
      </div>
    </button>
  );
}

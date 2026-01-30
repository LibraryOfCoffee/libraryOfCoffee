import styles from "./beanCard.module.css";

interface BeanCardProps {
  imageSrc: string;
  tag: string;
  tagColor: string;
  name: string;
  description: string;
  tastes: string[];
  onClick?: () => void;
}

export default function BeanCard({
  imageSrc,
  tag,
  tagColor,
  name,
  description,
  tastes,
  onClick,
}: BeanCardProps) {
  return (
    <button type="button" className={styles.card} onClick={onClick}>
      <div
        className={styles.img}
        style={{ backgroundImage: `url(${imageSrc})` }}
      />
      <div className={styles.content}>
        <span className={styles.tag} style={{ background: tagColor }}>
          {tag}
        </span>
        <h3 className={styles.name}>{name}</h3>
        <p className={styles.desc}>{description}</p>
        <div className={styles.tastes}>
          {tastes.map((taste) => (
            <span key={taste} className={styles.taste}>
              {taste}
            </span>
          ))}
        </div>
      </div>
    </button>
  );
}

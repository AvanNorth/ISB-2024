import styles from './index.module.css';

interface Props {
    onChange: (v: string) => void;
}

export const Input = ({onChange}: Props) => {
    return (
        <input
            placeholder="Введите поисковой запрос"
            className={styles.wrapper}
            type="text"
            onChange={(e) => onChange(e.target.value)}
        />
    );
}

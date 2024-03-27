import React, {useState} from 'react';
import axios from "axios";
import styles from './App.module.css';
import {Input} from "./components/input";

function request(value: string): Promise<string[]> {
    return axios({
        method: 'GET',
        url: 'http://localhost:8080/search',
        params: {
            value
        }
    })
        .then(v => v.data);
}

function App() {
    const [urls, setUrls] = useState<string[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [search, setSearch] = useState('');

    const handleSearch = (value: string) => {
        setIsLoading(true);
        setUrls([]);

        if (value === "") {
            return "";
        }

        request(value)
            .then(
            (data) => {
                setUrls(data);
                setIsLoading(false);
            }).catch(() => {
                setIsLoading(false);
            }
        );
    }

    return (
        <div className={styles.wrapper}>
            <div>
                <div>
                    <Input onChange={setSearch}/>
                    <button onClick={() => handleSearch(search)}>Искать</button>
                </div>

                <div className={styles.list}>
                    {
                        isLoading && <p>Загрузка...</p>
                    }
                    {
                        urls.map(item =>
                            <a key={item} href={item} target="_blank">
                                {item}
                            </a>
                        )
                    }
                </div>
            </div>
        </div>
    );
}

export default App;

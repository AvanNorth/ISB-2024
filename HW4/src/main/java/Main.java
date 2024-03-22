import utils.Search;

import java.io.IOException;
import java.util.Arrays;
import static com.github.demidko.aot.WordformMeaning.lookupForMeanings;

public class Main {
    /**
     * 1/ получаем список документов 4 5 6 7 и тп
     * 2/ получаем tf-idf для запроса (1, 2, 1)
     * 3/ для каждой страницы получаем вектор
     * 4/ считаем расстояние
     * 5/ сортируем как надо
     */
    public static void main(String[] args) throws IOException {
        String query = "(Жуткий AND невероятный)";
        String[] documents = Search.interact(query).split("\n");

        String cleanQuery = query.replaceAll("AND|OR|NOT|\\(|\\)", "").replaceAll("[\\s]{2,}", " ");

        String[] tokens = cleanQuery.split(" ");
        double[] queryVector = new double[tokens.length];

        for (int i = 0; i < tokens.length; i++) {
            var meaning = lookupForMeanings(tokens[i]).get(0).getLemma();
            HW
        }
    }

    /**
     * 1. используем реализованный поиск
     * 2. для сортировки этого поиска, нужно почистить запрос от логических слов,
     * разбить на леммы, получить вектор tf-idf
     * 3. сама сортировка - нужно расположить индексы так, чтобы документы относящиеся к индексам
     * располагались в порядке убывания релевантности
     */
    public static void getQueryVector(String str) {

    }
}
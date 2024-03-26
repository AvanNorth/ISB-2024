import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.github.demidko.aot.WordformMeaning.lookupForMeanings;


public class HW4 {
    static final String pathToLemmaFile = "HW3\\src\\main\\resources\\lemmas\\lemmas_%d.txt";

    /**
     * 1/ получаем список документов 4 5 6 7 и тп
     * 2/ получаем tf-idf для запроса (1, 2, 1)
     * 3/ для каждой страницы получаем вектор
     * 4/ считаем расстояние
     * 5/ сортируем как надо
     */
    public static void main(String[] args) throws IOException {
        String query = "клещ";

        List<Double> queryVector = getQueryVector(query);

        for (double q: queryVector)
            System.out.println("vector " + q);

        Map<Integer, Integer> docDist = generateDocDist(queryVector);

        for (Map.Entry<Integer, Integer> e : docDist.entrySet()) {
            System.out.println(e.getKey() + "    " + e.getValue());
        }
    }

    /**
     * 1. используем реализованный поиск
     * 2. для сортировки этого поиска, нужно почистить запрос от логических слов,
     * разбить на леммы, получить вектор tf-idf
     * 3. сама сортировка - нужно расположить индексы так, чтобы документы относящиеся к индексам
     * располагались в порядке убывания релевантности
     */
    public static List<Double> getQueryVector(String query) throws IOException {
        String cleanQuery = query.replaceAll("AND|OR|NOT|\\(|\\)", "").replaceAll("\\s{2,}", " ");

        System.out.println("query is: " + cleanQuery);

        String[] tokens = cleanQuery.split(" ");
        List<Double> queryVector = new ArrayList<>();

        for (String token: tokens) {
            queryVector.add(HW3.tfidfLemma(lookupForMeanings(token).get(0).getLemma().toString()).get("tfidf"));
        }

        return queryVector;
    }

    public static Map<Integer, Integer> generateDocDist(List<Double> queryVector) throws IOException {
        Map<Integer, Integer> distMap = new HashMap<>();

        for (int document = 0; document < 112; document++) {
            List<String> lemmas = FileUtils.readLines(new File(String.format(pathToLemmaFile, document)), "UTF-8");

            List<Double> docVector = new ArrayList<>();

            for (String lemma: lemmas) {
                String[] splLemma = lemma.split(" ");

                docVector.add(Double.valueOf(splLemma[splLemma.length - 1]));
            }

            int dist = 0;

            for (double qV: queryVector) {
                if (docVector.contains(qV))
                   dist++;
            }

            distMap.put(document, dist);
        }

        return distMap;
    }
}
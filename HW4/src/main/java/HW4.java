import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.github.demidko.aot.WordformMeaning.lookupForMeanings;
import static utils.HWUtils2.getPageFromIndex;


public class HW4 {
    static final String pathToLemmaFile = "HW3\\src\\main\\resources\\lemmas\\lemmas_%d.txt";

    public static void main(String[] args) throws IOException {
        String query = "аквариум";

        double[] queryVector = getQueryVector(query);

        for (double q: queryVector)
            System.out.println("vector: " + q);

        Map<Integer, Double> docDist = generateDocDist(queryVector);

        docDist = sortedHashMapByValues(docDist);

        for (Map.Entry<Integer, Double> e : docDist.entrySet()) {
            System.out.println(getPageFromIndex(String.valueOf(e.getKey())) + " " + e.getValue());
        }
    }


    /**
     * Получение вектора для строки запроса
     */
    public static double[] getQueryVector(String query) throws IOException {
        String cleanQuery = query.replaceAll("AND|OR|NOT|\\(|\\)", "").replaceAll("\\s{2,}", " ");

        System.out.println("query is: " + cleanQuery);

        String[] tokens = cleanQuery.split(" ");
        double[] queryVector = new double[tokens.length];

        for (int i = 0; i < queryVector.length; i++) {
            queryVector[i] = (HW3.tfidfLemma(lookupForMeanings(tokens[i]).get(0).getLemma().toString()).get("tfidf"));
        }

        return queryVector;
    }

    /**
     * Получаем вектор для каждой страницы
     */
    public static Map<Integer, Double> generateDocDist(double[] queryVector) throws IOException {
        Map<Integer, Double> distMap = new HashMap<>();

        for (int document = 0; document < 112; document++) {
            List<String> lemmas = FileUtils.readLines(new File(String.format(pathToLemmaFile, document)), "UTF-8");

            double[] docVector = new double[lemmas.size()];

            for (int i = 0; i < lemmas.size(); i++) {
                String[] splLemma = lemmas.get(i).split(" ");

                docVector[i] = (Double.parseDouble(splLemma[splLemma.length - 1]));
            }

            double[] paddedQueryVector = new double[1000];
            double[] paddedDocVector = new double[1000];

            Arrays.fill(paddedDocVector, 1);

            System.arraycopy(queryVector, 0, paddedQueryVector, 0, queryVector.length);
            System.arraycopy(docVector, 0, paddedDocVector, 0, docVector.length);

            distMap.put(document, cosineSimilarity(paddedDocVector, paddedQueryVector));
        }

        return distMap;
    }

    /**
     * Высчитываем косинусное расстояние между двумя векторами
     */
    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            if (vectorA[i] != 0 || vectorB[i] != 0) {
                dotProduct += vectorA[i] * vectorB[i];
                normA += Math.pow(vectorA[i], 2);
                normB += Math.pow(vectorB[i], 2);
            }
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    /**
     * Сортируем мапу
     */
    private static Map<Integer, Double> sortedHashMapByValues(Map<Integer, Double> hashmap) {
        TreeMap<Integer, Double> treeMap = new TreeMap<>((o1, o2) -> {
            if (!Objects.equals(hashmap.get(o1), hashmap.get(o2)))
                return Double.compare(hashmap.get(o1), hashmap.get(o2));

            return o1.compareTo(o2);
        });

        treeMap.putAll(hashmap);
        return treeMap;
    }
}
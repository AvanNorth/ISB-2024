package com.example.demo.utils;

import org.apache.commons.io.FileUtils;
import static com.github.demidko.aot.WordformMeaning.lookupForMeanings;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class VectorSearch {
    static final String pathToLemmaFile = "src\\main\\resources\\lemmas\\lemmas_%d.txt";
    static final String pathToPagesFile = "src\\main\\resources\\pages\\index.txt";


    public static String[] search(String query) throws IOException {
        double[] queryVector = getQueryVector(query);

        Map<Integer, Double> docDist = sortedHashMapByValues(generateDocDist(queryVector));

        String[] result = new String[10];
        int iterator = 0;

        for (Map.Entry<Integer, Double> e : docDist.entrySet()) {
            if (iterator < 10) {
                result[iterator] = getPageFromIndex(String.valueOf(e.getKey())).split(" ")[1];
            }

            iterator++;
        }

        return result;
    }


    /**
     * Получение вектора для строки запроса
     */
    public static double[] getQueryVector(String query) throws IOException {
        String cleanQuery = query.replaceAll("AND|OR|NOT|\\(|\\)", "").replaceAll("\\s{2,}", " ");

        String[] tokens = cleanQuery.split(" ");
        double[] queryVector = new double[tokens.length];

        for (int i = 0; i < queryVector.length; i++) {
            queryVector[i] = (
                    HW3.tfidfLemma(lookupForMeanings(tokens[i]).get(0).getLemma().toString()).get("tfidf")
            );
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

    public static String getPageFromIndex(String index) throws IOException {
        File file = new File(pathToPagesFile);

        List<String> pageIndex = FileUtils.readLines(file, "UTF-8");

        for (String indexStr : pageIndex) {
            if (indexStr.split(" ")[0].equals(index)) {
                return indexStr;
            }
        }

        return "";
    }
}
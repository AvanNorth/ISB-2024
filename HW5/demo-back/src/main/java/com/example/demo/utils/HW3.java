package com.example.demo.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.log10;

public class HW3 {
    static final String pathToPageFile = "src\\main\\resources\\pages\\%d.txt";
    static final String pathToLemmaFile = "src\\main\\resources\\lemmas_hw3\\lemmas_%d.txt";

    public static Map<String, Double> tfidfLemma(String lemma) throws IOException {
        String clearLemma = lemma.toLowerCase();
        double lemmaPagesCount = HWUtils2.getIndexes(clearLemma).size();

        double idf = log10((double) 112 / lemmaPagesCount);
        double tf = tfLemma(clearLemma);

        Map<String, Double> result = new HashMap<>();

        result.put("tf", tf);
        result.put("idf", idf);
        result.put("tfidf", tf * idf);

        return result;
    }

    /**
     * Получаем tf для леммы
     */
    public static double tfLemma(String lemma) throws IOException {
        int numerator = 0;
        int denominator = 0;

        int[] documents = HWUtils2.getIndexes(lemma).stream().mapToInt(i -> i).toArray();

        for (int document : documents) {
            String documentPath = String.format(pathToPageFile, document);
            String text = Tokenizer.cleanPage(documentPath);

            List<String> lemmas = FileUtils.readLines(new File(String.format(pathToLemmaFile, document)), "UTF-8");

            List<String> lemmaForms = new ArrayList<>();

            for (String lemmaStr : lemmas) {
                if (lemmaStr.split(":")[0].equals(lemma)) {
                    lemmaForms.addAll(Arrays.asList(lemmaStr.split(":")[1].trim().split(" ")));

                    break;
                }
            }

            for (String str : lemmaForms) {
                numerator += countWordOccurrences(text, str);
            }

            denominator += countTotalWords(text);
        }

        if (denominator == 0) {
            return 0;
        }

        return (double) numerator / denominator;
    }

    /**
     * Получаем количества слов в тексте
     */
    public static int countTotalWords(String text) throws IOException {
        int allWords = 0;

        for (String word : text.split(" ")) {
            if (!Tokenizer.cleanWord(word.toLowerCase()).isEmpty()) {
                allWords += 1;
            }
        }

        return allWords;
    }

    /**
     * Получаем, сколько раз встречалось слово в тексте
     */
    public static int countWordOccurrences(String text, String value) {
        int result = 0;

        for (String word : text.split(" ")) {
            if (Tokenizer.cleanWord(word.toLowerCase()).equals(value.toLowerCase())) {
                result += 1;
            }
        }

        return result;
    }
}
import org.apache.commons.io.FileUtils;
import utils.HWUtils;
import utils.HWUtils2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.log10;

public class Main {
    static final String pathToPageFile = "HW1\\src\\main\\resources\\pages\\%d.txt";

    static final String pathToTokenFile = "HW1\\src\\main\\resources\\tokens\\tokens_%d.txt";
    static final String pathToLemmaFile = "HW1\\src\\main\\resources\\lemmas\\lemmas_%d.txt";

    static final String pathToTokenAnswerFile = "HW3\\src\\main\\resources\\tokens\\";
    static final String pathToLemmaAnswerFile = "HW3\\src\\main\\resources\\lemmas\\";

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

        int streaming = 112;

        for (int i = 0; streaming > i; i += 14) {
            int finalI = i;

            service.submit(() -> {
                try {
                    tfidfLemma(finalI);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
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

            numerator += countWordOccurrences(text, lemma);
            denominator += countTotalWords(text);
        }

        if (denominator == 0) {
            return 0;
        }

        return (double) numerator / denominator;
    }

    /**
     * Получаем tf для токена
     *
     * @param pageIdx - номер странички
     * @param token   - слово, для которого подсчитывается метрика
     */
    public static double tfToken(Integer pageIdx, String token) throws IOException {
        String documentPath = String.format(pathToPageFile, pageIdx);
        String text = Tokenizer.cleanPage(documentPath);

        return (double) countWordOccurrences(text, token) / countTotalWords(text);
    }

    /**
     * Получаем количества слов в тексте
     */
    public static int countTotalWords(String text) throws IOException {
        int allWords = 0;

        for (String word : text.split(" ")) {
            if (!Tokenizer.cleanWord(word.toLowerCase()).equals(" ")) {
                allWords += 1;
            }
        }

        return allWords;
    }

    /**
     * Получаем, сколько раз встречалось слово в тексте
     */
    public static int countWordOccurrences(String text, String value) throws IOException {
        int result = 0;

        for (String word : text.split(" ")) {
            if (Tokenizer.cleanWord(word.toLowerCase()).equals(value.toLowerCase())) {
                result += 1;
            }
        }

        return result;
    }

    public static void tfidfLemma(int n) throws IOException {

        for (int i = n; i < n + 14; i++) {
            File file = new File(String.format(pathToLemmaFile, i));
            List<String> lemmas = FileUtils.readLines(file, "UTF-8");

            for (String word : lemmas) {
                String clearWord = word.split(":")[0];
                double lemmaPagesCount = HWUtils2.getIndexes(clearWord).size();

                double idf = log10((double) 112 / lemmaPagesCount);
                double tf = tfLemma(clearWord);

                writeTfIdf(word, idf, tf * idf, pathToLemmaAnswerFile, "lemmas_%d.txt", i);
            }
        }
    }

    public static void tfidfToken() throws IOException {

        for (int i = 0; i < 112; i++) {
            File file = new File(String.format(pathToTokenFile, i));
            List<String> tokens = FileUtils.readLines(file, "UTF-8");

            for (String word : tokens) {
                double idf = log10((double) 112 / countWordOccurrences(Tokenizer.cleanPage(String.format(pathToPageFile, i)), word));
                double tf = tfToken(i, word);

                writeTfIdf(word, idf, tf * idf, pathToTokenAnswerFile, "token_%d.txt", i);
            }
        }
    }

    private static void writeTfIdf(String word, double idf, double tf_idf, String path, String fileName, int index) throws IOException {

        String answer = word +
                " " +
                idf +
                " " +
                tf_idf +
                "\n";

        HWUtils.saveStrToFile(path, String.format(fileName, index), answer, true);
    }
}
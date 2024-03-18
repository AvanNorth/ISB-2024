import utils.HWUtils2;

import java.io.IOException;

public class Main {
    static final String pathToPageFile = "HW1\\src\\main\\resources\\pages\\%d.txt";

    public static void main(String[] args) throws IOException {
        System.out.println(tfLemma("выплескивать")); // пример для подсчета tf леммы
        System.out.println(tfToken(100, "выплескивать")); // пример для подсчета tf леммы
    }

    /**
     * Получаем tf для леммы
     */
    public static String tfLemma(String lemma) throws IOException {
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
            return Integer.toString(0);
        }

        return Double.toString((double) numerator / denominator);
    }

    /**
     * Получаем tf для токена
     * @param pageIdx - номер странички
     * @param token - слово, для которого подсчитывается метрика
     */
    public static String tfToken(Integer pageIdx, String token) throws IOException {
        String documentPath = String.format(pathToPageFile, pageIdx);
        String text = Tokenizer.cleanPage(documentPath);

        return Double.toString((double) countWordOccurrences(text, token) / countTotalWords(text));
    }

    /**
     * Получаем количества слов в тексте
     */
    public static int countTotalWords(String text) throws IOException {
        int allWords = 0;

        for (String word: text.split(" ")) {
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

        for (String word: text.split(" ")) {
            if (Tokenizer.cleanWord(word.toLowerCase()).equals(value.toLowerCase())) {
                result += 1;
            }
        }

        return result;
    }
}
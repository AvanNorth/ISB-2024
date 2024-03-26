import com.github.demidko.aot.WordformMeaning;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static com.github.demidko.aot.WordformMeaning.lookupForMeanings;
import static utils.HWUtils.*;

public class Tokenizer {
    static final String pathToTokensFile = "HW1\\src\\main\\resources\\tokens\\";
    static final String pathToLemmasFile = "HW1\\src\\main\\resources\\lemmas\\";
    static final String pathToPageFile = "HW1\\src\\main\\resources\\pages\\%d.txt";

    public static void main(String[] args) throws IOException {
        /*
         * С помощью java.util.Tokenizer разделим текст на токены,
         * при этом очистим нашу страницу от html разметки и прочего, оставив только параграфы текста
         * */
        for (int i = 0; i <= 111; i++) {
            StringTokenizer st = new StringTokenizer(cleanPage(String.format(pathToPageFile, i)));
            HashMap<String, String> mapOfLemmas = new HashMap<>();
            StringBuilder tokenList = new StringBuilder();

            while (st.hasMoreTokens()) {
                /*
                 * Приведем каждый токен к ловер кейсу и найдем для него лемму
                 * */
                String token = cleanWord(st.nextToken().toLowerCase());

                if (token.isEmpty()) {
                    continue;
                }

                tokenList.append(token).append("\n");

                var meaning = lookupForMeanings(token);

                if (!meaning.isEmpty()) {
                    WordformMeaning lemma = meaning.get(0).getLemma();
                    String lineOfTokens = mapOfLemmas.get(lemma.toString()) == null ? "" : mapOfLemmas.get(lemma.toString());

                    lineOfTokens += " " + token;

                    // Очистим строку с токенами от дублей
                    String filteredLine = clenLine(lineOfTokens);
                    mapOfLemmas.put(lemma.toString(), filteredLine);
                }
            }

            saveMapToFile(pathToLemmasFile, String.format("lemmas_%d.txt", i), mapOfLemmas);
            saveStrToFile(pathToTokensFile, String.format("tokens_%d.txt", i), tokenList.toString(), false);
        }
    }

    /*
     * Вытаскиваем весь текст из страницы с помощью поиска по тэгу <p>, <h2>, <h3>
     * */
    static String cleanPage(String path) throws IOException {
        File file = new File(path);
        Document doc = Jsoup.parse(file);

        Elements elementsP = doc.getElementsByTag("p");
        Elements elementsH2 = doc.getElementsByTag("h2");
        Elements elementsH3 = doc.getElementsByTag("h3");

        StringBuilder pageText = new StringBuilder();
        for (Element e : elementsP) {
            pageText.append(e.text()).append(" ");
        }

        for (Element e : elementsH2) {
            pageText.append(e.text()).append(" ");
        }

        for (Element e : elementsH3) {
            pageText.append(e.text()).append(" ");
        }

        return pageText.toString();
    }
    
    static String cleanWord(String dirtyToken) throws IOException {
        // Проверяем, содержит ли строка числа
        if (Pattern.matches(".*\\d.*", dirtyToken)) {
            return ""; // Если содержит, возвращаем пустую строку
        }

        // Удаляем все не-буквы
        return dirtyToken.replaceAll("[^\\p{L}]", "");
    }
}

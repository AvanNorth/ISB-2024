package com.example.demo.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class Tokenizer {
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
    
    static String cleanWord(String dirtyToken) {
        // Проверяем, содержит ли строка числа
        if (Pattern.matches(".*\\d.*", dirtyToken)) {
            return ""; // Если содержит, возвращаем пустую строку
        }

        // Удаляем все не-буквы
        return dirtyToken.replaceAll("[^\\p{L}]", "");
    }
}

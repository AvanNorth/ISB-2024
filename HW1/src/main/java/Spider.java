import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class Spider {
    static final String pathToFile = "src\\main\\resources\\";

    public static void main(String[] args) throws Exception {
        for (int page = 0; page < 7; page++) {
            Connection connection = Jsoup.connect(String.format("https://4lapy.ru/articles/?PAGEN_1=%d", page));
            Document doc = connection.get();

            if (connection.response().statusCode() == 200) {
                Elements artList = doc.getElementsByClass("b-info-blocks__item-link");

                for (int i = 0; i < artList.size(); i++) {
                    String href = artList.get(i).attr("abs:href");

                    downloadPage(16 * page + i, href);
                    saveStrToFile("index.txt", String.format("%d %s\n", 16 * page + i, href), true);
                }
            }
        }
    }

    public static void downloadPage(int index, String url) throws Exception {
        Document ArticleDoc = Jsoup.connect(url).get();
        saveStrToFile(String.format("%d.txt", index), ArticleDoc.html(), false);
    }

    public static void saveStrToFile(String fileName, String html, boolean append) throws IOException {
        final File f = new File(String.format("%s%s", pathToFile, fileName));
        FileUtils.writeStringToFile(f, html, "UTF-8", append);
    }
}
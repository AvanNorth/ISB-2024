import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static final String filePath = "HW1/src/main/resources/lemmas/lemmas_%d.txt";
    static final String saveFilePath = "HW2/src/main/resources/reverse_indexes/";

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static void cleanFiles(String[] args) {
        /*
         * Проходим по всем файлам с леммами, занося информацию об индексе (номере файла или же номере страницы) в мапу,
         *  ключом выступает сама лемма.
         * После чего просто сохраняем мапу в файл в нужном формате (я сделал через преобразование объекта ReverseIndex в json)
         * */
        for (int i = 0; i < 112; i++) {
            try {
                File file = new File(String.format(filePath, i));
                List<String> lemmaList = FileUtils.readLines(file, "UTF-8");

                for (String line : lemmaList) {
                    ArrayList<Integer> currList;
                    String lemma = line.split(":")[0];

                    currList = indexMap.get(lemma) == null ? new ArrayList<>() : indexMap.get(lemma);

                    currList.add(i);
                    indexMap.put(lemma, currList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        saveRevIndexToFile(saveFilePath, "reverse_index.txt", indexMap);
    }
}
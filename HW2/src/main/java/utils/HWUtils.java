package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.ReverseIndex;
import data.ReverseIndexList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HWUtils {
    public static void saveRevIndexToFile(String pathToFile, String fileName, Map<String, ArrayList<Integer>> map) throws IOException {
        final File f = new File(String.format("%s%s", pathToFile, fileName));

        for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()) {
            ObjectMapper mapper = new ObjectMapper();

            ReverseIndex ri = new ReverseIndex(entry.getValue().size(), entry.getValue(), entry.getKey());
            FileUtils.writeStringToFile(f, mapper.writeValueAsString(ri) + "\n", "UTF-8", true);
        }
    }

    public static String getIndexes(String word) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String path = "HW2/src/main/resources/reverse_indexes/";

        File file = new File(path);
        List<String> reverseIndex = FileUtils.readLines(file, "UTF-8");

        ReverseIndexList jsonList = new ReverseIndexList(new ArrayList<ReverseIndex>());

        for (String indexStr : reverseIndex) {
            ReverseIndex obj = mapper.readValue(indexStr, ReverseIndex.class);
            jsonList.getIndexArrayList().add(obj);
        }

        ArrayList<Integer> indexArray = jsonList.findIndexes(word);

        return Arrays.toString(indexArray.toArray());
    }
}

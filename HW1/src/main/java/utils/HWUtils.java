package utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HWUtils {

    public static void saveStrToFile(String pathToFile, String fileName, String str, boolean append) throws IOException {
        final File f = new File(String.format("%s%s", pathToFile, fileName));
        FileUtils.writeStringToFile(f, str, "UTF-8", append);
    }

    public static void saveMapToFile(String pathToFile, String fileName, Map<String, String> map) throws IOException {
        final File f = new File(String.format("%s%s", pathToFile, fileName));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            FileUtils.writeStringToFile(f, entry.getKey() + ":" + entry.getValue() + "\n", "UTF-8", true);
        }
    }

    public static String clenLine(String line) {
        return Arrays.stream(line.split(" "))
                .distinct()
                .collect(Collectors.joining(" "));
    }
}

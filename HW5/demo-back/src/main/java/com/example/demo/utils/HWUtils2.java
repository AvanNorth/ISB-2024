package com.example.demo.utils;

import com.example.demo.utils.data.ReverseIndex;
import com.example.demo.utils.data.ReverseIndexList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HWUtils2 {
    public static ArrayList<Integer> getIndexes(String word) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String path = "src\\main\\resources\\reverse_indexes\\reverse_index.txt";

        File file = new File(path);
        List<String> reverseIndex = FileUtils.readLines(file, "UTF-8");

        ReverseIndexList jsonList = new ReverseIndexList(new ArrayList<ReverseIndex>());

        for (String indexStr : reverseIndex) {
            ReverseIndex obj = mapper.readValue(indexStr, ReverseIndex.class);
            jsonList.getIndexArrayList().add(obj);
        }

        return jsonList.findIndexes(word);
    }
}

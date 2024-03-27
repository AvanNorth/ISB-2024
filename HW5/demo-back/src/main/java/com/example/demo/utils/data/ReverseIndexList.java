package com.example.demo.utils.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ReverseIndexList {
    public ArrayList<ReverseIndex> getIndexArrayList() {
        return indexArrayList;
    }

    @JsonProperty
    private final ArrayList<ReverseIndex> indexArrayList;

    public ReverseIndexList(ArrayList<ReverseIndex> list) {
        this.indexArrayList = list;
    }

    public ArrayList<Integer> findIndexes(String word) {
        for (ReverseIndex reverse : indexArrayList) {
            if (reverse.getWord().equals(word))
                return reverse.getInverted_array();
        }
        return new ArrayList<>();
    }

}

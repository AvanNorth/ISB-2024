package data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ReverseIndex {
    @JsonProperty
    private final int count;

    @JsonProperty
    private final ArrayList<Integer> inverted_array;

    @JsonProperty
    private final String word;

    public int getCount() {
        return count;
    }

    public ArrayList<Integer> getInverted_array() {
        return inverted_array;
    }

    public String getWord() {
        return word;
    }

    public ReverseIndex(@JsonProperty("count") int count, @JsonProperty("inverted_array") ArrayList<Integer> list,@JsonProperty("word") String word) {
        this.count = count;
        this.inverted_array = list;
        this.word = word;
    }
}

package data;

import java.util.ArrayList;

public class ReverseIndex {
    private final int count;
    private final ArrayList<Integer> inverted_array;
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

    public ReverseIndex(int count, ArrayList<Integer> list, String word) {
        this.count = count;
        this.inverted_array = list;
        this.word = word;
    }
}

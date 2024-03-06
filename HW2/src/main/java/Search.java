import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static utils.HWUtils.getIndexes;


public class Search {
    public static void main(String[] args) {
        interact();
    }

    static void interact() {
        System.out.println("Введите ваш запрос: ");

        Scanner sc = new Scanner(System.in);

        String searchString = sc.nextLine();
    }

    static void makeSearch(String searchString) throws IOException {
        String[] searchList = splitString(searchString);

        searchList = findNOT(searchList);
        searchList = findAND(searchList);

        Integer[] indexes;
    }

    static String[] splitString(String searchString) {
        return searchString.split(" ");
    }

    static String[] findNOT(String[] searchList) {
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i].equals("NOT")) {
                searchList[i + 1] = searchList[i] + " " + searchList[i + 1];

                searchList[i] = "";
            }
        }

        return searchList;
    }

    static String[] findAND(String[] searchList) throws IOException {
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i].equals("AND")) {
                searchList[i] = AND(searchList[i - 1], searchList[i + 1]);

                searchList[i - 1] = "";
                searchList[i + 1] = "";
            }
        }

        return searchList;
    }

    static String AND(String w1, String w2) throws IOException {
        ArrayList<Integer> answer = new ArrayList<>();

        boolean w1NOT = false;
        boolean w2NOT = false;


        if (w1.split(" ")[0].equals("NOT")) {
            w1NOT = true;
            w1 = w1.split(" ")[1];
        }

        if (w2.split(" ")[0].equals("NOT")) {
            w2NOT = true;
            w2 = w2.split(" ")[1];
        }

        String w1Index = getIndexes(w1);

        int[] arr1 = Arrays.stream(w1Index.substring(1, w1Index.length() - 1).split(","))
                .map(String::trim).mapToInt(Integer::parseInt).toArray();

        for (Integer i : arr1) {
            if (w2.contains(i.toString()))
                answer.add(i);
        }

        return Arrays.toString(answer.toArray());
    }
}

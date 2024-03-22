package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import static utils.HWUtils2.getIndexes;
import static utils.HWUtils2.getPageFromIndex;


public class Search {
    public static void main(String[] args) throws IOException {
        // interact();
    }

    public static String interact(String str) throws IOException {
        // System.out.println("Введите ваш запрос: ");

        // Scanner sc = new Scanner(System.in);

        /// String searchString = sc.nextLine();
        return openExpressions(str);
    }

    static String makeSearch(String searchString) throws IOException {
        String[] searchList = splitString(searchString);

        searchList = findNOT(searchList);
        searchList = findAND(searchList);
        searchList = findOR(searchList);

        return searchList[searchList.length - 1];
    }

    public static String openExpressions(String value) throws IOException {
        int lastOpenBracketIdx = -1; // индекс последней открывающейся скобки
        int closeBracketForOpenIdx = -1; // индекс закрывающейся скобки, которая встречается сразу после открывающейся

        for (int i = 0; i < value.length(); i++) {
            char symbol = value.charAt(i);

            if (symbol == '(') {
                lastOpenBracketIdx = i + 1;
                closeBracketForOpenIdx = -1;
            }

            if (symbol == ')' && closeBracketForOpenIdx == -1) {
                closeBracketForOpenIdx = i;
            }
        }

        boolean hasSubstring = lastOpenBracketIdx != -1 && closeBracketForOpenIdx != -1; // случай, когда мы нашли подвыражение

        if (!hasSubstring) {
            return outResult(makeSearch(value));
        }

        String substring = value.substring(lastOpenBracketIdx, closeBracketForOpenIdx);

        String valueWithoutSubstring =
                value.substring(0, lastOpenBracketIdx - 1)
                        + makeSearch(substring)
                        + value.substring(closeBracketForOpenIdx + 1);

        return openExpressions(valueWithoutSubstring);
    }

    static String outResult(String searchResult) throws IOException {
        String[] searchResultArr = searchResult.replaceAll("\\[", "")
                .replaceAll("]", "").split(",");

        StringBuilder str = new StringBuilder();
        for (String s : searchResultArr) {
            if (!s.equals("")){
                str.append(getPageFromIndex(s.trim()));
                str.append("\n");
            }
        }

        return str.toString();
    }

    static String[] splitString(String searchString) {
        searchString = searchString.replaceAll(", ", ",_");
        String[] answer = searchString.split(" ");

        for (int i = 0; i < answer.length; i++) {
            answer[i] = answer[i].replaceAll(",_", ",  ");
        }

        return answer;
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
                searchList[i + 1] = doLogic(searchList[i - 1], searchList[i + 1], "AND").trim();

                searchList[i - 1] = "";
                searchList[i] = "";
            }
        }

        return searchList;
    }

    static String[] findOR(String[] searchList) throws IOException {
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i].equals("OR")) {
                searchList[i + 1] = doLogic(searchList[i - 1], searchList[i + 1], "OR").trim();

                searchList[i - 1] = "";
                searchList[i] = "";
            }
        }

        return searchList;
    }

    static String doLogic(String w1, String w2, String logic) throws IOException {
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

        ArrayList<Integer> w1Index = parseIndexes(w1);
        ArrayList<Integer> w2Index = parseIndexes(w2);

        switch (logic) {
            case "AND":
                AND(w1Index, w2Index, w1NOT, w2NOT, answer);
            case "OR":
                OR(w1Index, w2Index, w1NOT, w2NOT, answer);
        }

        return Arrays.toString(answer.toArray());
    }

    static void AND(ArrayList<Integer> w1Index, ArrayList<Integer> w2Index, Boolean w1NOT, Boolean w2NOT, ArrayList<Integer> answer) {
        if (w1NOT && !w2NOT) {
            for (Integer i : w2Index) {
                if (!w1Index.contains(i)) {
                    answer.add(i);
                }
            }
        }

        if (!w1NOT && w2NOT) {
            for (Integer i : w1Index) {
                if (!w2Index.contains(i)) {
                    answer.add(i);
                }
            }
        }

        if (!w1NOT && !w2NOT) {
            for (Integer i : w1Index) {
                if (w2Index.contains(i)) {
                    answer.add(i);
                }
            }
        }
    }


    static void OR(ArrayList<Integer> w1Index, ArrayList<Integer> w2Index, Boolean w1NOT, Boolean w2NOT, ArrayList<Integer> answer) {
        if (w1NOT) {
            answer.addAll(w2Index);
        }

        if (w2NOT) {
            answer.addAll(w1Index);
        }

        if (!w1NOT && !w2NOT){
            for (int i : w2Index) {
                if (!w1Index.contains(i))
                    w1Index.add(i);
            }

            answer.addAll(w1Index);
        }
    }

    static ArrayList<Integer> parseIndexes(String str) throws IOException {
        if (str.contains("[") && str.contains("]")) {
            String[] parseStringArr = str.replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .split(",");

            Integer[] arr = new Integer[parseStringArr.length];
            for (int i = 0; i < parseStringArr.length; i++) {
                arr[i] = Integer.parseInt(parseStringArr[i].trim());
            }

            ArrayList<Integer> parsedInd = new ArrayList<>();
            Collections.addAll(parsedInd, arr);

            return parsedInd;
        }

        return getIndexes(str);
    }
}

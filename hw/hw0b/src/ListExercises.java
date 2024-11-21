import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;

public class ListExercises {

    /**
     * Returns the total sum in a list of integers
     */
    public static int sum(List<Integer> L) {
        int sum = 0;
        if (L.size() == 0) {
            return 0;
        } else {
            for (int l : L) {
                sum += l;
            }
            return sum;
        }
    }

    /**
     * Returns a list containing the even numbers of the given list
     */
    public static List<Integer> evens(List<Integer> L) {
        List<Integer> evens = new ArrayList<>();
        for (int l : L) {
            if (l % 2 == 0) {
                evens.add(l);
            }
        }
        return evens;
    }

    /**
     * Returns a list containing the common item of the two given lists
     */
    public static List<Integer> common(List<Integer> L1, List<Integer> L2) {
        List<Integer> common = new ArrayList<>();
        for (int l1 : L1) {
            for (int l2 : L2) {
                if (l1 == l2) {
                    common.add(l1);
                }
            }
        }
        return common;
    }


    /**
     * Returns the number of occurrences of the given character in a list of strings.
     */
    public static int countOccurrencesOfC(List<String> words, char c) {
        int sum = 0;
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                if (c == word.charAt(i)) {
                    sum++;
                }
            }
        }
        return sum;
    }
}

import java.util.Scanner;
import java.util.*;

/**
 * This class implements the Boyer-Moore-Horspool algorithm
 * with wild cards support.
 */ 
public class BoyerMoore {

    // construct the algorithm for the given text and pattern
    // return a list of starting position that the pattern is found in the text.
    public static ArrayList<Integer> match(String text, String pattern) {

        ArrayList<Integer> positions = new ArrayList<Integer>();


        // build up the bad character shift table
        Map<Character, Integer> badTable = buildBadCharShiftTable(pattern);

        // the search algorithm using good suffix table with bad character shift optimization
        int i = 0;
        boolean matched = false;
        while (i <= text.length() - pattern.length()) {
            int j = pattern.length() - 1; // match character from back to front
            while (j >= 0 && match(pattern.charAt(j), text.charAt(i + j))) {
                j --;
            }
            if (j < 0) { // a match is found at index i, add into the result list
                positions.add(i);
                i = i + 1;
            } else { 
                // not match at this charcter
                int shift = 1;
                // check if a bad character shift will shift more
                int shift1 = shift;
                int shift2 = shift;
                if (badTable.containsKey(text.charAt(i + j))) {
                    shift1 = j - badTable.get(text.charAt(i + j));
                }
                if (badTable.containsKey('_')) {
                    shift2 = j - badTable.get('_');
                    // shift the smaller one between two cases of bad character
                    // considering the wildcard
                    if (shift2 < shift1) {
                        shift1 = shift2;
                    }
                }
                if (shift1 > shift) { // shift more
                    shift = shift1;
                }
                i = i + shift;
            }
        }

        return positions;
    }


    // a helper function to check if two characters match each other
    private static boolean match(char c1, char c2) {
        return c1 == '_' || c1 == c2;
    }

    // build up the bad character shift table
    private static Map<Character, Integer> buildBadCharShiftTable(String pattern) {
        Map<Character, Integer> result = new HashMap<Character, Integer>();
        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);
            result.put(chr, i);
        }
        return result;
    }
}



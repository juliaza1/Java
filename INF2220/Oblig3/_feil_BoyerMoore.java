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

        // build up the good suffix table for the pattern
        int[] goodTable = buildGoodSuffixTable(pattern);

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
                i = i + goodTable[0];
            } else { 
                // not match at this charcter
                int shift = goodTable[j + 1];
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

    // build up the good suffix table
    // this function is enhanced to support wild cards
    private static int[] buildGoodSuffixTable(String pattern) {
        int len = pattern.length();
        int i = len, j = len + 1;
        int[] f = new int[len + 1]; // borders array
        int[] s = new int[len + 1]; // shift table
        f[len] = len + 1;

        while (i > 0) {
            // the pattern will rewind to the wildcard if any
            while (j <= len && !match(pattern.charAt(i - 1), pattern.charAt(j - 1))) {
                if (s[j] == 0) {
                    s[j] = j - i;
                }
                j = f[j];
            }
            i--;
            j--;
            f[i] = j;
        }

        // build up the shift table from the borders array
        j = f[0];
        for (i = 0; i <= len; i++) {
            if (s[i] == 0) {
                s[i] = j;
            }
            if (i == j) {
                j = f[j];
            }
        }
        return s;
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



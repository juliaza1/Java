import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

class Oblig3 {
    public static void main(String[] args) {
        String needle ="";
        String haystack ="";         
        // Read command-line arguments
        if (args.length != 2) {
            String error="InputError, use: java Oblig3 <needle> <haystack>";
            System.out.println(error);
            return;
        }

        try {
            File n = new File(args[0]);
            File h = new File(args[1]);
            Scanner needleScnr = new Scanner(n);
            Scanner haystackScnr = new Scanner(h);
            int lineNumber = 1;
            
            while(needleScnr.hasNextLine()){
                needle = needleScnr.nextLine().trim();
            }

            while(haystackScnr.hasNextLine()) {
                haystack = haystackScnr.nextLine().trim();
            }

            needleScnr.close();
            haystackScnr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ArrayList<Integer> positions = new ArrayList<Integer>();
        if (haystack.length() > 0) {
            // found all the matching positions
            positions = BoyerMoore.match(haystack, needle);
        }

        // display results
        System.out.printf("%d match(es).\n", positions.size());
        int len = needle.length();
        for (int position : positions) {
            System.out.printf("index %2d: %s\n", position, haystack.substring(position, position + len));
        }
    }
}

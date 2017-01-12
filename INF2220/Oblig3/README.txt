Explanation

The BoyerMoore class implements the Boyer-Moore-Horspool algorithm
with bad character shift optimization with wildcard support.
To support wildcard, the good suffix table is built for the pattern in the way so that
_ matches any character. For example, consider the pattern a_ba. The shift back position
for the second 'a' is 1 (the _ character) instead of 0 (the first 'a' character).
In addition, the bad character shift optimization will pick up the smaller distance if both '_' and the unmatched character exists before.
The details are in BoyerMoore.java.


The main method is in class Oblig3.java.
Use javac *.java to compile.

The program assumes the haystack does not contain wildcards.


Test Cases

(1) java AssignmentThree "" cogwrgaccag

12 match(es).
index  0: 
index  1: 
index  2: 
index  3: 
index  4: 
index  5: 
index  6: 
index  7: 
index  8: 
index  9: 
index 10: 
index 11: 

(2) java AssignmentThree _ cogwrgaccag

11 match(es).
index  0: c
index  1: o
index  2: g
index  3: w
index  4: r
index  5: g
index  6: a
index  7: c
index  8: c
index  9: a
index 10: g

(3) java AssignmentThree c_g cogwrgaccag

2 match(es).
index  0: cog
index  8: cag

(4) java AssignmentThree _c_g cogwrgaccag 

1 match(es).
index  7: ccag

(5) java AssignmentThree "" ""

0 match(es).



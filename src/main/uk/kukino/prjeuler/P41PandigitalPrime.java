package uk.kukino.prjeuler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 * meh solution, it should not use objects and collections... but finishes in a couple of seconds anyway.
 */

public class P41PandigitalPrime {

    private static boolean isPrime(long possible) {
        long last = possible % 10;
        if (last == 2 || last == 4 || last == 5 || last == 6 || last == 8 || last == 0) { //assuming number >10
            return false;
        }
        for (long i=3; i<possible/2; i++) {
            if ((possible % i) == 0) {
                return false;
            }
        }
        return true;
    }

    private static long toLong(List<Byte> bytes) {
        long result = 0;
        long mult = (long)Math.pow(10,bytes.size()-1);
        for (byte b : bytes) {
            result = result + b * mult;
            mult = mult / 10;
        }
        return result;
    }

    private static void buildWith(List<Long> result, Set<Byte> availDigits, List<Byte> wip) {
        if (availDigits.isEmpty()) {
            result.add(toLong(wip));
        } else {
            for (Byte b : availDigits) {
                wip.add(b);
                Set<Byte> subAvail = new HashSet<>();
                subAvail.addAll(availDigits);
                subAvail.remove(b);
                buildWith(result, subAvail, wip);
                wip.remove(b);
            }
        }

    }

    private static List<Long> allPalindromes() {
        List<Long> results = new ArrayList<>();
        Set<Byte> digits = new HashSet<>();
        digits.add((byte)1); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)2); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)3); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)4); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)5); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)6); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)7); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)8); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)9); buildWith(results, digits, new ArrayList<>());
        digits.add((byte)0); buildWith(results, digits, new ArrayList<>());
        return results;
    }


    public static void main(String[] args) {
        List<Long> palindromes = allPalindromes();
        Collections.sort(palindromes);
        Collections.reverse(palindromes);
        for (long l : palindromes) {
            if (isPrime(l))  {
                System.out.println("Found " + l);
                return;
            }
        }
    }

}

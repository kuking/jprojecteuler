package uk.kukino.prjeuler;

import java.util.Arrays;

public class P77PrimeSummations {

    static boolean isPrime(int n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n%2 == 0 || n%3 == 0) return false;
        long sqrtN = (long)Math.sqrt(n)+1;
        for(long i = 6L; i <= sqrtN; i += 6) {
            if(n%(i-1) == 0 || n%(i+1) == 0) return false;
        }
        return true;
    }

    static int[] primes = new int[200];

    private static int multiply(final int[] primes, final int[] mult, final int limit) {
        int r = 0;
        for (int i=mult.length-1; i>=0; i--) {
            if (mult[i]!=0) {
                r += mult[i] * primes[i];
                if (r>limit) return -1;
            }
        }
        return r;
    }

    private static boolean add(final int[] maxs, int[] mult) {
        int idx = 0;
        while (idx<mult.length) {
            mult[idx]++;
            if (mult[idx]<=maxs[idx]) {
                return false;
            } else {
                mult[idx]=0;
            }
            idx++;
        }
        return true;
    }

    private static int waysToCount(int n) {

        int upper = 0;
        for (int i=0; i<200; i++) {
            if (primes[i]>n) {
                upper = i;
                break;
            }
        }
        System.out.println("Upper bound for " +n + " is the " + upper + "n prime " + primes[upper-1]);

        int[] maxs = new int[upper];
        int[] mult = new int[upper];
        int[] prim = Arrays.copyOf(primes, upper);

        for (int i=0; i<upper; i++) {
            maxs[i] = (n + 1)/ primes[i];
        }

        System.out.println(toSt(prim));
        System.out.println(toSt(maxs));
        System.out.println(toSt(mult));

        int count = 0;
        while (!add(maxs, mult)) {
            int m = multiply(prim, mult, n);
            //System.out.println(toSt(mult) + "   " + m + "   > " + count);
            if (m == n) {
                count++;
                if (count>2500 && count%25==0) {
                    System.out.println(toSt(mult) + "   " + m + "   > " + count);
                }
            }
        }
        return count;
    }

    private static String toSt(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<arr.length; i++) {
            sb.append(String.format("%3d", arr[i])).append(" ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        int idx = 0;
        int n = 2;
        while (idx<200) {
            if (isPrime(n)) primes[idx++]=n;
            n++;
        }

        int bestN = 0;
        int bestC = 0;
        for (int i=71; i<500; i++) {
            int counts = waysToCount(i);
            if (bestC < counts) {
                bestC = counts;
                bestN = i;
            }
            System.out.println("Ways to count " + i + " is " + counts + "  best so far: " + bestN + " with " + bestC + " numbers");
            if (bestC >= 5000) {
                return;
            }
        }

    }

}

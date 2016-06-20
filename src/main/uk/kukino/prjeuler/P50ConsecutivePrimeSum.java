package uk.kukino.prjeuler;

import java.util.ArrayList;
import java.util.List;

public class P50ConsecutivePrimeSum {

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

    static List<Integer> primes = new ArrayList<>();

    public static void main(String[] args) {

        int targetSum = 1000000;

        for (int i=1; i<targetSum; i++) {
            if (isPrime(i)) primes.add(i);
        }
        System.out.println("Primes Done - " + primes.size());

        int bestI = 0;
        int bestQty = 0;
        for (int i=0; i<1000; i++) { /// 1k is already too big
            int thisQty = count(i, targetSum);
            if (thisQty > bestQty) {
                bestQty = thisQty;
                bestI = i;
            }
        }

        System.out.println("---------");
        System.out.println("Best starting position is " + bestI + " with " + bestQty + " digits");
        count(bestI, targetSum); // again so outputs the actual value
    }

    private static Integer count(int idx, int target) {
        int bestPrime = 0;
        int bestPrimeQty = 0;

        int sum = 0;
        for (int c = idx; c<primes.size(); c++) {
            int thisPrime = primes.get(c);
            sum = sum + thisPrime;
            if (sum > target) {
                System.out.println("Best prime sum starting in position " + idx + " is " + bestPrime + " with " + bestPrimeQty + " numbers.");
                return bestPrimeQty;
            }
            if (primes.contains( sum ) ) {
                bestPrime = sum ;
                bestPrimeQty = c - idx + 1;
            }
        }
        return bestPrimeQty;
    }

}

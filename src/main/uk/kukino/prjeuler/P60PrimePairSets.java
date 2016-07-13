package uk.kukino.prjeuler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class P60PrimePairSets {

    static final int PRIMES_TO_CHECK = 1200;
    static final int PRIMES_TO_CONCATENATE = 5;

    static int[] primes = new int[PRIMES_TO_CHECK];

    static LoadingCache<Integer, Boolean> isPrime = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Integer, Boolean>() {
                @Override
                public Boolean load(Integer n) throws Exception {
                    if(n < 2) return false;
                    if(n == 2 || n == 3) return true;
                    if(n%2 == 0 || n%3 == 0) return false;
                    long sqrtN = (long)Math.sqrt(n)+1;
                    for(long i = 6L; i <= sqrtN; i += 6) {
                        if(n%(i-1) == 0 || n%(i+1) == 0) return false;
                    }
                    return true;
                }
            });

    static LoadingCache<Pair<Integer,Integer>, Boolean> twoPrimesSatisfy = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Pair<Integer, Integer>, Boolean>() {
                @Override
                public Boolean load(Pair<Integer, Integer> key) throws Exception {
                    return twoPrimesSatisfy(key.getLeft(), key.getRight());
                }
            });

    public static void main(String[] args) {

        long startMs = System.currentTimeMillis();

        int idx = 0;
        int n = 2;
        while (idx<primes.length) {
            if (isPrime.getUnchecked(n)) {
                primes[idx++]=n;
            }
            n++;
        }
        System.out.println("Biggest prime to check #" + primes.length + " is " + primes[primes.length-1]);

        int[] pairset = new int[PRIMES_TO_CONCATENATE];
        for (int i=0; i<pairset.length; i++) pairset[i]=i;

        do {

            // optimisation
            outter:
            for (int a=0; a<pairset.length-2; a++) {
                for (int b=a+1; b<pairset.length-1; b++) {
                    while (!twoPrimesSatisfy.getUnchecked(new ImmutablePair<>(primes[pairset[a]], primes[pairset[b]]))) {
                        if (incNoDupe(pairset, primes.length, b)) return;
                        break outter;
                    }
                }
            }

            if (satisfy(pairset)) {
                System.out.print("Found! ");
                int sum = 0;
                for (int i=0; i<pairset.length; i++) {
                    System.out.print(primes[pairset[i]] + " ");
                    sum += primes[pairset[i]];
                }
                System.out.println("and the sum is " + sum);
                System.out.println("took " + (System.currentTimeMillis() - startMs) + "ms");
                return;
            }

        } while (!incNoDupe(pairset, primes.length, pairset.length-1));
        System.out.println("No Luck, try to increasing PRIMES_TO_CHECK");
    }

    // return true if overflow
    private static boolean incNoDupe(int[] arr, int max, int p) {
        while (p>0) {
            arr[p]++;
            if (arr[p]>=max) {
                p--;
                while (arr[p] + (arr.length-p) >= max) {
                    p--;
                    if (p<0) { return true; }
                }
                arr[p]++;
                for (int i=p+1; i<arr.length; i++) arr[i] = arr[i-1] + 1;
                if (arr[p] < max) return false;
            } else {
                return false;
            }
        }
        return p==0;
    }

    private static boolean twoPrimesSatisfy(final int prime1, final int prime2) {
        String sprime1 = Integer.toString(prime1);
        String sprime2 = Integer.toString(prime2);
        return isPrime.getUnchecked( Integer.parseInt (sprime1 + sprime2 ))
            && isPrime.getUnchecked( Integer.parseInt (sprime2 + sprime1 ));
    }

    private static boolean satisfy(int[] pairset) {
        for (int a=0; a<pairset.length; a++) {
            int pa = primes[pairset[a]];
            String sa = Integer.toString(pa);
            for (int b=a+1; b<pairset.length; b++) {
                int pb = primes[pairset[b]];
                String sb = Integer.toString(pb);
                if (!isPrime.getUnchecked( Integer.parseInt(sa + sb) )) return false;
                if (!isPrime.getUnchecked( Integer.parseInt(sb + sa) )) return false;
            }
        }
        return true;
    }

}

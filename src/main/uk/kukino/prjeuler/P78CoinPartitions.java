package uk.kukino.prjeuler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class P78CoinPartitions {

    static LoadingCache<Integer, BigInteger> Ps = CacheBuilder.newBuilder()
            .concurrencyLevel(1)
            .recordStats()
            .build(new CacheLoader<Integer, BigInteger>() {
                @Override
                public BigInteger load(Integer n) throws Exception {
                    if (n<0) return BigInteger.ZERO;
                    if (n==1 || n==0) return BigInteger.ONE;
                    BigInteger res = BigInteger.ZERO;
                    int k = 1;
                    // Pentagonal
                    while (true) {
                        int gk = (k * (3*k -1 )) / 2;
                        if (n<gk) return res;
                        res = (k%2==0)? res.subtract(Ps.get(n-gk)) : res.add(Ps.get(n-gk));
                        if (k<0) { // 1, -1, 2, -2, 3, -3, 4, -4
                            k = Math.abs(k) + 1;
                        } else {
                            k = -k;
                        }
                    }
                }
            });

    public static void main(String[] args) throws ExecutionException {

        BigInteger divider = BigInteger.valueOf(1000000);

        for (int i=1; i<10000000; i++) {
            int n = 5*i + 4; // Srinivasa congruence p(5k+4) === 0 mod 5 ... and 1000000 === 5 mod 5
            if ( (n-5) % 7 == 0 || (n-6) % 11 == 0 ) { // and skipping p(7k+5) and p(11k+6) as 1000000 is not div by 11 or 7
                // skip
            } else {
                BigInteger p = Ps.get(n);
                if (n>10 && p.remainder(divider) == BigInteger.ZERO) {
                    System.out.println("Final " + Ps.stats().toString());
                    System.out.println("Found! p(" + n + ") = " + p);
                    System.exit(0);
                }
                if (i % 1000 == 0) {
                    System.out.println("p(" + n + ") = " + p.toString());
                }
            }
        }
    }

}

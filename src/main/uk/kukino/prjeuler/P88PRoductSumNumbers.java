package uk.kukino.prjeuler;

import java.util.Arrays;

public class P88ProductSumNumbers {

    static int[] kval = new int[12000];

    public static void main(String[] args) {
        int[] factors = new int[12]; // no more than 12 factors required
        factors[0]=2;
        factors[1]=2;
        int tail = 2;
        int curr = 1;
        int iters = 0;

        while (tail != factors.length) {
            iters++;
            // System.out.println(ArrayUtils.toString(factors) + " c:" + curr + " t:" + tail);

            // calculates current factor sum/prod
            int sum = 0;
            int prod = 1;
            for (int i=0; i<tail; i++) {
                sum += factors[i];
                prod *= factors[i];
            }

            // then, either account for it
            if (prod < 12300) {
                int k = prod - sum + tail;
                if (k < kval.length && (kval[k] == 0 || prod < kval[k])) {
                    kval[k] = prod;
                    // System.out.println(ArrayUtils.toString(factors) + " prod=" + prod + "  sum=" + sum + "  k=" + k);
                }
                factors[curr]++;
            } else {
                // or clean backwards as we went over the limit (upper limit of 12300)
                if (curr==0) {
                    // if it cant go back anymore, increase the number of factors
                    tail++;
                    for (int i=0; i<tail; i++) factors[i] = 2;
                    curr = tail-1; // and start again from the last one to inc
                } else {
                    if (factors[curr-1] == factors[curr]) {
                        // if prev factor == this one, it means go further back
                        curr--;
                    } else {
                        // otherwise, just increase previous and reset all following and start again
                        factors[curr - 1]++;
                        for (int i=curr; i<tail; i++) factors[i] = factors[curr-1];
                        curr = tail -1;
                    }
                }
            }
        }

        System.out.println(Arrays.stream(kval).distinct().sum() + " - solved exploring " + iters + " factors.");
    }

}

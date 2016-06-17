package uk.kukino.prjeuler;

public class P76CountingSummations {

    public static long countSummation(int n, int currMax, int currSum) {
        long tailCount = 0;
        for (int localMax = currMax; localMax > 0; localMax--) {
            if (currSum + localMax == n && localMax != n) {
                tailCount++; // base
            } else if (currSum + localMax < n) {
                tailCount += countSummation(n, localMax, currSum + localMax);
            }
        }
        return tailCount;
    }

    public static void main(String[] args) {

        System.out.println("Counting summation for 100 is " + countSummation(100, 100, 0));

        for (int n=1; n<=100; n++) {
            System.out.println("Counting summation for " + n + " is: " + countSummation(n, n, 0));
        }
    }

}

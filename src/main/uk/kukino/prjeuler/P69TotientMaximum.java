package uk.kukino.prjeuler;

public class P69TotientMaximum {

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

    private static int gcd(int a, int b) {
        if (b==0) return a;
        return gcd(b, a % b);
    }

    private static int phi(int n) {
        if (isPrime(n)) {  // optimisation
            return n-1;
        }
        int res = 0;
        for (int i=0; i<n; i++) {
            if (gcd(n, i) == 1) {
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int maxTotient = 1000000;
        int knownTotients = 0;
        int[] totients = new int[maxTotient+1];

        System.out.println("Calculating Totients ...");
        int currBestTotient = 0;
        float currBestNPhi = Float.MIN_VALUE;

        for (int n=2; n<=maxTotient; n++) {
            int phi;
            if (totients[n]==0) {
                phi = phi(n);
                totients[n] = phi;
                knownTotients++;
                // phi(nm)==phi(n) * phi(m) iff gcd(n,m)==1,  so it tries to multiply (cheaper) than than phi(n)
                for (int nn=2; nn*n < maxTotient; nn++) {
                    if (nn*n < maxTotient && totients[nn*n]==0 && totients[nn]!=0 && gcd(nn, n)==1) {
                        totients[nn*n] = totients[nn] * totients[n];
                        knownTotients++;
                    }
                }
            } else {
                phi = totients[n];
            }
            float nphi = (float) n / phi;

            if (currBestNPhi < nphi) {
                currBestNPhi = nphi;
                currBestTotient = n;
            }

            if (n%100000==0) {
                System.out.println("actual=" + n + ", knownTotients=" + knownTotients + " or " + (float)knownTotients/maxTotient);
            }
        }

        System.out.println("Totiem maximum for " + maxTotient + " is " + currBestTotient + " with n/phi: " + currBestNPhi);
    }

}

package uk.kukino.prjeuler;

import java.math.BigInteger;

public class P97LargeNonMersennePrime {

    public static void main(String[] args) {
        BigInteger prime = (BigInteger.valueOf(28433).multiply( BigInteger.valueOf(2).pow(7830457) )).add( BigInteger.ONE );
        System.out.println("... " + prime.toString().substring(2357180));
    }

}


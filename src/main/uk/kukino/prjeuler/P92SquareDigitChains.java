package uk.kukino.prjeuler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class P92SquareDigitChains {

    static LoadingCache<Integer,Integer> SD = CacheBuilder.newBuilder()
            .recordStats()
            .build(new CacheLoader<Integer, Integer>() {
                @Override
                public Integer load(Integer i) throws Exception {
                    if (i==1) return 1;
                    if (i==89) return 89;

                    int sq = 0;
                    for( char ch : i.toString().toCharArray()) {
                        sq += (ch-48) * (ch-48);
                    }
                    return SD.get(sq);
                }
            });


    public static void main(String[] args) {
        int count89 = 0;
        for (int i=1; i<10000000; i++) {
            if (SD.getUnchecked(i) == 89) count89++;
        }
        System.out.println("There are " + count89 + " counting to 89 under 10000000");
        System.out.println(SD.stats() + " entries: " + SD.size());
    }
}

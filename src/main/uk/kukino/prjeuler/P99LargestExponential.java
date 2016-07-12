package uk.kukino.prjeuler;

import com.google.common.io.LineReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class P99LargestExponential {

    public static void main(String[] args) throws IOException {

        InputStream in =  new P99LargestExponential().getClass().getResourceAsStream("p99_base_exp.txt");
        LineReader lr = new LineReader(new InputStreamReader(in));

        String line = lr.readLine();
        int lineNo= 1;
        int bestLineNo = 0;
        double bestValue = 0d;
        while (line != null) {
            String[] numbers = line.split(",");
            int base = Integer.parseInt(numbers[0]);
            int exp = Integer.parseInt(numbers[1]);
            // a^b > c^d <=> log(a^b) > log (c^d) <=> a*log(b) > c*log(d)
            double thisValue = exp * Math.log(base);
            if (thisValue > bestValue) {
                bestLineNo = lineNo;
                bestValue = thisValue;
            }
            line = lr.readLine();
            lineNo++;
        }
        System.out.println("Biggest value is #" + bestLineNo);
    }

}

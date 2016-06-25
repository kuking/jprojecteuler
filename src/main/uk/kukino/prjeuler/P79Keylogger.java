package uk.kukino.prjeuler;

import java.util.HashSet;
import java.util.Set;

public class P79Keylogger {

    static final String[] keys = {"319", "680", "180", "690", "129", "620", "762", "689", "762", "318", "368", "710",
             "720", "710", "629", "168", "160", "689", "716", "731", "736", "729", "316", "729", "729", "710", "769",
             "290", "719", "680", "318", "389", "162", "289", "162", "718", "729", "319", "790", "680", "890", "362",
             "319", "760", "316", "729", "380", "319", "728", "716"};

    public static class Order {
        public int number;
        public Set<Integer> predecessors;
        public Order(int number) {
            this.number = number;
            this.predecessors = new HashSet<>();
        }
    }

    public static int withoutPredecessorExcluding(final Order[] orders, final Set<Integer> alreadyIn) {
        for (int i=0; i<10; i++) {
            if (orders[i].predecessors.isEmpty() && !alreadyIn.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    private static void removeFromPredecessors(Order[] orders, final int wop) {
        for (int i=0; i<10; i++) {
            orders[i].predecessors.remove(wop);
        }
    }

    public static void main(String[] args) {

        Order[] orders = new Order[10];
        for (int i=0; i<10; i++) orders[i] = new Order(i);

        Set<Integer> already = new HashSet<>();
        for (int i=0; i<10; i++) already.add(i);

        for (String key : keys) {
            int a = key.charAt(0)-48;
            int b = key.charAt(1)-48;
            int c = key.charAt(2)-48;
            orders[b].predecessors.add(a);
            orders[c].predecessors.add(a);
            orders[c].predecessors.add(b);
            already.remove(a);
            already.remove(b);
            already.remove(c);
        }

        while (withoutPredecessorExcluding(orders, already) != -1 ) {
            int wop = withoutPredecessorExcluding(orders, already);
            already.add(wop);
            System.out.print(wop + " ");
            removeFromPredecessors(orders, wop);
        }

    }
}

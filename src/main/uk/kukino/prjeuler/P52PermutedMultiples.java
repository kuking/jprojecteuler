package uk.kukino.prjeuler;

import java.util.ArrayList;
import java.util.List;

public class P52PermutedMultiples {

   public static void main(String[] args) {
      long n = 1;
      while (true) {
         if (check(n)) {
            System.out.println("Found! x=" + n + " 2x=" + n*2 + " 3x="+ n*3 + " 4x="+ n*4 + " 5x="+ n*5 + " 6x="+ n*6);
             System.exit(0);
         }
         n++;
      }
   }

   private static boolean check(long n) {
      String sn1 = Long.toString(n);
      String sn6 = toStringIfSameLength(sn1, n*6); if (sn6==null) return false;
      if (!allCommonDigits(sn1, sn6)) return false;
      String sn2 = toStringIfSameLength(sn1, n*2); if (sn2==null) return false;
      if (!allCommonDigits(sn1, sn2)) return false;
      String sn4 = toStringIfSameLength(sn1, n*4); if (sn4==null) return false;
       if (!allCommonDigits(sn1, sn4)) return false;
      String sn3 = toStringIfSameLength(sn1, n*3); if (sn3==null) return false;
       if (!allCommonDigits(sn1, sn3)) return false;
      return true;
   }

   private static boolean allCommonDigits(String a, String b) {
      List<Character> chars = new ArrayList<>();
      for (int i=0; i<a.length(); i++) chars.add(a.charAt(i));
      for (int i=0; i<b.length(); i++) {
         if (!chars.remove((Character) b.charAt(i) )) {
            return false;
         }
      }
      return chars.isEmpty();
   }

   private static String toStringIfSameLength(String sn, long l) {
      String ret = Long.toString(l);
      if (ret.length()!=sn.length()) {
         return null;
      }
      return ret;
   }


}

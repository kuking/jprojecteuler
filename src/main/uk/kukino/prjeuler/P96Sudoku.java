package uk.kukino.prjeuler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.io.LineReader;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class P96Sudoku {

    public static class SuPuzzle {

        byte[] val = new byte[9*9];
        int[] possible = new int[9*9];

        public SuPuzzle(LineReader lr) throws IOException {
            for (byte r=0; r<9; r++) {
                String line = lr.readLine();
                for (byte c=0; c<9; c++) {
                    set(r,c, (byte)(line.charAt(c) - '0'));
                }
            }
        }

        public SuPuzzle(SuPuzzle orig) {
            this.val = Arrays.copyOf(orig.val, 9*9);
        }

        public void set(byte row, byte col, byte value) {
            val[row*9+col] = value;
        }

        public byte get(byte row, byte col) {
            return val[row*9+col];
        }

        @Override
        public String toString() {
            int freedom = 0;
            StringBuilder sb = new StringBuilder(90);
            for (byte row = 0; row<9; row++) {
                if (row==3 || row==6)
                    sb.append("---+---+---   ---+---+---\n");
                for (byte col = 0; col<9; col++) {
                    sb.append(get(row, col));
                    if (col==2 || col==5)
                        sb.append('|');
                }
                sb.append("   ");
                for (byte col = 0; col<9; col++) {
                    sb.append(getPossiblesCount(row, col));
                    freedom += getPossiblesCount(row, col);
                    if (col==2 || col==5)
                        sb.append('|');
                }
                if (row == 8) sb.append(" #Free: " + freedom);
                sb.append('\n');
            }
            ;
            return sb.toString();
        }

        public int puzzleHash() {
            int hash = 7;
            for (int i=0; i<val.length; i++) hash = hash*33 + val[i];
            return hash;
        }

        public void setPossible(final byte row, final byte col, final byte number, final boolean val) {
            int pv = possible[row*9+col];
            if (val) {
                pv |= (1 << number);
            } else {
                pv &= ~(1 << number);
            }
            possible[row*9+col] = pv;
        }

        public boolean isPossible(final byte row, final byte col, final byte number) {
            int pv = possible[row*9+col];
            return (pv & (1 << number)) > 0;
        }

        public int getPossiblesCount(final byte row, final byte col) {
            return Integer.bitCount(possible[row*9+col]);
        }

        public boolean isNumberInRow(final byte number, final byte row) {
            for (int i=row*9; i<row*9+9; i++) {
                if (val[i] == number) return true;
            }
            return false;
        }

        public boolean isNumberInCol(final byte number, final byte col) {
            for (int i=col; i< 9*9 ; i=i+9) {
                if (val[i] == number) return true;
            }
            return false;
        }

        private boolean isNumberInBlock(final byte number, final byte row, final byte col) {
            byte rows = (byte)((row/3)*3);
            byte cols = (byte)((col/3)*3);
            for (byte r = rows; r<rows+3; r++) {
                for (byte c = cols; c<cols+3; c++) {
                    if (get(r,c) == number) return true;
                }
            }
            return false;
        }

        private boolean isNumberPossibleInRow(final byte num, final byte row) {
            for (byte col=0; col<9; col++) {
                if (isPossible(row, col, num)) return true;
            }
            return false;
        }

        private boolean isNumberPossibleInCol(final byte num, final byte col) {
            for (byte row=0; row<9; row++) {
                if (isPossible(row, col, num)) return true;
            }
            return false;
        }

        private boolean isNumberPossibleInBlock(final byte number, final byte row, final byte col) {
            byte rows = (byte)((row/3)*3);
            byte cols = (byte)((col/3)*3);
            for (byte r = rows; r<rows+3; r++) {
                for (byte c = cols; c<cols+3; c++) {
                    if (isPossible(r, c, number)) return true;
                }
            }
            return false;
        }

        public void reEvaluatePossibles() {
            Arrays.fill(possible, 0);
            for (byte row = 0; row<9; row++) {
                for (byte col = 0; col<9; col++) {
                    if (get(row, col)==0) {
                        for (byte num = 1; num<10; num++) {
                            if (!isNumberInRow(num, row) && !isNumberInCol(num, col) && !isNumberInBlock(num, row, col)) {
                                setPossible(row, col, num, true);
                            }
                        }
                    }
                }
            }
        }

        public List<Pair<Byte,Byte>> getCoordsWithOneOption() {

            List<Pair<Byte,Byte>> results = Lists.newArrayList();
            for (byte row = 0; row<9; row++) {
                for (byte col = 0; col<9; col++) {
                    if (get(row, col) == 0 && getPossiblesCount(row, col) == 1) {
                        results.add(new ImmutablePair<>(row, col));
                    }
                }
            }
            return results;
        }

        public List<Pair<Byte,Byte>> getCoordsWithLessRelatedPossibilitiesFirst() {
            List<Pair<Byte,Byte>> results = Lists.newArrayList();
            for (byte row = 0; row<9; row++) {
                for (byte col = 0; col<9; col++) {
                    if (get(row, col) == 0 && getPossiblesCount(row, col) > 0) {
                        results.add(new ImmutablePair<>(row, col));
                    }
                }
            }
            results.sort((p1, p2) -> {
                int c1 = getPossiblesCount(p1.getLeft(), p1.getRight());
                int c2 = getPossiblesCount(p2.getLeft(), p2.getRight());
                return c1 - c2;
            });
            return results;
        }

        public boolean isComplete() {
            for (int i=0; i<9*9; i++) if (val[i]==0) return false;
            return true;
        }


        public boolean isFeasible() {
            // every row has the possibility to hold every number
            for (byte row=0; row<9; row++) {
                for (byte num = 1; num < 10; num++) {
                    if (!isNumberInRow(num, row) && !isNumberPossibleInRow(num, row)) {
                        return false;
                    }
                }
            }
            // ditto cols
            for (byte col=0; col<9; col++) {
                for (byte num = 1; num < 10; num++) {
                    if (!isNumberInCol(num, col) && !isNumberPossibleInCol(num, col)) {
                        return false;
                    }
                }
            }
            // ditto block
            for (byte row=0; row<9; row+=3) {
                for (byte col=0; col<9; col+=3) {
                    for (byte num = 1; num < 10; num++) {
                        if (!isNumberInBlock(num, row, col) && !isNumberPossibleInBlock(num, row, col)) {
                            return false;
                        }
                    }
                }
            }
            // and every empty place has something that can be put into
            for (byte row=0; row<9; row++) {
                for (byte col=0; col<9; col++) {
                    if (get(row, col) == 0 && getPossiblesCount(row, col) == 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        static Cache<Integer, Boolean> TRIED_HASHES = CacheBuilder.newBuilder().recordStats().build();

        private boolean autoPlayHiddenSinglesInRowsAndCols() {
            int total = 0;

            // if one number can only be played in ONLY one cell in the row (even if that cell is shared with other
            // possible candidates), it will auto play it as there is no alternative. (but only if it is a legal move.)
            for (byte row = 0; row<9; row++) {
                int count[] = new int[10];
                for (byte col = 0; col<9; col++) {
                    for (byte n = 1; n < 10; n++) {
                        if (isPossible(row, col, n)) {
                            count[n]++;
                        }
                    }
                }
                for (byte n = 1; n<10; n++) {
                    if (count[n]==1) {
                        for (byte col=0; col<9; col++) {
                            if (isPossible(row, col, n) && isValidWith(n, row, col)) {
                                set(row, col, n);
                                total++;
                                break;
                            }
                        }
                    }
                }
            }

            // idem cols
            for (byte col = 0; col<9; col++) {
                int count[] = new int[10];
                for (byte row = 0; row<9; row++) {
                    for (byte n = 1; n < 10; n++) {
                        if (isPossible(row, col, n)) count[n]++;
                    }
                }
                for (byte n = 1; n<10; n++) {
                    if (count[n]==1) {
                        for (byte row=0; row<9; row++) {
                            if (isPossible(row, col, n) && isValidWith(n, row, col)) {
                                set(row, col, n);
                                total++;
                                break;
                            }
                        }
                    }
                }
            }

            //XXX: missing blocks, but still finds the solution.
            return total > 0;
        }

        private boolean isValidWith(final byte num, final byte row, final byte col) {
            // applying n into row/col, would make the row/col/block still valid?
            if (get(row, col) != 0) return false;
            if (isNumberInRow(num, row)) return false;
            if (isNumberInCol(num, col)) return false;
            if (isNumberInBlock(num, row, col)) return false;
            return true;
        }

        public boolean validate() {
            for (byte row = 0; row<9; row++) {
                for (byte n = 1; n<10; n++) {
                    if (!isNumberInRow(n, row)) {
                        System.out.println(n + " is not in row " + (row + 1));
                        return false;
                    }
                }
            }
            for (byte col = 0; col<9; col++) {
                for (byte n = 1; n<10; n++) {
                    if (!isNumberInCol(n, col)) {
                        System.out.println(n + " is not in col " + (col+1));
                        return false;
                    }
                }
            }
            for (byte row = 0; row<9; row+=3) {
                for (byte col = 0; col<9; col+=3) {
                    for (byte n = 1; n<10; n++) {
                        if (!isNumberInBlock(n, row, col)) {
                            System.out.println(n + " is not in block " + (row+1) + "x" + (col+1));
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public SuPuzzle solve() {

            // base case
            if (isComplete()) return this; // found!

            SuPuzzle child = new SuPuzzle(this);
            child.reEvaluatePossibles();
            if (!child.isFeasible()) return null;

            // #1 first, tries to sort out the hidden plays
            if (child.autoPlayHiddenSinglesInRowsAndCols()) {
                child.reEvaluatePossibles();
                if (!child.isFeasible()) return null;
            }

            // #2 then, does trivial moves and iterates sorting out hidden plays
            // ... until no hidden-play or trivial move is available
            List<Pair<Byte,Byte>> possibles = child.getCoordsWithOneOption();

            boolean doingTrivialPlay = true;
            while (doingTrivialPlay) {
                doingTrivialPlay = false;

                Iterator<Pair<Byte,Byte>> iter = possibles.iterator();
                while (iter.hasNext()) {
                    Pair<Byte, Byte> focus = iter.next();
                    byte row = focus.getLeft();
                    byte col = focus.getRight();

                    if (child.getPossiblesCount(row, col) == 1) {
                        for (byte num=1; num<10; num++) {
                            if (child.isPossible(row, col, num) && child.isValidWith(num, row, col)) {
                                child.set(row, col, num);
                                break;
                            }
                        }
                        iter.remove();
                        doingTrivialPlay = true;
                    } else {
                        break;
                    }
                }

                if (doingTrivialPlay) {
                    child.reEvaluatePossibles();
                    child.autoPlayHiddenSinglesInRowsAndCols();
                    if (child.isComplete()) return child; // found!
                    child.reEvaluatePossibles();
                    if (!child.isFeasible()) return null;
                    possibles = child.getCoordsWithOneOption();
                }
            }

            // exploration limit -- to bail-out on excess search
            if (TRIED_HASHES.stats().requestCount() > 100000) {
                return child;
            }

            // #3 finally, exhaustive & recursive search
            // uses hashes for not repeating puzzle configurations in different parts of the branches

            child.reEvaluatePossibles();
            possibles = child.getCoordsWithLessRelatedPossibilitiesFirst();

            for (Pair<Byte,Byte> focus : possibles) {
                byte row = focus.getLeft();
                byte col = focus.getRight();
                for (byte num=1; num<10; num++) {
                    if (child.isPossible(row, col, num) && child.isValidWith(num, row, col)) {
                        child.set(row, col, num);
                        final int hash = child.puzzleHash();
                        if (TRIED_HASHES.getIfPresent(hash) == null) {
                            SuPuzzle childResult = child.solve();
                            if (childResult != null) {
                                return childResult; // solved!
                            } else {
                                TRIED_HASHES.put(hash, Boolean.TRUE);
                            }
                        }
                    }
                }
                child.set(row, col, (byte)0);
            }

            return null;
        }
    }



    public static void main(String[] args) throws IOException {

        long result = 0;
        int count = 1;
        int completedCount = 0;

        InputStream in =  new P96Sudoku().getClass().getResourceAsStream("p96_sudoku.txt");
        LineReader lr = new LineReader(new InputStreamReader(in));

        String line = lr.readLine();
        while (line != null) {

            SuPuzzle.TRIED_HASHES = CacheBuilder.newBuilder().recordStats().build(); // create new instead of invalidate so starts get reset.

            System.out.println(line);
            SuPuzzle su = new SuPuzzle(lr);

            if (count == 44 || true) {
                System.out.println(su);
                SuPuzzle solved = su.solve();
                solved.reEvaluatePossibles();
                System.out.println(solved);
                System.out.println(SuPuzzle.TRIED_HASHES.stats());

                if (!solved.validate()) {
                    System.out.println("MEHHHHHH -- not solved");
                    System.exit(-1);
                }

                if (solved.isComplete()) {
                    result += (solved.get((byte)0,(byte)0)*100 + solved.get((byte)0,(byte)1)*10 + solved.get((byte)0,(byte)2));
                    System.out.println("Valid and Complete.\n");
                    completedCount++;
                }
                System.out.println("---------------------------------------------------------------------------");
            }

            line = lr.readLine(); // skip header
            count++;
        }

        System.out.println("Completed " + completedCount + " out of " + (count-1) + " answer is: " + result);
    }

}

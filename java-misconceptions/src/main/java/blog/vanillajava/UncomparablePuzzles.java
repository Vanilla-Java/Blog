package blog.vanillajava;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Illustrates puzzles from the post "Uncomparable puzzles in Java" by Peter Lawrey.
 * https://blog.vanillajava.blog/2014/05/uncomparable-puzzles-in-java.html
 */
public class UncomparablePuzzles {

    public static void main(String[] args) {
        puzzlingLongAndDoubleComparison();
        puzzlingDoubleAndLongConversion();
        puzzlingDoubleComparison();
        puzzlingBigDecimalComparison();
        shrinkingCollectionsPuzzle();
    }

    /**
     * Puzzle 1: Demonstrates long and double comparison behavior.
     */
    private static void puzzlingLongAndDoubleComparison() {
        long a = (1L << 54) + 1;
        double b = a;

        System.out.println("b == a is " + (b == a));             // true
        System.out.println("(long) b < a is " + ((long) b < a)); // true

        // Additional cases
        double bRounded = (double) ((long) b);
        System.out.println("bRounded == a is " + (bRounded == a));  // true
        System.out.println("Precision lost: b - (long) b = " + (b - (long) b)); // Displays no loss of precision
    }

    /**
     * Puzzle 2: Demonstrates rounding behavior when assigning a large double to a long.
     */
    private static void puzzlingDoubleAndLongConversion() {
        double c = 1e19;
        long d = 0;
        d += c;

        System.out.println("\nd < c is " + (d < c));             // true
        System.out.println("d < (long) c is " + (d < (long) c)); // false

        // Additional cases
        System.out.println("Value of c as long: " + (long) c);         // Conversion of c to long truncates
        System.out.println("Difference between d and c: " + (c - d));  // Shows the difference after assignment
    }

    /**
     * Puzzle 3: Demonstrates comparison between Double objects with special handling for NaN values.
     */
    private static void puzzlingDoubleComparison() {
        Double e = -0.0;
        Double f = 0.0;

        System.out.println("\ne <= f is " + (e <= f));           // true
        System.out.println("e >= f is " + (e >= f));             // true
        System.out.println("e == f is " + (e == f));             // false
        System.out.println("e - f == 0 is " + (e - f == 0));     // true
        System.out.println("e != f is " + (e != f));             // true
        System.out.println("e.equals(f) is " + e.equals(f));     // false
        System.out.println("e.compareTo(f) is " + e.compareTo(f)); // -1, as -0.0 is considered less than 0.0

        // Additional insight: Comparing with Double.NaN
        Double g = Double.NaN;
        System.out.println("g == g is " + (g == g)); // as per IEEE 754, NaN is not equal to itself
        System.out.println("g.equals(g) is " + g.equals(g));    // false, NaN is not equal to itself
        System.out.println("g.compareTo(g) is " + g.compareTo(g)); // 0 in compareTo, which can be counterintuitive
        System.out.println("g.compareTo(0.0) is " + g.compareTo(0.0)); // 1 in compareTo, which can be counterintuitive
        System.out.println("g.compareTo(Inf) is " + g.compareTo(Double.POSITIVE_INFINITY)); // 1 in compareTo, which can be counterintuitive
    }

    /**
     * Puzzle 4: Demonstrates comparison between BigDecimal instances and highlights nuances in equality.
     */
    private static void puzzlingBigDecimalComparison() {
        BigDecimal x = new BigDecimal("0.0");
        BigDecimal y = BigDecimal.ZERO;

        System.out.println("\nx == y is " + (x == y));                                  // false
        System.out.println("x.doubleValue() == y.doubleValue() is " + (x.doubleValue() == y.doubleValue())); // true
        System.out.println("x.equals(y) is " + x.equals(y));                            // false
        System.out.println("x.compareTo(y) == 0 is " + (x.compareTo(y) == 0));          // true

        BigDecimal z = new BigDecimal("0.00");
        System.out.println("x.equals(z) is " + x.equals(z));            // false due to different scale
        System.out.println("x.compareTo(z) == 0 is " + (x.compareTo(z) == 0)); // true because values are numerically equal
    }

    /**
     * Bonus Puzzle: Demonstrates how BigDecimal elements in collections are affected by equality and comparison rules.
     */
    private static void shrinkingCollectionsPuzzle() {
        List<BigDecimal> bds = Arrays.asList(
                new BigDecimal("1"),
                new BigDecimal("1.0"),
                new BigDecimal("1.00"),
                BigDecimal.ONE
        );
        System.out.println("\nbds.size()=    " + bds.size());           // Outputs 4

        Set<BigDecimal> bdSet = new HashSet<>(bds);
        System.out.println("bdSet.size()=  " + bdSet.size());           // Outputs 3

        Set<BigDecimal> bdSet2 = new TreeSet<>(bds);
        System.out.println("bdSet2.size()= " + bdSet2.size());          // Outputs 1
    }
}

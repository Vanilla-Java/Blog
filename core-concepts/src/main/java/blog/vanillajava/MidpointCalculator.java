package blog.vanillajava;

public class MidpointCalculator {

    public static void main(String[] args) {
        int h = 102;
        int l = 99;

        System.out.println("Naive midpoint calculation: " + naiveMidpoint(h, l));
        System.out.println("Safe midpoint calculation: " + safeMidpoint(h, l));
        System.out.println("Midpoint with rounding down (>>>): " + midpointRoundDown(h, l));
        System.out.println("Midpoint with rounding half-up (>>> with +1): " + midpointRoundHalfUp(h, l));
    }

    /**
     * Naive midpoint calculation: risks overflow if h and l are large
     */
    public static int naiveMidpoint(int h, int l) {
        return (h + l) / 2;
    }

    /**
     * Safe midpoint calculation to avoid overflow
     */
    public static int safeMidpoint(int h, int l) {
        return l + (h - l) / 2;
    }

    /**
     * Midpoint calculation using unsigned right shift for rounding down
     */
    public static int midpointRoundDown(int h, int l) {
        return (h + l) >>> 1;
    }

    /**
     * Midpoint calculation using unsigned right shift for rounding half-up
     */
    public static int midpointRoundHalfUp(int h, int l) {
        return (h + l + 1) >>> 1;
    }
}

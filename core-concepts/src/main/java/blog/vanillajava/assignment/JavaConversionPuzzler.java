package blog.vanillajava.assignment;

public class JavaConversionPuzzler {

    public static void main(String[] args) {
        // Example 1: int with += 0.0f
        int i = Integer.MAX_VALUE;
        i += 0.0f;
        int j = i;
        System.out.println("For int:");
        System.out.println("j == Integer.MAX_VALUE: " + (j == Integer.MAX_VALUE)); // true
        System.out.println("j == Integer.MIN_VALUE: " + (j == Integer.MIN_VALUE)); // false

        // Example 2: long with += 0.0f
        long l = Integer.MAX_VALUE;
        l += 0.0f;
        int k = (int) l;
        System.out.println("\nFor long:");
        System.out.println("k == Integer.MAX_VALUE: " + (k == Integer.MAX_VALUE)); // false
        System.out.println("k == Integer.MIN_VALUE: " + (k == Integer.MIN_VALUE)); // true

        // Example 3: Char division causing a character change
        char ch = '0'; // '0' has ASCII code 48
        ch /= 0.9;     // dividing 48 by 0.9 ~= 53.3333, truncated to 53 = '5'
        System.out.println("\nChar division example:");
        System.out.println("ch after division: " + ch); // prints '5'
    }
}

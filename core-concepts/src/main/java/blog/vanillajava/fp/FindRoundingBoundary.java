package blog.vanillajava.fp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FindRoundingBoundary {
    public static final BigDecimal TWO = BigDecimal.valueOf(2);

    public static void main(String... args) {
        int digits = 80; // High precision to capture tiny differences
        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = BigDecimal.ONE;

        for (int i = 0; i <= 10 * digits / 3; i++) {
            BigDecimal mid = low.add(high)
                    .divide(TWO, digits, RoundingMode.HALF_UP);
            if (mid.equals(low) || mid.equals(high))
                break;
            if (Math.round(Double.parseDouble(mid.toString())) > 0)
                high = mid;
            else
                low = mid;
        }

        System.out.println("Math.round(" + low
                + ", as double " + low.doubleValue()
                + " or " + Double.toHexString(low.doubleValue()) + ") = "
                + Math.round(Double.parseDouble(low.toString())));
        System.out.println("Math.round(" + high
                + ", as double " + high.doubleValue()
                + " or " + Double.toHexString(high.doubleValue()) + ") = "
                + Math.round(Double.parseDouble(high.toString())));
    }
}

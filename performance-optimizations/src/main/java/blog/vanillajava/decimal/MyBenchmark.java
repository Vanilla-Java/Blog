package blog.vanillajava.decimal;

import com.epam.deltix.dfp.Decimal64;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/* RUn on a Ryzen 5950X running Linux with Azul JDK v23
Benchmark                                          Mode  Cnt        Score        Error  Units
MyBenchmark.bigDecimalMidPriceDivide              thrpt   25    83467.627 ±    529.667  ops/s
MyBenchmark.bigDecimalMidPriceMultiply            thrpt   25    90053.410 ±    785.010  ops/s
MyBenchmark.bigDecimalMidPriceMultiplyWORounding  thrpt   25   114612.951 ±    963.940  ops/s
MyBenchmark.deltixDecimal64MidPrice               thrpt   25    63605.847 ±    434.017  ops/s
MyBenchmark.doubleMidPrice                        thrpt   25   855706.255 ±   3239.675  ops/s
MyBenchmark.doubleMidPriceWORounding              thrpt   25  9751458.388 ± 782845.714  ops/s
 */
/**
 * Benchmark class to compare performance of different mid-price calculation methods
 * using double, BigDecimal, and Decimal64 representations.
 */
@State(Scope.Thread) // Each thread has its own state to ensure thread-safety for benchmarking
public class MyBenchmark {
    static final BigDecimal TWO = BigDecimal.valueOf(2);
    static final BigDecimal HALF = new BigDecimal("0.5");
    static final Decimal64 HALF64 = Decimal64.fromDouble(0.5);
    static final int SIZE = Integer.getInteger("size", 1024);

    // Arrays to store test data for each data type
    final double[] ap = new double[SIZE];
    final double[] bp = new double[SIZE];
    final double[] mp = new double[SIZE];

    final BigDecimal[] ap2 = new BigDecimal[SIZE];
    final BigDecimal[] bp2 = new BigDecimal[SIZE];
    final BigDecimal[] mp2 = new BigDecimal[SIZE];

    final Decimal64[] ap3 = new Decimal64[SIZE];
    final Decimal64[] bp3 = new Decimal64[SIZE];
    final Decimal64[] mp3 = new Decimal64[SIZE];

    public MyBenchmark() {
        Random rand = new Random(1);
        for (int i = 0; i < SIZE; i++) {
            int x = rand.nextInt(200000);
            int y = rand.nextInt(10000);
            ap2[i] = BigDecimal.valueOf(ap[i] = x / 1e5);
            bp2[i] = BigDecimal.valueOf(bp[i] = (x + y) / 1e5);
            ap3[i] = Decimal64.fromBigDecimal(ap2[i]);
            bp3[i] = Decimal64.fromBigDecimal(bp2[i]);
        }

        // Initial benchmark runs to populate result arrays
        doubleMidPrice();
        deltixDecimal64MidPrice();
        bigDecimalMidPriceDivide();

        // Validate that results across different representations match
        for (int i = 0; i < SIZE; i++) {
            if (mp[i] != mp2[i].doubleValue())
                throw new AssertionError(mp[i] + " " + mp2[i]);
            if (mp[i] != mp3[i].doubleValue())
                throw new AssertionError(mp[i] + " " + mp3[i]);
        }
    }

    /**
     * Benchmark method that computes the mid-price using double precision values and rounding.
     */
    @Benchmark
    public void doubleMidPrice() {
        for (int i = 0; i < SIZE; i++)
            mp[i] = round6((ap[i] + bp[i]) / 2);
    }

    /**
     * Benchmark method that computes the mid-price using Decimal64 precision values and rounding.
     */
    @Benchmark
    public void deltixDecimal64MidPrice() {
        for (int i = 0; i < SIZE; i++)
            mp3[i] = ap3[i].add(bp3[i])
                    .multiply(HALF64)
                    .round(6, RoundingMode.HALF_UP);
    }

    /**
     * Benchmark method that computes the mid-price using double precision values without rounding.
     */
    @Benchmark
    public void doubleMidPriceWORounding() {
        for (int i = 0; i < SIZE; i++)
            mp[i] = (ap[i] + bp[i]);
    }

    /**
     * Utility method to round up a double to 6 decimal places.
     */
    static double round6(double x) {
        final double factor = 1e6;
        return (long) (x * factor + 0.5) / factor;
    }

    /**
     * Benchmark method that computes the mid-price using BigDecimal division by 2 and rounding.
     */
    @Benchmark
    public void bigDecimalMidPriceDivide() {
        for (int i = 0; i < SIZE; i++) {
            mp2[i] = ap2[i].add(bp2[i])
                    .divide(TWO, 6, RoundingMode.HALF_UP);
        }
    }

    /**
     * Benchmark method that computes the mid-price using BigDecimal multiplication (HALF) and rounding.
     */
    @Benchmark
    public void bigDecimalMidPriceMultiply() {
        for (int i = 0; i < SIZE; i++) {
            mp2[i] = ap2[i].add(bp2[i])
                    .multiply(HALF)
                    .setScale(6, RoundingMode.HALF_UP);
        }
    }

    /**
     * Benchmark method that computes the mid-price using BigDecimal multiplication (HALF) without rounding.
     */
    @Benchmark
    public void bigDecimalMidPriceMultiplyWORounding() {
        for (int i = 0; i < SIZE; i++) {
            mp2[i] = ap2[i].add(bp2[i])
                    .multiply(HALF);
        }
    }

    /**
     * Main method to run the benchmark with the specified options.
     */
    public static void main(String[] args) throws RunnerException {
        System.out.println("Double vs BigDecimal mid price, with -Dsize=" + SIZE);
        Options opt = new OptionsBuilder()
                .include(".*" + MyBenchmark.class.getSimpleName() + ".*")
                .forks(5)
                .build();

        new Runner(opt).run();
    }
}
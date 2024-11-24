package blog.vanillajava;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Demonstrates the effects of floating-point representation errors in Java.
 * Calculates the modulus of double values and observes how representation errors
 * can lead to unexpected results when performing calculations and equality checks.
 */
/*
0.1d= 0.1000000000000000055511151231257827021181583404541015625
i: 0 / 10.0 = 0, with % 0.1 = 0
i: 3 / 10.0 = 0.299999999999999988897769753748434595763683319091796875, with % 0.1 = 0.09999999999999997779553950749686919152736663818359375
i: 6 / 10.0 = 0.59999999999999997779553950749686919152736663818359375, with % 0.1 = 0.0999999999999999500399638918679556809365749359130859375
i: 7 / 10.0 = 0.6999999999999999555910790149937383830547332763671875, with % 0.1 = 0.099999999999999922284388276239042170345783233642578125
i: 11 / 10.0 = 1.100000000000000088817841970012523233890533447265625, with % 0.1 = 2.77555756156289135105907917022705078125E-17
i: 12 / 10.0 = 1.1999999999999999555910790149937383830547332763671875, with % 0.1 = 0.0999999999999998945288126606101286597549915313720703125
i: 14 / 10.0 = 1.399999999999999911182158029987476766109466552734375, with % 0.1 = 0.0999999999999998390176614293523016385734081268310546875
i: 17 / 10.0 = 1.6999999999999999555910790149937383830547332763671875, with % 0.1 = 0.0999999999999998667732370449812151491641998291015625
i: 19 / 10.0 = 1.899999999999999911182158029987476766109466552734375, with % 0.1 = 0.099999999999999811262085813723388127982616424560546875
i: 22 / 10.0 = 2.20000000000000017763568394002504646778106689453125, with % 0.1 = 5.5511151231257827021181583404541015625E-17

 */
public class RepresentationError {

    /**
     * The main method executes the demonstration of floating-point representation errors.
     */
    public static void main(String[] args) {
        // Create a BigDecimal from the double value 0.1 to show its exact decimal representation.
        BigDecimal bd = new BigDecimal(0.1d);
        // Print the exact decimal representation of 0.1d.
        System.out.println("0.1d= " + bd);
        // Output: 0.1d= 0.1000000000000000055511151231257827021181583404541015625

        // Initialize a Set to store unique modulus results.
        Set<Double> set = new HashSet<>();

        // Loop until the set contains 1000 unique modulus values.
        for (int i = 0; set.size() < 1000; i++) {
            // Calculate d as i divided by 10.0.
            double d = i / 10.0;
            // Calculate the modulus of d with 0.1.
            double mod = d % 0.1;

            // Add the modulus to the set if it is not already present.
            // Due to floating-point precision errors, mod values that should be equal may not be.
            if (set.add(mod)) {
                // Print the value of i, d, and mod with their exact decimal representations using BigDecimal.
                System.out.printf("i: %,d / 10.0 = %s, with %% 0.1 = %s%n",
                        i, new BigDecimal(d), new BigDecimal(mod));
            }
        }
    }
}

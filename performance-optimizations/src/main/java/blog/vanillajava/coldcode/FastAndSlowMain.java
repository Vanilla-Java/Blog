package blog.vanillajava.coldcode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.GregorianCalendar;

/* Windows 10, i7-10710U, Azul 21.0.2
0: Took 93,204 us to serialise/deserialise GregorianCalendar
1: Took 1,876 us to serialise/deserialise GregorianCalendar
10: Took 782 us to serialise/deserialise GregorianCalendar
100: Took 549 us to serialise/deserialise GregorianCalendar
1,000: Took 112 us to serialise/deserialise GregorianCalendar
10,000: Took 45 us to serialise/deserialise GregorianCalendar
20,000: Took 39 us to serialise/deserialise GregorianCalendar
100,001: Took 41 us to serialise/deserialise GregorianCalendar
After sleep 0 ms: Took 42 us to serialise/deserialise GregorianCalendar
After sleep 1 ms: Took 201 us to serialise/deserialise GregorianCalendar
After sleep 2 ms: Took 210 us to serialise/deserialise GregorianCalendar
After sleep 5 ms: Took 235 us to serialise/deserialise GregorianCalendar
After sleep 10 ms: Took 301 us to serialise/deserialise GregorianCalendar
After sleep 20 ms: Took 298 us to serialise/deserialise GregorianCalendar
After sleep 50 ms: Took 297 us to serialise/deserialise GregorianCalendar
After sleep 100 ms: Took 353 us to serialise/deserialise GregorianCalendar
 */
public class FastAndSlowMain {
    public static void main(String[] args) throws Exception {
        // warm up the code
        int[] display = {0, 1, 10, 100, 1000, 10000, 20000, 100001};
        for (int i = 0; i <= display[display.length - 1]; i++) {
            long start = System.nanoTime();
            doTask();
            long time = System.nanoTime() - start;
            if (Arrays.binarySearch(display, i) >= 0)
                System.out.printf("%,d: Took %,d us to serialise/deserialise " +
                        "GregorianCalendar%n", i, time / 1000);
        }

        // run immediately after the previous section where the code has warmed up.
        for (int i : new int[]{0, 1, 2, 5, 10, 20, 50, 100}) {
            int runs = i < 10 ? 1000 : 100;
            long total = 0;
            for (int j = 0; j < runs; j++) {
                Thread.sleep(i);
                long start = System.nanoTime();
                doTask();
                long time = System.nanoTime() - start;
                total += time;
            }
            System.out.printf("After sleep %d ms: Took %,d us to serialise/deserialise " +
                    "GregorianCalendar%n", i, total / runs / 1000);
        }
    }

    // Serialization of the GregorianCalendar was chosen as both as relatively heavy weight but can be used in a short section of code.
    public static void doTask() {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(cal);
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            GregorianCalendar cal2 = (GregorianCalendar) ois.readObject();
            ois.close();
            cal2.toString();

        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}

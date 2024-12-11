package blog.vanillajava.memory;

import java.lang.ref.WeakReference;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * A utility class which attempts to measure the approximate memory usage of various objects and data structures in Java.
 *
 * <p>It is recommended to run this program with the JVM argument:
 * {@code -XX:-UseTLAB}
 * (turning off Thread-Local Allocation Buffers) to make memory allocation more predictable and hopefully more measurable.
 *
 * <p>Even with these settings, the reported sizes are best treated as rough estimates rather than exact measurements.
 */
public class SizeofTest {
    public static void main(String[] args) {
        // Check if memory usage measurement works. If zero, TLAB might still be on.
        if (memoryUsage(() -> new HashMap<>()) == 0) {
            System.out.println("Memory usage is zero, use -XX:-UseTLAB on the command line");
            return;
        }
        System.out.println("Java version=" + System.getProperty("java.version")
                + ", vendor=" + System.getProperty("java.vendor")
                + ", vm=" + System.getProperty("java.vm.name")
                + ", arch=" + System.getProperty("os.arch"));

        System.out.println("\nWrappers");
        printMemoryUsage(Object.class, () -> new Object());

        // Use constructors to ensure a new object is created
        printMemoryUsage(Boolean.class, () -> new Boolean(true));
        printMemoryUsage(Byte.class, () -> new Byte((byte) -128));
        printMemoryUsage(Short.class, () -> new Short((short) -128));
        printMemoryUsage(Integer.class, () -> new Integer(-128));
        printMemoryUsage(Float.class, () -> new Float(-128));
        printMemoryUsage(Character.class, () -> new Character('a'));
        printMemoryUsage(Long.class, () -> new Long(-128));
        printMemoryUsage(Double.class, () -> new Double(-128));

        System.out.println("\nSimple Classes");
        printMemoryUsage(AtomicReference.class, () -> new AtomicReference<>(0));
        printMemoryUsage(AtomicInteger.class, AtomicInteger::new);
        printMemoryUsage(AtomicLong.class, AtomicLong::new);
        printMemoryUsage(Optional.class, () -> Optional.of("test"));
        printMemoryUsage(AbstractMap.SimpleEntry.class, () -> new AbstractMap.SimpleEntry<>(0, 0));
        printMemoryUsage("\"Hello World\"", () -> new String("Hello World")); // Forces a new String object
        printMemoryUsage(CompletableFuture.class, CompletableFuture::new);
        printMemoryUsage(WeakReference.class, () -> new WeakReference<>(new Object()));
        printMemoryUsage(StringBuilder.class, StringBuilder::new);
        printMemoryUsage(Pattern.class, () -> Pattern.compile("test"));
        printMemoryUsage(UUID.class, UUID::randomUUID);
        printMemoryUsage(Exception.class, Exception::new);
        printMemoryUsage(Locale.class, () -> new Locale("EN"));

        System.out.println("\nDate and Time");
        printMemoryUsage(Date.class, Date::new);
        printMemoryUsage(java.sql.Timestamp.class, () -> new java.sql.Timestamp(0));
        printMemoryUsage(TimeZone.class, TimeZone::getDefault);
        printMemoryUsage(LocalDate.class, LocalDate::now);
        printMemoryUsage(LocalTime.class, LocalTime::now);
        printMemoryUsage(LocalDateTime.class, LocalDateTime::now);
        printMemoryUsage(ZonedDateTime.class, ZonedDateTime::now);
        printMemoryUsage(Calendar.class, Calendar::getInstance);
        printMemoryUsage(Instant.class, Instant::now);
        printMemoryUsage(Duration.class, () -> Duration.ofSeconds(60));
        printMemoryUsage(Period.class, () -> Period.ofDays(7));
        printMemoryUsage(ZoneId.class, ZoneId::systemDefault);

        System.out.println("\nCollections");
        printMemoryUsage(ArrayList.class, ArrayList::new);
        printMemoryUsage(LinkedList.class, LinkedList::new);
        printMemoryUsage(ConcurrentLinkedQueue.class, ConcurrentLinkedQueue::new);
        printMemoryUsage(ConcurrentHashMap.class, ConcurrentHashMap::new);
        printMemoryUsage(TreeMap.class, TreeMap::new);
        printMemoryUsage(TreeSet.class, TreeSet::new);
        printMemoryUsage(HashMap.class, HashMap::new);
        printMemoryUsage(HashSet.class, HashSet::new);
        printMemoryUsage(LinkedHashMap.class, LinkedHashMap::new);
        printMemoryUsage(LinkedHashSet.class, LinkedHashSet::new);
        printMemoryUsage(Vector.class, Vector::new);
        printMemoryUsage(Stack.class, Stack::new);
        printMemoryUsage(Hashtable.class, Hashtable::new);

        System.out.println("\nArrays");
        printMemoryUsage("new BitSet(64)", () -> new BitSet(64));
        printMemoryUsage("new boolean[64]", () -> new boolean[64]);
        printMemoryUsage("new byte[64]", () -> new byte[64]);
        printMemoryUsage("new char[64]", () -> new char[64]);
        printMemoryUsage("new short[64]", () -> new short[64]);
        printMemoryUsage("new int[64]", () -> new int[64]);
        printMemoryUsage("new float[64]", () -> new float[64]);
        printMemoryUsage("new long[64]", () -> new long[64]);
        printMemoryUsage("new double[64]", () -> new double[64]);
        printMemoryUsage("new Object[64]", () -> new Object[64]);
        printMemoryUsage("new Integer[64]", () -> new Integer[64]);
        printMemoryUsage("new String[64]", () -> new String[64]);
        printMemoryUsage("new Long[64]", () -> new Long[64]);
        printMemoryUsage("new Double[64]", () -> new Double[64]);

    }

    static <T> void printMemoryUsage(Class<T> clazz, Supplier<T> supplier) {
        printMemoryUsage(clazz.getSimpleName(), supplier);
    }

    static <T> void printMemoryUsage(String name, Supplier<T> supplier) {
        System.out.println(name + " usage was: " + memoryUsage(supplier) + " bytes");
    }

    static long memoryUsage(Supplier<?> supplier) {
        // Create one instance to ensure static initialization is done.
        supplier.get();

        // Record memory usage before and after creating the second instance.
        // sometimes this shows negative,
        // sometimes this shows more than it should because something eats up memory
        long[] memory = new long[3];
        long before = memoryUsage();
        for (int i = 0; i < 3; i++) {
            supplier.get();
            long after = memoryUsage();
            memory[i] = after - before;
            before = after;
        }
        Arrays.sort(memory);
        // take the median value
        return memory[1];
    }

    static long memoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}

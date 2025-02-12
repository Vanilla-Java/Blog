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
        printMemoryUsage("ArrayList.add(0)", () -> {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(0);
            return list;
        });
        printMemoryUsage("ArrayList.add x10", () -> {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) list.add(i);
            return list;
        });
        printMemoryUsage(LinkedList.class, LinkedList::new);
        printMemoryUsage("LinkedList.add(0)", () -> {
            LinkedList<Integer> list = new LinkedList<>();
            list.add(0);
            return list;
        });
        printMemoryUsage("LinkedList.add x10", () -> {
            LinkedList<Integer> list = new LinkedList<>();
            for (int i = 0; i < 10; i++) list.add(i);
            return list;
        });
        printMemoryUsage(ConcurrentLinkedQueue.class, ConcurrentLinkedQueue::new);
        printMemoryUsage("ConcurrentLinkedQueue.add(0)", () -> {
            ConcurrentLinkedQueue<Integer> q = new ConcurrentLinkedQueue<>();
            q.add(0);
            return q;
        });
        printMemoryUsage("ConcurrentLinkedQueue.add x10", () -> {
            ConcurrentLinkedQueue<Integer> q = new ConcurrentLinkedQueue<>();
            for (int i = 0; i < 10; i++) q.add(i);
            return q;
        });
        printMemoryUsage(ConcurrentHashMap.class, ConcurrentHashMap::new);
        printMemoryUsage("ConcurrentHashMap.put(0,0)", () -> {
            ConcurrentHashMap<Integer, Integer> m = new ConcurrentHashMap<>();
            m.put(0, 0);
            return m;
        });
        printMemoryUsage("ConcurrentHashMap.put x10", () -> {
            ConcurrentHashMap<Integer, Integer> m = new ConcurrentHashMap<>();
            for (int i = 0; i < 10; i++) m.put(i, i);
            return m;
        });

        printMemoryUsage(TreeMap.class, TreeMap::new);
        printMemoryUsage("TreeMap.put(0,0)", () -> {
            TreeMap<Integer, Integer> m = new TreeMap<>();
            m.put(0, 0);
            return m;
        });
        printMemoryUsage("TreeMap.put x10", () -> {
            TreeMap<Integer, Integer> m = new TreeMap<>();
            for (int i = 0; i < 10; i++) m.put(i, i);
            return m;
        });

        printMemoryUsage(TreeSet.class, TreeSet::new);
        printMemoryUsage("TreeSet.add(0)", () -> {
            TreeSet<Integer> set = new TreeSet<>();
            set.add(0);
            return set;
        });
        printMemoryUsage("TreeSet.add x10", () -> {
            TreeSet<Integer> set = new TreeSet<>();
            for (int i = 0; i < 10; i++) set.add(i);
            return set;
        });
        printMemoryUsage(HashMap.class, HashMap::new);
        printMemoryUsage("HashMap.put(0,0)", () -> {
            HashMap<Integer, Integer> m = new HashMap<>();
            m.put(0, 0);
            return m;
        });
        printMemoryUsage("HashMap.put x10", () -> {
            HashMap<Integer, Integer> m = new HashMap<>();
            for (int i = 0; i < 10; i++) m.put(i, i);
            return m;
        });
        printMemoryUsage(HashSet.class, HashSet::new);
        printMemoryUsage("HashSet.add(0)", () -> {
            HashSet<Integer> set = new HashSet<>();
            set.add(0);
            return set;
        });
        printMemoryUsage("HashSet.add x10", () -> {
            HashSet<Integer> set = new HashSet<>();
            for (int i = 0; i < 10; i++) set.add(i);
            return set;
        });
        printMemoryUsage(LinkedHashMap.class, LinkedHashMap::new);
        printMemoryUsage("LinkedHashMap.put(0,0)", () -> {
            LinkedHashMap<Integer, Integer> m = new LinkedHashMap<>();
            m.put(0, 0);
            return m;
        });
        printMemoryUsage("LinkedHashMap.put x10", () -> {
            LinkedHashMap<Integer, Integer> m = new LinkedHashMap<>();
            for (int i = 0; i < 10; i++) m.put(i, i);
            return m;
        });
        printMemoryUsage(LinkedHashSet.class, LinkedHashSet::new);
        printMemoryUsage("LinkedHashSet.add(0)", () -> {
            LinkedHashSet<Integer> set = new LinkedHashSet<>();
            set.add(0);
            return set;
        });
        printMemoryUsage("LinkedHashSet.add x10", () -> {
            LinkedHashSet<Integer> set = new LinkedHashSet<>();
            for (int i = 0; i < 10; i++) set.add(i);
            return set;
        });
        printMemoryUsage(Vector.class, Vector::new);
        printMemoryUsage("Vector.add(0)", () -> {
            Vector<Integer> vector = new Vector<>();
            vector.add(0);
            return vector;
        });
        printMemoryUsage("Vector.add x10", () -> {
            Vector<Integer> vector = new Vector<>();
            for (int i = 0; i < 10; i++) vector.add(i);
            return vector;
        });

        printMemoryUsage(Stack.class, Stack::new);
        printMemoryUsage("Stack.add(0)", () -> {
            Stack<Integer> stack = new Stack<>();
            stack.add(0);
            return stack;
        });
        printMemoryUsage("Stack.add x10", () -> {
            Stack<Integer> stack = new Stack<>();
            for (int i = 0; i < 10; i++) stack.add(i);
            return stack;
        });
        printMemoryUsage(Hashtable.class, Hashtable::new);
        printMemoryUsage("Hashtable.put(0,0)", () -> {
            Hashtable<Integer, Integer> m = new Hashtable<>();
            m.put(0, 0);
            return m;
        });
        printMemoryUsage("Hashtable.put x10", () -> {
            Hashtable<Integer, Integer> m = new Hashtable<>();
            for (int i = 0; i < 10; i++) m.put(i, i);
            return m;
        });

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

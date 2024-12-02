package blog.vanillajava.toarray;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.*;

/* Run on a Ryzen 5950X running Linux with Azul JDK v21
-Dthreads=1
Benchmark                                      Mode  Cnt          Score         Error  Units
ToArrayBench.hashSetsToArray                  thrpt   21   45671887.394 ±  164539.467  ops/s
ToArrayBench.hashSetsToArrayNewString0        thrpt   21   41233006.658 ± 2570657.467  ops/s
ToArrayBench.hashSetsToArrayNewStringSize     thrpt   21   36655255.403 ±  290403.321  ops/s
ToArrayBench.hashSetsToArrayNoStrings         thrpt   21   37383486.868 ± 1536298.098  ops/s
ToArrayBench.hashSetsToArrayTriNewStringSize  thrpt   21   37234527.981 ±  403125.663  ops/s
ToArrayBench.listToArray                      thrpt   21  169754597.811 ± 4174898.469  ops/s
ToArrayBench.listToArrayNewString0            thrpt   21   63431940.768 ± 1308737.980  ops/s
ToArrayBench.listToArrayNewStringSize         thrpt   21   66317414.311 ±  239669.003  ops/s
ToArrayBench.listToArrayNoStrings             thrpt   21   66164278.000 ± 1142057.628  ops/s
ToArrayBench.listToArrayTriNewStringSize      thrpt   21   68279338.180 ±  667977.249  ops/s
ToArrayBench.treeSetsToArray                  thrpt   21   31465920.508 ± 6563566.230  ops/s
ToArrayBench.treeSetsToArrayNewString0        thrpt   21   36285893.317 ± 1271781.827  ops/s
ToArrayBench.treeSetsToArrayNewStringSize     thrpt   21   30002562.806 ± 4184300.316  ops/s
ToArrayBench.treeSetsToArrayNoStrings         thrpt   21   36750868.392 ±  596945.007  ops/s
ToArrayBench.treeSetsToArrayTriNewStringSize  thrpt   21   33398162.958 ± 4337647.240  ops/s

-Dthreads=2
Benchmark                                      Mode  Cnt          Score          Error  Units
ToArrayBench.hashSetsToArray                  thrpt   21   91152806.381 ±   564796.759  ops/s
ToArrayBench.hashSetsToArrayNewString0        thrpt   21   85993282.912 ±   820543.060  ops/s
ToArrayBench.hashSetsToArrayNewStringSize     thrpt   21   73523323.537 ±   567859.221  ops/s
ToArrayBench.hashSetsToArrayNoStrings         thrpt   21   74252462.718 ±  2715602.111  ops/s
ToArrayBench.hashSetsToArrayTriNewStringSize  thrpt   21   74868407.610 ±   311200.259  ops/s
ToArrayBench.listToArray                      thrpt   21  334996593.259 ±  5918146.068  ops/s
ToArrayBench.listToArrayNewString0            thrpt   21  125669030.343 ±  1661695.341  ops/s
ToArrayBench.listToArrayNewStringSize         thrpt   21  127133356.508 ± 13807686.407  ops/s
ToArrayBench.listToArrayNoStrings             thrpt   21  130119586.525 ±  1641853.622  ops/s
ToArrayBench.listToArrayTriNewStringSize      thrpt   21  134842893.855 ±  2442242.631  ops/s
ToArrayBench.treeSetsToArray                  thrpt   21   63495367.587 ± 12029522.342  ops/s
ToArrayBench.treeSetsToArrayNewString0        thrpt   21   73797497.629 ±   707074.146  ops/s
ToArrayBench.treeSetsToArrayNewStringSize     thrpt   21   57071302.428 ±  8719277.265  ops/s
ToArrayBench.treeSetsToArrayNoStrings         thrpt   21   73487014.040 ±   562567.237  ops/s
ToArrayBench.treeSetsToArrayTriNewStringSize  thrpt   21   64191191.149 ±  9063330.306  ops/s

-Dthreads=4
Benchmark                                      Mode  Cnt          Score          Error  Units
ToArrayBench.hashSetsToArray                  thrpt   21  166022621.813 ±  7777215.176  ops/s
ToArrayBench.hashSetsToArrayNewString0        thrpt   21  158110730.409 ± 16228480.112  ops/s
ToArrayBench.hashSetsToArrayNewStringSize     thrpt   21  140000492.206 ± 10227895.379  ops/s
ToArrayBench.hashSetsToArrayNoStrings         thrpt   21  145500030.418 ±  1938300.138  ops/s
ToArrayBench.hashSetsToArrayTriNewStringSize  thrpt   21  136862210.562 ± 11265086.007  ops/s
ToArrayBench.listToArray                      thrpt   21  482794495.490 ± 11211877.985  ops/s
ToArrayBench.listToArrayNewString0            thrpt   21  244482797.189 ±  9656345.786  ops/s
ToArrayBench.listToArrayNewStringSize         thrpt   21  258356393.597 ± 11390018.529  ops/s
ToArrayBench.listToArrayNoStrings             thrpt   21  256894549.002 ±  9120674.393  ops/s
ToArrayBench.listToArrayTriNewStringSize      thrpt   21  263832848.231 ±  7786604.080  ops/s
ToArrayBench.treeSetsToArray                  thrpt   21  148312173.721 ±  9971997.313  ops/s
ToArrayBench.treeSetsToArrayNewString0        thrpt   21  120248448.786 ± 26240499.769  ops/s
ToArrayBench.treeSetsToArrayNewStringSize     thrpt   21  132514956.157 ±  9349592.078  ops/s
ToArrayBench.treeSetsToArrayNoStrings         thrpt   21  103522651.689 ± 25012517.370  ops/s
ToArrayBench.treeSetsToArrayTriNewStringSize  thrpt   21  141197722.690 ±  5580254.514  ops/s

-Dthreads=8
Benchmark                                      Mode  Cnt          Score          Error  Units
ToArrayBench.hashSetsToArray                  thrpt   21  287479563.660 ± 11749820.912  ops/s
ToArrayBench.hashSetsToArrayNewString0        thrpt   21  272329665.592 ± 20383430.551  ops/s
ToArrayBench.hashSetsToArrayNewStringSize     thrpt   21  254015477.942 ± 17907339.352  ops/s
ToArrayBench.hashSetsToArrayNoStrings         thrpt   21  261282517.219 ± 11107783.303  ops/s
ToArrayBench.hashSetsToArrayTriNewStringSize  thrpt   21  258536352.834 ± 26257532.364  ops/s
ToArrayBench.listToArray                      thrpt   21  456513630.023 ±  8779098.977  ops/s
ToArrayBench.listToArrayNewString0            thrpt   21  355051805.864 ±  7187119.164  ops/s
ToArrayBench.listToArrayNewStringSize         thrpt   21  432660717.039 ± 15479504.744  ops/s
ToArrayBench.listToArrayNoStrings             thrpt   21  463326077.183 ± 15955707.095  ops/s
ToArrayBench.listToArrayTriNewStringSize      thrpt   21  464959247.055 ± 10547800.274  ops/s
ToArrayBench.treeSetsToArray                  thrpt   21  252417511.430 ± 39483580.841  ops/s
ToArrayBench.treeSetsToArrayNewString0        thrpt   21  212248428.604 ± 40801845.920  ops/s
ToArrayBench.treeSetsToArrayNewStringSize     thrpt   21  230548486.269 ± 31064178.446  ops/s
ToArrayBench.treeSetsToArrayNoStrings         thrpt   21  241803604.409 ± 50013469.427  ops/s
ToArrayBench.treeSetsToArrayTriNewStringSize  thrpt   21  252463095.232 ± 42364441.405  ops/s
 */

/**
 * Benchmark class to compare performance of different ways to call toArray to see if it makes a difference to performance
 */
@State(Scope.Thread) // Each thread has its own state to ensure thread-safety for benchmarking
public class ToArrayBench {
    static final int THREADS = Integer.getInteger("threads", 8);
    static final String[] NO_STRINGS = {};

    List<Set<String>> hashSets = new ArrayList<>();
    List<Set<String>> treeSets = new ArrayList<>();
    List<List<String>> lists = new ArrayList<>();

    public ToArrayBench() {
        // empty
        lists.add(new ArrayList<>());
        // 3 elements
        lists.add(new ArrayList<>(Arrays.asList("a", "b", "c")));
        // 7 elements
        lists.add(new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f")));
        // 16 elements
        lists.add(new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p")));

        for (List<String> list : lists) {
            hashSets.add(new HashSet<>(list));
            treeSets.add(new TreeSet<>(list));
        }
    }

    int n = 0;

    @Benchmark
    public Object[] listToArray() {
        List<String> strings = lists.get(n++ & 3);
        return strings.toArray();
    }

    @Benchmark
    public Object[] hashSetsToArray() {
        Set<String> strings = hashSets.get(n++ & 3);
        return strings.toArray();
    }

    @Benchmark
    public Object[] treeSetsToArray() {
        Set<String> strings = treeSets.get(n++ & 3);
        return strings.toArray();
    }

    @Benchmark
    public String[] listToArrayNoStrings() {
        List<String> strings = lists.get(n++ & 3);
        return strings.toArray(NO_STRINGS);
    }

    @Benchmark
    public String[] hashSetsToArrayNoStrings() {
        Set<String> strings = hashSets.get(n++ & 3);
        return strings.toArray(NO_STRINGS);
    }

    @Benchmark
    public String[] treeSetsToArrayNoStrings() {
        Set<String> strings = treeSets.get(n++ & 3);
        return strings.toArray(NO_STRINGS);
    }

    @Benchmark
    public String[] listToArrayNewString0() {
        List<String> strings = lists.get(n++ & 3);
        return strings.toArray(new String[0]);
    }

    @Benchmark
    public String[] hashSetsToArrayNewString0() {
        Set<String> strings = hashSets.get(n++ & 3);
        return strings.toArray(new String[0]);
    }

    @Benchmark
    public String[] treeSetsToArrayNewString0() {
        Set<String> strings = treeSets.get(n++ & 3);
        return strings.toArray(new String[0]);
    }


    @Benchmark
    public String[] listToArrayNewStringSize() {
        List<String> strings = lists.get(n++ & 3);
        return strings.toArray(new String[strings.size()]);
    }

    @Benchmark
    public String[] hashSetsToArrayNewStringSize() {
        Set<String> strings = hashSets.get(n++ & 3);
        return strings.toArray(new String[strings.size()]);
    }

    @Benchmark
    public String[] treeSetsToArrayNewStringSize() {
        Set<String> strings = treeSets.get(n++ & 3);
        return strings.toArray(new String[strings.size()]);
    }

    @Benchmark
    public String[] listToArrayTriNewStringSize() {
        List<String> strings = lists.get(n++ & 3);
        return strings.isEmpty() ? NO_STRINGS : strings.toArray(new String[strings.size()]);
    }

    @Benchmark
    public String[] hashSetsToArrayTriNewStringSize() {
        Set<String> strings = hashSets.get(n++ & 3);
        return strings.isEmpty() ? NO_STRINGS : strings.toArray(new String[strings.size()]);
    }

    @Benchmark
    public String[] treeSetsToArrayTriNewStringSize() {
        Set<String> strings = treeSets.get(n++ & 3);
        return strings.isEmpty() ? NO_STRINGS : strings.toArray(new String[strings.size()]);
    }

    /**
     * Main method to run the benchmark with the specified options.
     */
    public static void main(String[] args) throws RunnerException {
        System.out.println("-Dthreads=" + THREADS);

        Options opt = new OptionsBuilder()
                .include(".*" + ToArrayBench.class.getSimpleName() + ".*")
                .jvmArgsAppend("-Xmx1g")
                .warmupIterations(2)
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(3)
                .measurementTime(TimeValue.seconds(10))
                .threads(THREADS)
                .forks(7)
                .build();

        new Runner(opt).run();
    }
}
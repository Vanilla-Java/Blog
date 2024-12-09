package blog.vanillajava.triviallycopyable;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.Wire;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/* Options required on Java 17+ to run this benchmark:
--add-exports=java.base/jdk.internal.misc=ALL-UNNAMED
--add-exports=java.base/jdk.internal.ref=ALL-UNNAMED
--add-exports=java.base/jdk.internal.util=ALL-UNNAMED
--add-exports=java.base/sun.nio.ch=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED
--add-opens=java.base/java.io=ALL-UNNAMED
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=jdk.compiler/com.sun.tools.javac=ALL-UNNAMED
 */
/* Run on an Intel I7-10710U
Benchmark                              Mode  Cnt     Score    Error  Units
BenchmarkRunner.defaultWriteRead       avgt   25  1708.038 ± 13.975  ns/op
BenchmarkRunner.defaultBytesWriteRead  avgt   25   495.976 ± 10.731  ns/op
BenchmarkRunner.explicitWriteRead      avgt   25    47.820 ±  1.391  ns/op
BenchmarkRunner.trivialWriteRead       avgt   25    32.289 ±  0.939  ns/op
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(NANOSECONDS)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 5, time = 200, timeUnit = MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = MILLISECONDS)
public class BenchmarkRunner {

    private final MarketData defaultMarketData = new DefaultMarketData();
    private final MarketData defaultMarketData2 = new DefaultMarketData();
    private final MarketData defaultBytesMarketData = new DefaultBytesMarketData();
    private final MarketData defaultBytesMarketData2 = new DefaultBytesMarketData();
    private final MarketData explicitMarketData = new ExplicitMarketData();
    private final MarketData explicitMarketData2 = new ExplicitMarketData();
    private final MarketData triviallyCopyableMarketData = new TriviallyCopyableMarketData();
    private final MarketData triviallyCopyableMarketData2 = new TriviallyCopyableMarketData();
    private final Bytes<Void> bytes = Bytes.allocateElasticDirect();
    private final Wire wire = BinaryWire.binaryOnly(bytes);

    public BenchmarkRunner() {
    }

    @Benchmark
    public void defaultWriteRead() {
        bytes.clear();
        wire.getValueOut().marshallable(defaultMarketData);
        wire.getValueIn().marshallable(defaultMarketData2);
    }
    @Benchmark
    public void defaultBytesWriteRead() {
        bytes.clear();
        wire.getValueOut().marshallable(defaultBytesMarketData);
        wire.getValueIn().marshallable(defaultBytesMarketData2);
    }

    @Benchmark
    public void explicitWriteRead() {
        bytes.clear();
        wire.getValueOut().marshallable(explicitMarketData);
        wire.getValueIn().marshallable(explicitMarketData2);
    }

    @Benchmark
    public void trivialWriteRead() {
        bytes.clear();
        wire.getValueOut().marshallable(triviallyCopyableMarketData);
        wire.getValueIn().marshallable(triviallyCopyableMarketData2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + BenchmarkRunner.class.getSimpleName() + ".*")
                .forks(5)
                .build();

        new Runner(opt).run();
    }

}
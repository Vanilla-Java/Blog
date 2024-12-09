package blog.vanillajava.triviallycopyable;

import blog.vanillajava.decimal.MyBenchmark;
import net.openhft.chronicle.bytes.Bytes;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(NANOSECONDS)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 5, time = 200, timeUnit = MILLISECONDS)
@Measurement(iterations = 5, time = 500, timeUnit = MILLISECONDS)
public class BenchmarkRunner {

    private final MarketData defaultMarketData = new DefaultMarketData();
    private final MarketData explicitMarketData = new ExplicitMarketData();
    private final MarketData triviallyCopyableMarketData = new TriviallyCopyableMarketData();
    private final Bytes<Void> toBytes = Bytes.allocateElasticDirect();
    private final Bytes<Void> fromBytesDefault = Bytes.allocateElasticDirect();
    private final Bytes<Void> fromBytesExplicit = Bytes.allocateElasticDirect();
    private final Bytes<Void> fromBytesTriviallyCopyable = Bytes.allocateElasticDirect();

    public BenchmarkRunner() {
        defaultMarketData.writeMarshallable(fromBytesDefault);
        explicitMarketData.writeMarshallable(fromBytesExplicit);
        triviallyCopyableMarketData.writeMarshallable(fromBytesTriviallyCopyable);
    }

    @Benchmark
    public void defaultWrite() {
        toBytes.writePosition(0);
        defaultMarketData.writeMarshallable(toBytes);
    }

    @Benchmark
    public void defaultRead() {
        fromBytesDefault.readPosition(0);
        defaultMarketData.readMarshallable(fromBytesDefault);
    }

    @Benchmark
    public void explicitWrite() {
        toBytes.writePosition(0);
        explicitMarketData.writeMarshallable(toBytes);
    }

    @Benchmark
    public void explicitRead() {
        fromBytesExplicit.readPosition(0);
        explicitMarketData.readMarshallable(fromBytesExplicit);
    }

    @Benchmark
    public void trivialWrite() {
        toBytes.writePosition(0);
        triviallyCopyableMarketData.writeMarshallable(toBytes);
    }

    @Benchmark
    public void trivialRead() {
        fromBytesTriviallyCopyable.readPosition(0);
        triviallyCopyableMarketData.readMarshallable(fromBytesTriviallyCopyable);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + BenchmarkRunner.class.getSimpleName() + ".*")
                .forks(5)
                .build();

        new Runner(opt).run();
    }

}
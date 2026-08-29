package fastaisandbox.benchmark;

import fastaisandbox.FastAISandbox;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class FastAISandboxBenchmark {

    private FastAISandbox sandbox;
    private String safeCommand;
    private String safePath;

    @Setup
    public void setup() {
        sandbox = new FastAISandbox();
        safeCommand = "python compute_embeddings.py --dim 1536";
        safePath = "data/input.parquet";
    }

    @Benchmark
    public boolean benchmarkCommandValidation() {
        return sandbox.executeSafe(safeCommand);
    }

    @Benchmark
    public boolean benchmarkPathJailValidation() {
        return sandbox.isPathPermitted(safePath);
    }
}
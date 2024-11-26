package com.matrix;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchmarkBasic {
    @Param({"10", "20", "50", "100", "200", "500", "1000", "1500", "2000"})
    public int n;
    private int[][] matrix1;
    private int[][] matrix2;
    private int[][] resultMatrix1;

    @Setup(Level.Trial)
    public void setupMatrix() {
        matrix1 = utils.generateMatrix(n, false);
        matrix2 = utils.generateMatrix(n, false);
        resultMatrix1 = utils.generateMatrix(n, true);
    }

    @Benchmark
    public void multiplyMatricesBasic() {
        BasicMatrixMultiplication.multiplyMatrices(matrix1, matrix2, resultMatrix1);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

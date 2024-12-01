package com.matrix;

import com.matrix.multiplications.ArraysParallelMultiplication;
import com.matrix.multiplications.BasicMatrixMultiplication;
import com.matrix.multiplications.BlockingParallelMultiplication;
import com.matrix.multiplications.VectorizedMultiplication;
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
public class MultiplicationBenchmark {
    @Param({"10", "20", "50", "100", "200", "500", "1000", "1500", "2000"})
//    @Param({"1000", "1500", "2000"})
    public int n;
    private int[][] matrix1;
    private int[][] matrix2;
    private int[][] resultMatrix;

    @Setup(Level.Trial)
    public void setupMatrix() {
        matrix1 = utils.generateMatrix(n, false);
        matrix2 = utils.generateMatrix(n, false);
        resultMatrix = utils.generateMatrix(n, true);
    }

    @Benchmark
    public void multiplyParallelArrays() {
        ArraysParallelMultiplication.multiply(matrix1, matrix2, resultMatrix);
    }


    @Benchmark
    public void multiplyParallelBlocking() {
        int tileSize = 1024;
        BlockingParallelMultiplication.multiply(matrix1, matrix2, resultMatrix, tileSize);
    }

    @Benchmark
    public void multiplyVectorized() {
        VectorizedMultiplication.multiply(matrix1, matrix2, resultMatrix);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

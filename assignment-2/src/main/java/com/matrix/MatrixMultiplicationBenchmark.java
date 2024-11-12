package com.matrix;

import com.jmatio.types.MLSparse;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MatrixMultiplicationBenchmark {

    @Param({"10", "20", "50", "100", "200", "500", "1000", "1500", "2000"})
    public int n;
    int tileSize = 1000;

    double densityRatio = 0.01;
    private BasicMatrix matrix1;
    private BasicMatrix matrix2;
    private BasicMatrix resultMatrix1;

    private HashMap<Integer, ArrayList<MatrixTuple>> matrix3;

    private HashMap<Integer, ArrayList<MatrixTuple>> matrix4;

    private HashMap<Integer, ArrayList<MatrixTuple>> matrix5;

    private HashMap<Integer, ArrayList<MatrixTuple>> matrix6;
    private MLSparse resultMatrix2;

    @Setup(Level.Trial)
    public void setupMatrix() {
        matrix1 = new BasicMatrix(n, false);
        matrix2 = new BasicMatrix(n, false);
        resultMatrix1 = new BasicMatrix(n, true);

        setupSparseMatrices();
        setupMarkovMatrix();
    }

    void setupMarkovMatrix() {
        MLSparse sparseMatrix = SparseMatrixMultiplication.loadSparseMatrix();
        matrix3 = MatrixConverter.convertSparseMatrixToHashmap(sparseMatrix);
        matrix4 = new HashMap<>(matrix3);
        resultMatrix2 = new MLSparse(null, new int[] {matrix3.size(), matrix3.size()}, 0, 0);
    }

    void setupSparseMatrices() {
        matrix5 = MatrixConverter.convertBasicToSparseMatrix(new BasicMatrix(n, densityRatio));
        matrix6 = MatrixConverter.convertBasicToSparseMatrix(new BasicMatrix(n, densityRatio));
        resultMatrix2 = new MLSparse(null, new int[] {n, n}, 0, 0);
    }

    @Benchmark
    public void multiplyMatricesBasic() {
        BasicMatrixMultiplication.multiplyMatrices(matrix1, matrix2, resultMatrix1);
    }

    @Benchmark
    public void multiplyMatricesBlocking() {
        BlockingMatrixMultiplication.multiplyMatrices(matrix1, matrix2, resultMatrix1, tileSize);
    }

    @Benchmark
    public void multiplyMatricesColumnBlocking() {
        ColumnBlockingMultiplication.multiplyMatrices(matrix1, matrix2, resultMatrix1, tileSize);
    }

    @Benchmark
    public void multiplyMarkovMatrix() {
        SparseMatrixMultiplication.sparseMultiply(matrix3, matrix4, resultMatrix2);
    }

    @Benchmark
    public void multiplySparseMatrices() {
        SparseMatrixMultiplication.sparseMultiply(matrix5, matrix6, resultMatrix2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}

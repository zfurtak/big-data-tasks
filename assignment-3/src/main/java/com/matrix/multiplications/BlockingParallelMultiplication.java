package com.matrix.multiplications;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlockingParallelMultiplication {

    public static void multiply(int[][] matrix1, int[][] matrix2, int[][] resultMatrix,
            int tileSize) {
        int n = matrix1.length;
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int r = 0; r < n; r++) {
            for (int t = 0; t < n; t += tileSize) {
                for (int i = 0; i < n; i += tileSize) {
                    int finalR = r;
                    int finalT = t;
                    int finalI = i;
                    executor.submit(() -> {
                        for (int j = 0; j < tileSize && finalI + j < n; j++) {
                            for (int k = 0; k < tileSize && finalT + k < n; k++) {
                                resultMatrix[finalR][finalT + k] +=
                                        matrix1[finalR][finalI + j] * matrix2[finalI + j][finalT + k];
                            }
                        }
                    });
                }
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Matrix multiplication was interrupted");
        }
    }
}
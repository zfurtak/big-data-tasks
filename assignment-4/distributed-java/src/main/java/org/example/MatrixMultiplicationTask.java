package org.example;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplicationTask implements Callable<Void> {
    private final int from;
    private final int to;

    public MatrixMultiplicationTask(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Void call() throws Exception {
        multiply(from, to);
        return null;
    }


    public static void multiply(int startRow, int endRow) {
        System.out.println("Performing multiplication from: " + startRow + "to: " + endRow);
        double[][] matrix1 = MultiplicationMatrixDataManager.getShared().getMatrixA();
        double[][] matrix2 = MultiplicationMatrixDataManager.getShared().getMatrixB();

        int n = matrix1.length;
        int tileSize = 1024;
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);
        HashMap<String, Double> resultMap = MultiplicationMatrixDataManager.getShared().result;

        for (int r = startRow; r < endRow; r++) {
            for (int t = 0; t < n; t += tileSize) {
                for (int i = 0; i < n; i += tileSize) {
                    int finalR = r;
                    int finalT = t;
                    int finalI = i;
                    executor.submit(() -> {
                        for (int j = 0; j < tileSize && finalI + j < n; j++) {
                            for (int k = 0; k < tileSize && finalT + k < n; k++) {
                                String key = finalR + "-" + (finalI + k);
                                Double toAdd = matrix1[finalR][finalI + j] * matrix2[finalI + j][finalT + k];
                                if (resultMap.get(key) != null) {
                                  MultiplicationMatrixDataManager.getShared().result.put(key, resultMap.get(key) + toAdd);
                                } else {
                                  MultiplicationMatrixDataManager.getShared().result.put(key, toAdd);
                                }
                            }
                        }
                    });
                }
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            MultiplicationMatrixDataManager.getShared().shareResults();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Matrix multiplication interrupted", e);
        }
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
    /*    public static void multiply(int[][] matrix1, int[][] matrix2, int[][] resultMatrix) {
        int n = matrix1.length;
        int tileSize = 256;
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

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
    }*/
}

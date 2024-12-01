package com.matrix.multiplications;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class VectorizedParallelMultiplication {

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static int[][] multiply(int[][] matrix1, int[][] matrix2, int[][] resultMatrix) {
        int size = matrix1.length;

        forkJoinPool.invoke(new MatrixMultiplicationTask(matrix1, matrix2, resultMatrix, 0, size));

        return resultMatrix;
    }

    private static class MatrixMultiplicationTask extends RecursiveTask<Void> {
        private final int[][] matrix1;
        private final int[][] matrix2;
        private final int[][] resultMatrix;
        private final int startRow;
        private final int endRow;
        private static final int THRESHOLD = 100;

        public MatrixMultiplicationTask(int[][] matrix1, int[][] matrix2, int[][] resultMatrix, int startRow, int endRow) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.resultMatrix = resultMatrix;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        protected Void compute() {
            int size = matrix1.length;

            if (endRow - startRow <= THRESHOLD) {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < size; j++) {
                        resultMatrix[i][j] = vectorizedDotProduct(matrix1[i], getColumn(matrix2, j));
                    }
                }
            } else {
                int mid = (startRow + endRow) / 2;
                MatrixMultiplicationTask task1 = new MatrixMultiplicationTask(matrix1, matrix2, resultMatrix, startRow, mid);
                MatrixMultiplicationTask task2 = new MatrixMultiplicationTask(matrix1, matrix2, resultMatrix, mid, endRow);
                invokeAll(task1, task2);
            }
            return null;
        }
    }

    private static int vectorizedDotProduct(int[] row, int[] column) {
        int sum = 0;
        for (int k = 0; k < row.length; k++) {
            sum += row[k] * column[k];
        }
        return sum;
    }

    private static int[] getColumn(int[][] matrix, int colIndex) {
        int[] column = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][colIndex];
        }
        return column;
    }
}

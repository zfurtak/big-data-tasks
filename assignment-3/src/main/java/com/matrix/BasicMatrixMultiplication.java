package com.matrix;

public class BasicMatrixMultiplication {
    public static void multiplyMatrices(int[][] matrix1, int[][] matrix2, int[][] resultMatrix) {
        int n = matrix1.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    resultMatrix[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
    }
}
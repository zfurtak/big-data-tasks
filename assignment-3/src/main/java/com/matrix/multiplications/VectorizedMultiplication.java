package com.matrix.multiplications;

public class VectorizedMultiplication {
    public static int[][] multiply(int[][] matrix1, int[][] matrix2, int[][] resultMatrix){
        int size = matrix1.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                resultMatrix[i][j] = vectorizedDotProduct(matrix1[i], getColumn(matrix2, j));
            }
        }
        return resultMatrix;
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



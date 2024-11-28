package com.matrix.multiplications;
import java.util.Arrays;

public class ArraysParallelMultiplication
{
    public static int[][] multiply(int[][] matrix1, int[][] matrix2, int[][] resultMatrix) {
        Arrays.parallelSetAll(resultMatrix, i -> multiplyRow(matrix1, matrix2, i));
        return resultMatrix;
    }

    private static int[] multiplyRow(int[][] matrix1, int[][] matrix2, int row) {
        int size = matrix1[0].length;
        int[] resultRow = new int[size];

        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                resultRow[j] += matrix1[row][k] * matrix2[k][j];
            }
        }
        return resultRow;
    }
}
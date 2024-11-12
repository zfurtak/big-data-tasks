package com.matrix;

public class BasicMatrixMultiplication {
    public static void multiplyMatrices(BasicMatrix matrix1, BasicMatrix matrix2, BasicMatrix resultMatrix) {
        int n = matrix1.getSize();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    int newValue = matrix1.getCellValue(i, k) * matrix2.getCellValue(k, j);
                    resultMatrix.increaseCellValue(i, j, newValue);
                }
            }
        }
    }
}

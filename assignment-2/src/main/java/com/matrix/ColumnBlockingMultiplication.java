package com.matrix;

public class ColumnBlockingMultiplication {
    public static void multiplyMatrices(BasicMatrix matrix1, BasicMatrix matrix2, BasicMatrix resultMatrix, int tileSize) {
        int n = matrix1.getSize();

        for (int i = 0; i < n; i += tileSize) {
            for (int r = 0; r < n; r++) {
                for (int j = 0; j < n; j += tileSize) {
                    for (int k = 0; k < tileSize && k + j < n; k++) {
                        for (int ii = 0; ii < tileSize && i + ii < n; ii++) {
                            int value = matrix1.getCellValue(r, j + k) *
                                    matrix2.getCellValue(j + k, i + ii);
                            resultMatrix.increaseCellValue(r, i + ii, value);

                        }
                    }
                }
            }
        }
    }
}

package com.matrix;

public class BlockingMatrixMultiplication {

    public static void multiplyMatrices(BasicMatrix matrix1, BasicMatrix matrix2, BasicMatrix resultMatrix, int tileSize) {
        int n = matrix1.getSize();

        for (int r = 0; r < n; r++) {
            for (int t = 0; t < n; t += tileSize) {
                for (int i = 0; i < n; i += tileSize) {
                    for (int j = 0; j < tileSize && i + j < n; j++) {
                        for (int k = 0; k < tileSize && t + k < n; k++) {
                            int value = matrix1.getCellValue(r, i + j) *
                                    matrix2.getCellValue(i + j, t + k);
                            resultMatrix.increaseCellValue(r, t + k, value);

                        }
                    }
                }
            }
        }
    }
}



package com.matrix;

import java.util.Random;

public class utils {
    static int[][] generateMatrix(int size, boolean empty) {
        int[][] matrix = new int[size][size];
        if (!empty) {
            Random rand = new Random();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = rand.nextInt(10000); // Fills the matrix with random integers between 0-99.
                }
            }
        }
        return matrix;
    }
}

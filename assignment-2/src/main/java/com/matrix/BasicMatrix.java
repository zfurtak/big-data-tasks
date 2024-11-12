package com.matrix;

import java.util.Arrays;
import java.util.Random;

public class BasicMatrix {

    private int[][] data;

    private int size;

    public BasicMatrix(int size, boolean empty) {
        this.size = size;
        this.data = new int[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (empty) {
                    this.data[i][j] = 0;
                } else {
                    this.data[i][j] = random.nextInt();
                }
            }
        }
    }

    public BasicMatrix(int size, double density) {
        this.size = size;
        this.data = new int[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boolean x = random.nextDouble() < density;
                if (x) {
                    this.data[i][j] = random.nextInt();
                } else {
                    this.data[i][j] = 0;
                }
            }
        }
    }

    public int getCellValue(int row, int col) {
        return this.data[row][col];
    }

    public void increaseCellValue(int row, int col, int value) {
        this.data[row][col] += value;
    }

    public int getSize() {
        return this.size;
    }
}

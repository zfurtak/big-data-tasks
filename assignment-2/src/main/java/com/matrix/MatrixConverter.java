package com.matrix;

import com.jmatio.types.MLSparse;

import java.util.ArrayList;
import java.util.HashMap;

public class MatrixConverter {
    public static HashMap<Integer, ArrayList<MatrixTuple>> convertBasicToSparseMatrix(BasicMatrix matrix) {
        HashMap<Integer, ArrayList<MatrixTuple>> processedData = new HashMap<>();

        for (int i = 0; i < matrix.getSize(); i++) {
            for (int j = 0; j < matrix.getSize(); j++) {
                double value = matrix.getCellValue(i,j);

                if (value != 0) {
                    ArrayList<MatrixTuple> rowList = processedData.computeIfAbsent(i, x -> new ArrayList<>());
                    rowList.add(new MatrixTuple(j, value));
                }
            }
        }
        return processedData;
    }

    public static HashMap<Integer, ArrayList<MatrixTuple>> convertSparseMatrixToHashmap(MLSparse matrix) {
        int[] ir = matrix.getIR();
        int[] jc = matrix.getJC();
        Double[] data = matrix.exportReal();
        HashMap<Integer, ArrayList<MatrixTuple>> processedData = new HashMap<>();

        for (int i = 0; i < jc.length - 1; i++) {
            for (int j = jc[i]; j < jc[i + 1]; j++) {
                int row = ir[j];
                double value = data[j];

                processedData.computeIfAbsent(row, x -> new ArrayList<>());
                ArrayList<MatrixTuple> rowList = processedData.get(row);
                rowList.add(new MatrixTuple(i, value));
            }
        }
        return processedData;
    }

}

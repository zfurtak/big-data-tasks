package com.matrix;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLSparse;
import com.jmatio.types.MLStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SparseMatrixMultiplication {
    public static void sparseMultiply(HashMap<Integer, ArrayList<MatrixTuple>> matrix1, HashMap<Integer, ArrayList<MatrixTuple>> matrix2, MLSparse resultMatrix) {
        for (int i : matrix1.keySet()) {
            List<MatrixTuple> tuplesForRow = matrix1.get(i);

            for (MatrixTuple tuple1 : tuplesForRow) {
                int column1 = tuple1.column;
                if (matrix2.containsKey(column1)) {
                    List<MatrixTuple> row2 = matrix2.get(column1);
                    for (MatrixTuple tuple2 : row2) {
                        int j = tuple2.column;
                        Double currentValue = resultMatrix.get(i, j);
                        if(currentValue == null)
                            resultMatrix.set(tuple1.value * tuple2.value, i, j);
                        else
                            resultMatrix.set(currentValue + tuple1.value * tuple2.value, i, j);
                    }
                }
            }
        }
    }

    public static MLSparse loadSparseMatrix() {
        try {
            String path = "/Users/zuzannafurtak/studia/las_palmas/big_data/big-data-tasks/assignment-2/src/main/resources/markov.mat";
            MatFileReader matReader = new MatFileReader(path);
            MLArray mlArrayProblem = matReader.getMLArray("Problem");
            return (MLSparse) ((MLStructure)mlArrayProblem).getField("A");
        } catch (Exception e) {
            System.out.println("There was a problem with reading MLSparse matrix from a file!");
            e.printStackTrace();
        }
        return null;
    }
}


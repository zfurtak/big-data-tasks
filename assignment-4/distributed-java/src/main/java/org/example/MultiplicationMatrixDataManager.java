package org.example;

import com.hazelcast.core.HazelcastInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiplicationMatrixDataManager {
    private static MultiplicationMatrixDataManager shared;
    public HazelcastInstance hazelcastInstance;
    private double[][] matrixA;
    private double[][] matrixB;
    HashMap<String, Double> result;
    boolean ready = false;

    public static MultiplicationMatrixDataManager getShared() {
        if (shared == null) {
            shared = new MultiplicationMatrixDataManager();
        } else if (shared.hazelcastInstance == null) {
          System.out.println("Before using the matrix cache set the hazelcast instance!");
        }

      return shared;
    }

    public void fillMatricies() {
        if (ready) {
            return;
        }
        fillMatrixA();
        fillMatrixB();
        result = new HashMap<>();
        ready = true;
    }

    public void shareResults() {
      hazelcastInstance.getMap("result").put("size", matrixA.length);
      System.out.println("SHARING A MAP OF SIZE" + result.size());
      System.out.println("SHARING A MAP with value" + result.get("5-5"));

      hazelcastInstance.getMap("result").putAll(result);
      result.clear();
    }

    public void clear() {
        matrixA = null;
        matrixB = null;
        ready = false;
    }

    // Values are stored as "row-col"
    private void fillMatrixA() {
        com.hazelcast.map.IMap<String, Double>  map = hazelcastInstance.getMap("matrix_a");
        int size = map.get("size").intValue();
        Set<String> toGet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                toGet.add(i + "-" + j);
            }
        }
        Map<String, Double> values = map.getAll(toGet);
        matrixA = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixA[i][j] = values.get(i + "-" + j);
            }
        }
    }

    private void fillMatrixB() {
        com.hazelcast.map.IMap<String, Double>  map = hazelcastInstance.getMap("matrix_b");
        int size = map.get("size").intValue();
        Set<String> toGet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                toGet.add(i + "-" + j);
            }
        }
        Map<String, Double> values = map.getAll(toGet);
        matrixB = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixB[i][j] = values.get(i + "-" + j);
            }
        }
    }

  public double[][] getMatrixA() {
    if (!ready) {
      fillMatricies();
    }
    return matrixA;
  }

  public double[][] getMatrixB() {
      if (!ready) {
        fillMatricies();
      }
      return matrixB;
  }

  public double[][] getFinalResults() {

    com.hazelcast.map.IMap<String, Double> map = hazelcastInstance.getMap("result");
    int size = 512;
    double[][] result = new double[size][size];

    Set<String> toGet = new HashSet<>();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        toGet.add(i + "-" + j);
      }
    }
    Map<String, Double> values = map.getAll(toGet);
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (values.get(i + "-" + j) == null) {
          // fallback
          result[i][j] = -1;
        } else {
          result[i][j] = values.get(i + "-" + j);
        }
      }
    }
    return result;
  }
}

package org.example;

import com.hazelcast.core.HazelcastInstance;

import java.util.HashMap;
import java.util.Map;

public class MultiplicationTaskDataSharer {
    public static void addMatrix(HazelcastInstance instance, String key, double[][] matrix) {
        int size = matrix.length;
        com.hazelcast.map.IMap<String, Double> sharedMap = instance.getMap(key);
        sharedMap.put("size", (double) size);
        Map<String, Double> toPutAll = new HashMap<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                toPutAll.put(i + "-" + j, matrix[i][j]);
            }
        }
        sharedMap.putAll(toPutAll);
    }
}

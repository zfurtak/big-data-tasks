package org.example;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class MultiplicationTaskReceiver {

  Config config;

  HazelcastInstance instance;

  public MultiplicationTaskReceiver() {
    config = new XmlConfigBuilder(MultiplicationTaskReceiver.class.getResourceAsStream("/hazelcast.xml")).build();
    config.getSerializationConfig()
        .addSerializerConfig(
            new SerializerConfig()
                .setTypeClass(MatrixMultiplicationTask.class)
                .setImplementation(new MatrixMultiplicationTaskSerializer()
                )
        );
    instance = Hazelcast.newHazelcastInstance(config);
    MultiplicationMatrixDataManager.getShared().hazelcastInstance = instance;
  }

  public static void main(String[] args) {
    new MultiplicationTaskReceiver();
  }
}

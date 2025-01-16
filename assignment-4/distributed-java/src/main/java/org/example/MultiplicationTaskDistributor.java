package org.example;

import com.hazelcast.cluster.Member;
import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MultiplicationTaskDistributor {
  double[][] matrixA;
  double[][] matrixB;
  HazelcastInstance instance;
  public MultiplicationTaskDistributor(double[][] matrixA, double[][] matrixB, HazelcastInstance instance) {
    this.matrixA = matrixA;
    this.matrixB = matrixB;
    this.instance = instance;

    // Prepare data for other instances
    MultiplicationTaskDataSharer.addMatrix(instance, "matrix_a", matrixA);
    MultiplicationTaskDataSharer.addMatrix(instance, "matrix_b", matrixB);
    // Fill information for our instance
    MultiplicationMatrixDataManager.getShared().fillMatricies();
  }

  public void execute() {
    IExecutorService executor = instance.getExecutorService("matrix_executor");

    Member[] members = instance.getCluster().getMembers().toArray(new Member[0]);
    int memberTaskSize = matrixA.length / members.length;
    HashSet<Future<Void>> futures = new HashSet<>();
    for (int i = 0; i < matrixA.length; i += memberTaskSize) {
      MatrixMultiplicationTask multiplicationTask = new MatrixMultiplicationTask(i, Math.min(i + memberTaskSize, matrixA.length));
      Future<Void> future = executor.submitToMember(multiplicationTask, members[i % members.length]);
      futures.add(future);
    }

    for (Future<Void> future : futures) {
      try {
        future.get();
      } catch (ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
//
//  public static void main(String[] args) {
//    Config config = new XmlConfigBuilder(MultiplicationTaskReceiver.class.getResourceAsStream("/hazelcast.xml")).build();
//    config.getSerializationConfig()
//        .addSerializerConfig(
//            new SerializerConfig()
//                .setTypeClass(MatrixMultiplicationTask.class)
//                .setImplementation(new MatrixMultiplicationTaskSerializer()
//                )
//        );
//    HazelcastInstance myInstance = Hazelcast.newHazelcastInstance(config);
//    MultiplicationMatrixDataManager.getShared().hazelcastInstance = myInstance;
//
//    double[][] matrixA = utils.generateDoubleMatrix(512, false);
//    double[][] matrixB = utils.generateDoubleMatrix(512, false);
//
//    MultiplicationTaskDistributor distributor = new MultiplicationTaskDistributor(matrixA, matrixB, myInstance);
//
//    distributor.execute();
//  }
}

package org.example;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MultiplicationBenchmark {

  @Param({"1500"})
  public int n;
  private final Config config;
  MultiplicationTaskDistributor distributor;

  public MultiplicationBenchmark(){
    this.config = new XmlConfigBuilder(MultiplicationTaskReceiver.class.getResourceAsStream("/hazelcast.xml")).build();
    this.config.getSerializationConfig()
        .addSerializerConfig(
            new SerializerConfig()
                .setTypeClass(MatrixMultiplicationTask.class)
                .setImplementation(new MatrixMultiplicationTaskSerializer()
                )
        );
  }

  @Setup(Level.Trial)
  public void setupMatrix() {
    double[][] matrixA = utils.generateDoubleMatrix(n, false);
    double[][] matrixB = utils.generateDoubleMatrix(n, false);

    HazelcastInstance myInstance = Hazelcast.newHazelcastInstance(config);
    MultiplicationMatrixDataManager.getShared().hazelcastInstance = myInstance;
    distributor = new MultiplicationTaskDistributor(matrixA, matrixB, myInstance);
  }

  @Benchmark
  public void distributeAndCount() {
    distributor.execute();
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(".")
        .forks(1)
        .build();

    new Runner(opt).run();
  }
}

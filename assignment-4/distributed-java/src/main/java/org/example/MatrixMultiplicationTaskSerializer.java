package org.example;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

import java.io.IOException;

public class MatrixMultiplicationTaskSerializer implements StreamSerializer<MatrixMultiplicationTask> {
    @Override
    public int getTypeId() {
        return 70;
    }

    @Override
    public void write(ObjectDataOutput out, MatrixMultiplicationTask node) throws IOException {
        out.writeInt(node.getFrom());
        out.writeInt(node.getTo());
    }

    @Override
    public MatrixMultiplicationTask read(ObjectDataInput in) throws IOException {
        return new MatrixMultiplicationTask(in.readInt(), in.readInt());
    }

    @Override
    public void destroy() {
        // Optionally clean up resources
    }
}

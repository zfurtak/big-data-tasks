import hazelcast
import numpy as np
import time


def matrix_multiply_block(a_block, b_block):
    return np.dot(a_block, b_block)


def gather_results(client, num_partitions, block_size):
    result_map = client.get_map("result").blocking()
    result = np.zeros((block_size * num_partitions, block_size * num_partitions))

    for i in range(num_partitions):
        for j in range(num_partitions):
            sub_result = np.array(result_map.get(f"R_{i}_{j}"))
            result[
            i * block_size:(i + 1) * block_size,
            j * block_size:(j + 1) * block_size,
            ] = sub_result

    return result


def distribute_and_multiply_matrices(client, A, B, num_partitions):
    block_size = A.shape[0] // num_partitions

    assert A.shape[1] == B.shape[0], f"Incompatible dimensions: A{A.shape}, B{B.shape}"

    a_map = client.get_map("matrix_a").blocking()
    b_map = client.get_map("matrix_b").blocking()
    result_map = client.get_map("result").blocking()

    a_map.clear()
    b_map.clear()
    result_map.clear()

    for i in range(num_partitions):
        for j in range(num_partitions):
            a_block = A[i * block_size:(i + 1) * block_size, :]
            b_block = B[:, j * block_size:(j + 1) * block_size]
            a_map.put(f"A_{i}_{j}", a_block.tolist())
            b_map.put(f"B_{i}_{j}", b_block.tolist())

    for i in range(num_partitions):
        for j in range(num_partitions):
            result_block = np.zeros((block_size, block_size))

            for k in range(num_partitions):
                a_block = np.array(a_map.get(f"A_{i}_{k}"))
                b_block = np.array(b_map.get(f"B_{k}_{j}"))

                assert a_block.shape[1] == b_block.shape[0], f"Block shape mismatch: A{a_block.shape}, B{b_block.shape}"

                result_block += np.dot(a_block, b_block)

            result_map.put(f"R_{i}_{j}", result_block.tolist())

    result = np.zeros((A.shape[0], B.shape[1]))
    for i in range(num_partitions):
        for j in range(num_partitions):
            result_block = np.array(result_map.get(f"R_{i}_{j}"))
            result[i * block_size:(i + 1) * block_size, j * block_size:(j + 1) * block_size] = result_block

    return result, block_size


def benchmark_matrix_multiplication(sizes, num_partitions):
    client = hazelcast.HazelcastClient()

    for size in sizes:
        min_time = float('inf')
        max_time = float('-inf')
        total_time = 0
        num_of_iterations = 10

        for _ in range(num_of_iterations):
            A = np.random.rand(size, size)
            B = np.random.rand(size, size)

            start_time = time.time()
            _, block_size = distribute_and_multiply_matrices(client, A, B, num_partitions)
            result = gather_results(client, num_partitions, block_size)
            end_time = time.time()

            duration = end_time - start_time
            total_time += duration
            min_time = min(min_time, duration)
            max_time = max(max_time, duration)

        avg_time = total_time / num_of_iterations

        print(f"Size: {size}, Min time: {min_time:.4f}, Max time: {max_time:.4f}, Avg time: {avg_time:.4f}")

    client.shutdown()


if __name__ == "__main__":
    matrix_sizes = [100, 200, 400, 600]
    nodes = 2
    print("-------------------- Distributed matrix multiplication benchmark -------------------------")
    benchmark_matrix_multiplication(matrix_sizes, nodes)
    print("-------------------------------------------------------------------------------------------")

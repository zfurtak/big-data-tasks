import random
from memory_profiler import profile


def generate_matrix(size):
    return [[random.randint(1, 100) for _ in range(size)] for _ in range(size)]


@profile
def matrix_multiplication(matrix_a, matrix_b):
    rows_a = len(matrix_a)
    cols_a = len(matrix_a[0])
    rows_b = len(matrix_b)
    cols_b = len(matrix_b[0])

    if cols_a != rows_b:
        raise ValueError("Error: matrix_a should have the same number of columns as matrix_b has rows")

    result = [[0 for _ in range(cols_b)] for _ in range(rows_a)]

    for i in range(rows_a):
        for j in range(cols_b):
            for k in range(cols_a):
                result[i][j] += matrix_a[i][k] * matrix_b[k][j]
    return result


def test_my_function(benchmark):
    matrix_a = generate_matrix(150)
    matrix_b = generate_matrix(150)

    benchmark.pedantic(matrix_multiplication, args=(matrix_a, matrix_b,), iterations=5, rounds=2, warmup_rounds=1)

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void generate_matrix(int **matrix, int size) {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            matrix[i][j] = rand() % 10; // Random number between 0 and 9
        }
    }
}

void multiply_matrices(int **A, int **B, int **C, int size) {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            C[i][j] = 0;
            for (int k = 0; k < size; k++) {
                C[i][j] += A[i][k] * B[k][j];
            }
        }
    }
}
int compare_doubles(const void *a, const void *b) {
    double arg1 = *(const double *)a;
    double arg2 = *(const double *)b;
    return (arg1 > arg2) - (arg1 < arg2);
}

int main() {    
    int arr[] = {5, 10, 50, 100, 150, 200, 500};
    
    // Calculate the number of elements in the array
    int length = sizeof(arr) / sizeof(arr[0]);
    for (int i = 0; i < length; i++) {
      int size = arr[i];
       printf("size: %d:", size);
      
            srand(time(NULL)); // Seed for random number generation

    // Allocate memory for matrices
    int **A = (int **)malloc(size * sizeof(int *));
    int **B = (int **)malloc(size * sizeof(int *));
    int **C = (int **)malloc(size * sizeof(int *));
    for (int i = 0; i < size; i++) {
        A[i] = (int *)malloc(size * sizeof(int));
        B[i] = (int *)malloc(size * sizeof(int));
        C[i] = (int *)malloc(size * sizeof(int));
    }

    // Generate random matrices A and B
    generate_matrix(A, size);
    generate_matrix(B, size);

   // warmup
   for (int i = 0; i < 10; i++){
      multiply_matrices(A, B, C, size);
   }

   int iterations = 100;
   double times[iterations];
   double sum = 0, mean, min, max, median;
   for (int i = 0; i < iterations; i++){
      clock_t start = clock();
      multiply_matrices(A, B, C, size);
      clock_t end = clock();

      double time_taken = (((double)(end - start)) / CLOCKS_PER_SEC) * 1000; // milliseconds
      times[i] = time_taken;
      sum += time_taken;
    }

      mean = sum / iterations;
     min = times[0];
    max = times[0];
    for (int i = 1; i < iterations; i++) {
        if (times[i] < min) min = times[i];
        if (times[i] > max) max = times[i];
    }

    // Calculate median
    qsort(times, iterations, sizeof(double), compare_doubles);
    if (iterations % 2 == 0) {
        median = (times[iterations / 2 - 1] + times[iterations / 2]) / 2;
    } else {
        median = times[iterations / 2];
    }

        printf(" min: %.2f, max: %.2f, mean: %.2f, median: %.2f\n\n", min, max, mean, median);

    // Free allocated memory
    for (int i = 0; i < size; i++) {
        free(A[i]);
        free(B[i]);
        free(C[i]);
    }
    free(A);
    free(B);
    free(C);
    }


    return 0;
}
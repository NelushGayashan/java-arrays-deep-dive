package com.nelush.arrays.advanced;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * CacheFriendliness
 *
 * Demonstrates why arrays are significantly faster than pointer-linked
 * structures (like LinkedList) for iteration-heavy workloads, even when
 * the theoretical complexity is identical.
 *
 * The reason is CPU cache locality:
 *   - Array elements are contiguous in memory.
 *   - When the CPU loads one element it prefetches surrounding elements
 *     into a 64-byte cache line.
 *   - Subsequent element accesses hit the cache (L1/L2) at ~1-4 ns each.
 *
 *   - LinkedList nodes are scattered across the heap.
 *   - Each node dereference follows a pointer to a random memory location.
 *   - Cache miss => RAM access at ~100 ns each — ~50x slower.
 *
 * Both structures iterate in O(n), but the constant factor differs enormously.
 *
 * Row-major vs column-major access on 2-D arrays:
 *   Java stores int[][] row by row (row-major).
 *   Iterating row-by-row (arr[i][j]) is cache-friendly — sequential reads.
 *   Iterating column-by-column (arr[j][i]) thrashes the cache — strided reads.
 *
 */
public class CacheFriendliness {

    private static final int N = 2_000_000;

    // ─── ARRAY vs LINKEDLIST ITERATION ────────────────────────────────────────

    public static void compareIterationSpeed() {
        // Build both structures with the same data
        int[] array = new int[N];
        List<Integer> linked = new LinkedList<>();

        for (int i = 0; i < N; i++) {
            array[i] = i;
            linked.add(i);
        }

        // Warm up JIT
        long dummy = 0;
        for (int v : array) dummy += v;
        for (int v : linked) dummy += v;

        // Time array iteration
        long start = System.nanoTime();
        long sumArr = 0;
        for (int v : array) sumArr += v;
        long arrayMs = (System.nanoTime() - start) / 1_000_000;

        // Time LinkedList iteration
        start = System.nanoTime();
        long sumLink = 0;
        for (int v : linked) sumLink += v;
        long linkedMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("=== Cache Locality: Array vs LinkedList ===");
        System.out.printf("Array      (%,d elements): %d ms  (sum=%d)%n", N, arrayMs, sumArr);
        System.out.printf("LinkedList (%,d elements): %d ms  (sum=%d)%n", N, linkedMs, sumLink);
        System.out.printf("Ratio      LinkedList / Array: %.1fx slower%n",
            (double) linkedMs / Math.max(1, arrayMs));
        System.out.println("Both are O(n) — the difference is purely cache misses.");
    }

    // ─── ROW-MAJOR vs COLUMN-MAJOR ────────────────────────────────────────────

    /**
     * A 2-D Java array is laid out in row-major order in memory:
     *
     *   matrix[0][0], matrix[0][1], matrix[0][2], ...
     *   matrix[1][0], matrix[1][1], matrix[1][2], ...
     *
     * Row-major traversal reads sequential memory => cache-friendly.
     * Column-major traversal jumps by "row width" each step => cache-unfriendly.
     */
    public static void rowVsColumnMajor() {
        final int SIZE = 2000;
        int[][] matrix = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                matrix[i][j] = i * SIZE + j;

        // Row-major: iterate matrix[i][j]
        long start = System.nanoTime();
        long rowSum = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                rowSum += matrix[i][j];
        long rowMs = (System.nanoTime() - start) / 1_000_000;

        // Column-major: iterate matrix[j][i]
        start = System.nanoTime();
        long colSum = 0;
        for (int j = 0; j < SIZE; j++)
            for (int i = 0; i < SIZE; i++)
                colSum += matrix[i][j];
        long colMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("\n=== Row-Major vs Column-Major Traversal ===");
        System.out.printf("Row-major    (%dx%d): %d ms%n", SIZE, SIZE, rowMs);
        System.out.printf("Column-major (%dx%d): %d ms%n", SIZE, SIZE, colMs);
        System.out.printf("Column-major is %.1fx slower%n",
            (double) colMs / Math.max(1, rowMs));
        System.out.println("Always iterate 2-D arrays in row-major order.");
    }

    // ─── FALSE SHARING ILLUSTRATION (CONCEPTUAL) ──────────────────────────────

    /**
     * False sharing occurs when two threads write to different variables
     * that happen to share the same 64-byte cache line.
     * One thread's write invalidates the other's cached line.
     *
     * With arrays: adjacent array elements can share a cache line.
     * Solution: pad data or use striped access patterns.
     *
     * This method is illustrative — actual effects require multi-threaded
     * measurement in a controlled environment.
     */
    public static void falseSharingNote() {
        System.out.println("\n=== False Sharing (Conceptual) ===");
        System.out.println("A CPU cache line = 64 bytes = 16 ints.");
        System.out.println("arr[0] through arr[15] share ONE cache line.");
        System.out.println("If Thread-A writes arr[0] and Thread-B writes arr[1],");
        System.out.println("both writes invalidate the same cache line on the OTHER core.");
        System.out.println("Solution: separate hot data by >= 64 bytes (padding) OR use atomic.");

        // Demonstration of how many ints fit in a single cache line:
        int cacheLineBytes = 64;
        int intsPerLine    = cacheLineBytes / Integer.BYTES;
        System.out.println("Ints per cache line: " + intsPerLine);
        System.out.println("long[] per cache line: " + cacheLineBytes / Long.BYTES);
    }

    public static void main(String[] args) {
        compareIterationSpeed();
        rowVsColumnMajor();
        falseSharingNote();
    }
}

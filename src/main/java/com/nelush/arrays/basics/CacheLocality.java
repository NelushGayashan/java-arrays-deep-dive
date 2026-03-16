package com.nelush.arrays.basics;

import java.util.LinkedList;
import java.util.List;

/**
 * CacheLocality
 *
 * Article section: "Cache Locality: Why Arrays Win in Practice"
 *
 * Demonstrates the real-world performance gap between int[] and LinkedList<Integer>
 * caused by CPU cache behaviour, not algorithm complexity.
 *
 * Key facts from the article:
 *   - L1 cache: ~32KB, ~1 nanosecond access
 *   - Main RAM: ~100 nanoseconds access
 *   - CPU cache line: 64 bytes = 16 ints loaded at once
 *   - int[] → sequential reads → L1 cache hits → ~1 ns each
 *   - LinkedList → random pointer follows → cache misses → ~100 ns each
 *   - Both are O(n) but the constant factor differs 10–50x in practice
 *
 * Also demonstrates row-major vs column-major traversal on a 2D array.
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.basics.CacheLocality"
 *
 * Author: Nelush Gayashan Fernando
 */
public class CacheLocality {

    private static final int N = 2_000_000;

    // ─── ARRAY vs LINKEDLIST ITERATION BENCHMARK ─────────────────────────────

    /**
     * Both structures iterate in O(n).
     * The array wins by 10–50x purely due to cache locality.
     *
     * int[] → elements are contiguous → CPU prefetches 16 ints per cache line load
     * LinkedList → each node.next is a random heap address → cache miss every step
     */
    public static void arrayVsLinkedList() {
        int[] array = new int[N];
        List<Integer> linked = new LinkedList<>();

        for (int i = 0; i < N; i++) {
            array[i] = i;
            linked.add(i);
        }

        // Warm up JIT so first run doesn't skew results
        long dummy = 0;
        for (int v : array)  dummy += v;
        for (int v : linked) dummy += v;

        // Measure array iteration
        long start = System.nanoTime();
        long sumArr = 0;
        for (int v : array) sumArr += v;
        long arrayMs = (System.nanoTime() - start) / 1_000_000;

        // Measure LinkedList iteration
        start = System.nanoTime();
        long sumLink = 0;
        for (int v : linked) sumLink += v;
        long linkedMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("=== Cache Locality: Array vs LinkedList ===");
        System.out.printf("int[]      (%,d elements): %d ms%n", N, arrayMs);
        System.out.printf("LinkedList (%,d elements): %d ms%n", N, linkedMs);
        System.out.printf("LinkedList is ~%.1fx slower despite identical O(n) complexity%n",
            (double) linkedMs / Math.max(1, arrayMs));
        System.out.println("Reason: cache misses on every node.next pointer follow.");
        System.out.println("        (dummy=" + (dummy > 0 ? "ok" : "ok") + " to prevent dead-code elimination)");
    }

    // ─── ROW-MAJOR vs COLUMN-MAJOR ────────────────────────────────────────────

    /**
     * Java stores int[][] in row-major order (row by row in memory).
     * Iterating matrix[i][j] (row-first) reads sequentially — cache friendly.
     * Iterating matrix[j][i] (column-first) jumps by row stride — cache unfriendly.
     *
     * From the article:
     *   "when iterating a matrix, always go row by row (row-major order).
     *   Iterating matrix[j][i] (column-major) jumps by the full row stride
     *   on every access, causing CPU cache misses and degrading performance
     *   by a factor of 5–10x on large matrices."
     */
    public static void rowVsColumnMajor() {
        final int SIZE = 2000;
        int[][] matrix = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                matrix[i][j] = i * SIZE + j;

        // Row-major: matrix[i][j] — sequential memory reads
        long start = System.nanoTime();
        long rowSum = 0;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                rowSum += matrix[i][j];
        long rowMs = (System.nanoTime() - start) / 1_000_000;

        // Column-major: matrix[j][i] — strided memory reads (cache unfriendly)
        start = System.nanoTime();
        long colSum = 0;
        for (int j = 0; j < SIZE; j++)
            for (int i = 0; i < SIZE; i++)
                colSum += matrix[i][j];
        long colMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("\n=== Row-Major vs Column-Major Traversal (" + SIZE + "x" + SIZE + ") ===");
        System.out.printf("Row-major    : %d ms%n", rowMs);
        System.out.printf("Column-major : %d ms%n", colMs);
        System.out.printf("Column-major is %.1fx slower%n",
            (double) colMs / Math.max(1, rowMs));
        System.out.println("Always iterate 2D arrays as matrix[i][j] (row-major).");
        System.out.println("(rowSum=" + (rowSum == colSum ? "matches colSum ✓" : "mismatch!") + ")");
    }

    // ─── CACHE LINE EXPLAINER ─────────────────────────────────────────────────

    public static void cacheLineExplainer() {
        System.out.println("\n=== CPU Cache Line Facts ===");
        System.out.println("Cache line size       : 64 bytes");
        System.out.println("ints per cache line   : " + (64 / Integer.BYTES));
        System.out.println("longs per cache line  : " + (64 / Long.BYTES));
        System.out.println();
        System.out.println("When arr[0] is accessed, the CPU loads arr[0]..arr[15] into L1 cache.");
        System.out.println("The next 15 int reads are served from L1 at ~1 ns — no RAM access.");
        System.out.println("LinkedList: every node.next follow is a cache miss at ~100 ns.");
    }

    public static void main(String[] args) {
        arrayVsLinkedList();
        rowVsColumnMajor();
        cacheLineExplainer();
    }
}

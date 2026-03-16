package com.nelush.arrays.basics;

import java.util.Arrays;

/**
 * ArrayMemoryModel
 *
 * Article section: "What an Array Actually Is" and
 * "Primitive Arrays vs Object Arrays at the JVM Level"
 *
 * Demonstrates:
 *   - The contiguous-memory layout and O(1) address formula
 *   - Primitive array vs object (reference) array
 *   - Zero-initialisation guarantee
 *   - Array covariance and ArrayStoreException
 *   - Multi-dimensional (jagged) arrays
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.basics.ArrayMemoryModel"
 *
 * Author: Nelush Gayashan Fernando
 */
public class ArrayMemoryModel {

    // ─── SECTION: What an Array Actually Is ──────────────────────────────────

    /**
     * An array is a contiguous block of memory.
     * Address formula:  address(arr[i]) = base_address + (i × element_size)
     *
     * For int[] element_size = 4 bytes:
     *   arr[0] → base + 0  = 0x1000
     *   arr[1] → base + 4  = 0x1004
     *   arr[2] → base + 8  = 0x1008
     *
     * One multiply + one add = one CPU instruction = O(1) regardless of array size.
     * The CPU does NOT traverse elements 0 and 1 to reach element 2.
     */
    public static void addressFormulaDemo() {
        int[] arr = {10, 20, 30, 40, 50};

        System.out.println("=== Address Formula — O(1) Access ===");
        System.out.println("arr[2] = " + arr[2]
            + "  (base + 2×4 bytes — one CPU instruction)");

        // Each iteration is O(1) — not O(i). The index jump is computed, not walked.
        for (int i = 0; i < arr.length; i++) {
            System.out.printf("  arr[%d] = %d  offset = %d bytes%n",
                i, arr[i], i * Integer.BYTES);
        }
    }

    // ─── SECTION: Primitive Arrays vs Object Arrays ───────────────────────────

    /**
     * Primitive int[] — raw 32-bit integers packed consecutively:
     *   [ 10 | 20 | 30 ]   (12 bytes total, no indirection)
     *
     * Object Integer[] — 8-byte heap references packed consecutively:
     *   [ ref0 | ref1 | ref2 ]
     *      |        |       |
     *   Integer(10) Integer(20) Integer(30)   (scattered on heap)
     *
     * Accessing obj[i] needs TWO memory reads (reference + object).
     * Still O(1) but may incur a CPU cache miss on the second read.
     */
    public static void primitiveVsObjectArray() {
        // Primitive: raw values, contiguous, no heap objects per element
        int[] primitives = {10, 20, 30};

        // Object: references to separate heap objects
        Integer[] objects = {10, 20, 30};

        System.out.println("\n=== Primitive vs Object Array ===");
        System.out.println("primitives[0] = " + primitives[0] + "  (direct value read)");
        System.out.println("objects[0]    = " + objects[0]    + "  (reference → heap object)");

        // null is a valid reference value
        Integer[] withNull = {10, null, 30};
        System.out.println("withNull[1]   = " + withNull[1]); // null — safe
        // withNull[1].intValue();  // would throw NullPointerException
    }

    // ─── SECTION: Zero-Initialisation Guarantee ──────────────────────────────

    /**
     * Java zero-initialises every array slot at allocation time.
     * This is a JVM guarantee — you never read uninitialised memory.
     * Unlike C/C++ where uninitialised arrays contain garbage.
     *
     *   int[]     → 0          long[]    → 0L
     *   double[]  → 0.0        float[]   → 0.0f
     *   boolean[] → false      char[]    → '\u0000'
     *   Object[]  → null
     */
    public static void zeroInitialisationDemo() {
        int[]     ints    = new int[3];
        double[]  doubles = new double[3];
        boolean[] bools   = new boolean[3];
        String[]  strings = new String[3];

        System.out.println("\n=== Zero-Initialisation (JVM Guarantee) ===");
        System.out.println("int[]     : " + Arrays.toString(ints));
        System.out.println("double[]  : " + Arrays.toString(doubles));
        System.out.println("boolean[] : " + Arrays.toString(bools));
        System.out.println("String[]  : " + Arrays.toString(strings));
    }

    // ─── SECTION: Array Covariance ────────────────────────────────────────────

    /**
     * Arrays are covariant: Dog[] is assignable to Animal[].
     * The JVM enforces type safety at runtime with ArrayStoreException.
     *
     * From the article:
     *   String[] strings = {"hello", "world"};
     *   Object[] objects = strings;   // compiles — covariant
     *   objects[0] = "updated";       // fine — actual type is String[]
     *   objects[0] = 42;              // RUNTIME: ArrayStoreException
     */
    public static void covarianceDemo() {
        String[] strings = {"hello", "world"};
        Object[] objects = strings; // compiles — covariant assignment

        objects[0] = "updated"; // OK — actual element type is String
        System.out.println("\n=== Array Covariance ===");
        System.out.println("objects[0] after update = " + objects[0]);

        try {
            objects[0] = 42; // RUNTIME: ArrayStoreException
        } catch (ArrayStoreException e) {
            System.out.println("ArrayStoreException caught (expected): "
                + "cannot store Integer into String[]");
        }
    }

    // ─── SECTION: Multi-Dimensional Arrays Are Not What You Think ─────────────

    /**
     * int[][] is NOT a true 2D block. It is an array of int[] references.
     * Each row is a separate heap object — rows can have different lengths.
     *
     *   matrix --> [ ref0 | ref1 | ref2 ]
     *                 |       |       |
     *              int[4]  int[4]  int[4]   (separate heap objects)
     *
     * Always iterate row-major (matrix[i][j]) not column-major (matrix[j][i]).
     * Column-major causes cache misses and can be 5–10x slower on large matrices.
     */
    public static void multiDimensionalDemo() {
        // Standard 2D matrix
        int[][] matrix = new int[3][4];
        matrix[0][0] = 1;
        matrix[2][3] = 99;

        System.out.println("\n=== Multi-Dimensional Arrays (Jagged) ===");
        System.out.println("3×4 matrix (row-major iteration):");
        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("  row[%d] = %s%n", i, Arrays.toString(matrix[i]));
        }

        // Jagged array — rows with different lengths, fully legal in Java
        int[][] jagged = {
            {1},
            {2, 3},
            {4, 5, 6}
        };
        System.out.println("Jagged array lengths: "
            + jagged[0].length + ", " + jagged[1].length + ", " + jagged[2].length);
        // jagged[0].length == 1, jagged[1].length == 2, jagged[2].length == 3
    }

    public static void main(String[] args) {
        addressFormulaDemo();
        primitiveVsObjectArray();
        zeroInitialisationDemo();
        covarianceDemo();
        multiDimensionalDemo();
    }
}

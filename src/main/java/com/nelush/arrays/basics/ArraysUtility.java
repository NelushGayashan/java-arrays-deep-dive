package com.nelush.arrays.basics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * ArraysUtility
 *
 * Article section: "The Complete java.util.Arrays Toolkit"
 *
 * Every method demonstrated here maps directly to the article's code examples:
 *   - sort (Dual-Pivot Quicksort for primitives, TimSort for objects)
 *   - binarySearch (requires sorted array, negative return = insertion point)
 *   - copyOf / copyOfRange / System.arraycopy
 *   - fill (DP table initialisation pattern)
 *   - equals / deepEquals
 *   - stream integration (sum, max, filter, boxing)
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.basics.ArraysUtility"
 *
 * Author: Nelush Gayashan Fernando
 */
public class ArraysUtility {

    // ─── SORTING ──────────────────────────────────────────────────────────────

    /**
     * Arrays.sort on primitives → Dual-Pivot Quicksort (O(n log n), excellent cache perf)
     * Arrays.sort on objects   → TimSort (stable, handles partially-sorted data well)
     *
     * From the article:
     *   "Sorting is the most commonly used operation."
     */
    public static void sortingExamples() {
        System.out.println("=== Sorting ===");

        int[] nums = {5, 3, 1, 4, 2};
        Arrays.sort(nums); // Dual-Pivot Quicksort
        System.out.println("Sorted primitives : " + Arrays.toString(nums));
        // [1, 2, 3, 4, 5]

        String[] words = {"banana", "apple", "cherry"};
        Arrays.sort(words); // TimSort (stable)
        System.out.println("Sorted strings    : " + Arrays.toString(words));
        // ["apple", "banana", "cherry"]

        // Custom comparator — only available for object arrays
        String[] byLength = {"banana", "fig", "apple"};
        Arrays.sort(byLength, Comparator.comparingInt(String::length));
        System.out.println("By length         : " + Arrays.toString(byLength));
        // ["fig", "apple", "banana"]

        // Sort a subrange [1, 4) — only indices 1, 2, 3
        int[] partial = {9, 5, 3, 7, 1, 8};
        Arrays.sort(partial, 1, 4);
        System.out.println("Subrange sort [1,4): " + Arrays.toString(partial));
        // [9, 3, 5, 7, 1, 8]
    }

    // ─── BINARY SEARCH ────────────────────────────────────────────────────────

    /**
     * Requires a SORTED array. Returns:
     *   index of target if found
     *   -(insertion_point) - 1 if not found
     *
     * From the article:
     *   "Never call binarySearch on an unsorted array — results are undefined
     *   and you will get wrong answers silently."
     */
    public static void binarySearchExamples() {
        System.out.println("\n=== Binary Search ===");

        int[] sorted = {10, 20, 30, 40, 50};

        int idx = Arrays.binarySearch(sorted, 30);
        System.out.println("Search 30 → index " + idx); // 2

        int missing = Arrays.binarySearch(sorted, 25);
        System.out.println("Search 25 → " + missing);   // -3 (not found)
        System.out.println("  insertion point = " + (-(missing) - 1)); // 2
        // insertion_point = -(missing) - 1 = -(-3) - 1 = 2
    }

    // ─── COPYING ──────────────────────────────────────────────────────────────

    /**
     * Arrays.copyOf   — full or grown/shrunk copy (new independent array)
     * Arrays.copyOfRange — subrange copy
     * System.arraycopy — fastest bulk copy (JVM intrinsic)
     *
     * From the article:
     *   "Copying creates a new array without sharing memory with the original."
     */
    public static void copyingExamples() {
        System.out.println("\n=== Copying ===");

        int[] original = {1, 2, 3, 4, 5};

        int[] copy = Arrays.copyOf(original, original.length); // full copy
        copy[0] = 99;
        System.out.println("original after copy[0]=99 : " + Arrays.toString(original)); // unchanged
        System.out.println("copy                       : " + Arrays.toString(copy));

        // Grow — extra slots zero-filled
        int[] grown = Arrays.copyOf(original, 8);
        System.out.println("Grown to length 8          : " + Arrays.toString(grown));
        // [1, 2, 3, 4, 5, 0, 0, 0]

        // Subrange [1, 4)
        int[] sub = Arrays.copyOfRange(original, 1, 4);
        System.out.println("copyOfRange(1,4)            : " + Arrays.toString(sub));
        // [2, 3, 4]

        // System.arraycopy — fastest for large arrays (JVM intrinsic)
        int[] dest = new int[5];
        System.arraycopy(original, 0, dest, 0, original.length);
        System.out.println("System.arraycopy result    : " + Arrays.toString(dest));
    }

    // ─── FILL ─────────────────────────────────────────────────────────────────

    /**
     * Common DP table initialisation pattern from the article.
     */
    public static void fillExamples() {
        System.out.println("\n=== Fill ===");

        // DP table initialisation — from the article
        int[] dp = new int[10];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        System.out.println("DP table (MAX_VALUE sentinel): " + Arrays.toString(dp));

        // Subrange fill
        int[] partial = new int[6];
        Arrays.fill(partial, 2, 5, 99); // positions 2,3,4 become 99
        System.out.println("fill subrange [2,5) → 99    : " + Arrays.toString(partial));
    }

    // ─── EQUALITY ─────────────────────────────────────────────────────────────

    /**
     * == compares references (object identity).
     * Arrays.equals compares contents element-by-element.
     * Arrays.deepEquals handles nested arrays.
     *
     * From the article:
     *   "Equality comparisons must use Arrays.equals, not ==."
     */
    public static void equalityExamples() {
        System.out.println("\n=== Equality ===");

        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};

        System.out.println("a == b                : " + (a == b));              // false
        System.out.println("Arrays.equals(a, b)   : " + Arrays.equals(a, b));   // true

        int[][] a2d = {{1, 2}, {3, 4}};
        int[][] b2d = {{1, 2}, {3, 4}};
        System.out.println("Arrays.equals(2D)     : " + Arrays.equals(a2d, b2d));     // false (shallow)
        System.out.println("Arrays.deepEquals(2D) : " + Arrays.deepEquals(a2d, b2d)); // true  (deep)
    }

    // ─── STREAM INTEGRATION ───────────────────────────────────────────────────

    /**
     * Arrays.stream(arr) returns an IntStream for primitives.
     * Provides sum, max, filter, boxing, etc.
     *
     * From the article:
     *   "Stream integration connects arrays to the Java Streams API."
     */
    public static void streamExamples() {
        System.out.println("\n=== Stream Integration ===");

        int[] nums = {3, 1, 4, 1, 5, 9, 2, 6};

        int sum    = Arrays.stream(nums).sum();
        int max    = Arrays.stream(nums).max().getAsInt();
        long evens = Arrays.stream(nums).filter(n -> n % 2 == 0).count();

        System.out.println("sum   = " + sum);
        System.out.println("max   = " + max);
        System.out.println("evens = " + evens);

        // Convert int[] to Integer[] via stream boxing
        Integer[] boxed = Arrays.stream(nums).boxed().toArray(Integer[]::new);
        System.out.println("boxed = " + Arrays.toString(boxed));
    }

    // ─── TOSTRING ─────────────────────────────────────────────────────────────

    public static void toStringExamples() {
        System.out.println("\n=== toString / deepToString ===");
        int[] arr = {1, 2, 3};
        System.out.println("Direct print    : " + arr);              // [I@... (ugly)
        System.out.println("Arrays.toString : " + Arrays.toString(arr)); // [1, 2, 3]

        int[][] matrix = {{1, 2}, {3, 4}};
        System.out.println("deepToString    : " + Arrays.deepToString(matrix)); // [[1,2],[3,4]]
    }

    public static void main(String[] args) {
        sortingExamples();
        binarySearchExamples();
        copyingExamples();
        fillExamples();
        equalityExamples();
        streamExamples();
        toStringExamples();
    }
}

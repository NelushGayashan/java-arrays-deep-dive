package com.nelush.arrays.basics;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * ============================================================
 * SECTION 2: Array Operations and the Java Arrays API
 * ============================================================
 *
 * CREATION AND INITIALIZATION
 *
 * Three syntactic forms exist for declaring arrays, and understanding
 * when to use each one matters both for readability and for passing
 * arrays into methods.
 *
 * int[] a = new int[5];          — fixed-size, all zeroes
 * int[] b = {1, 2, 3};          — inline literal, size inferred
 * int[] c = new int[]{1, 2, 3}; — explicit form, required when
 *                                   passing an array literal as an
 *                                   argument inline, e.g.:
 *                                   process(new int[]{1, 2, 3})
 *
 * INSERTION COST
 * Inserting at the END of an array (when space exists) is O(1) —
 * just write to the next index. But inserting at the BEGINNING
 * or MIDDLE requires shifting every element to the right of the
 * insertion point:
 *
 *   Before: [1, 2, 3, 4, 5]
 *   Insert 99 at index 1 →
 *   Shift:  [1, ?, 2, 3, 4, 5]  (shift indices 1–4 right by one)
 *   Write:  [1, 99, 2, 3, 4, 5]
 *
 * In the worst case (insert at index 0), every element shifts — O(n).
 *
 * DELETION COST
 * Deletion from the middle also requires shifting — every element
 * to the right of the deleted position moves one place left.
 * After deletion there is a gap at the end that must be zeroed or
 * ignored via a size counter.
 *
 * THE java.util.Arrays CLASS
 * Arrays is the utility class for all common array operations.
 * It is part of java.util and covers sorting, searching, copying,
 * filling, equality, string representation, and stream conversion.
 * Mastering this class eliminates the need to hand-roll many
 * operations that interviewers nonetheless expect you to understand
 * at the algorithmic level.
 *
 * SORTING ALGORITHMS USED BY JAVA
 * Arrays.sort(int[])       — Dual-Pivot Quicksort (introduced Java 7)
 *   Average: O(n log n), in-place, outperforms classical quicksort
 *   on nearly-sorted data.
 *
 * Arrays.sort(Object[])    — TimSort (hybrid merge + insertion sort)
 *   Stable, O(n log n) worst case, O(n) on nearly-sorted data.
 *   Stability matters when sorting by one field of objects that
 *   already have a different ordering by another field.
 *
 * Arrays.parallelSort()    — Fork/Join parallel merge sort for large
 *   arrays. Worth using when array size > ~8 000 elements and
 *   a multi-core machine is available.
 */
public class ArrayOperations {

    // -------------------------------------------------------
    // Manual insertion — shift elements right, then write
    // Returns new logical size (the physical array is unchanged)
    // -------------------------------------------------------

    /**
     * Inserts {@code value} at {@code index} inside {@code arr}.
     * Elements from {@code index} to {@code logicalSize - 1} are
     * shifted one place to the right.
     *
     * @param arr         the backing array (must have capacity for one more element)
     * @param logicalSize current number of valid elements
     * @param index       position to insert at (0 ≤ index ≤ logicalSize)
     * @param value       value to insert
     * @return            new logical size
     */
    public static int insertAt(int[] arr, int logicalSize, int index, int value) {
        // Shift elements rightward from the end toward 'index'
        // We walk backward to avoid overwriting an element before we have moved it
        for (int i = logicalSize - 1; i >= index; i--) {
            arr[i + 1] = arr[i];
        }
        arr[index] = value;
        return logicalSize + 1;
    }

    /**
     * Deletes the element at {@code index} by shifting subsequent
     * elements one position left, then zeroing the vacated slot.
     *
     * @return new logical size
     */
    public static int deleteAt(int[] arr, int logicalSize, int index) {
        for (int i = index; i < logicalSize - 1; i++) {
            arr[i] = arr[i + 1]; // shift left
        }
        arr[logicalSize - 1] = 0; // clear vacated slot
        return logicalSize - 1;
    }

    // -------------------------------------------------------
    // Linear search — O(n) — no precondition on ordering
    // -------------------------------------------------------

    /**
     * Returns the first index where {@code target} appears, or -1.
     * Works on any array regardless of ordering.
     */
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i;
        }
        return -1;
    }

    // -------------------------------------------------------
    // The java.util.Arrays API — all major methods demonstrated
    // -------------------------------------------------------

    public static void arraysApiDemo() {
        // --- sort ---
        // Primitive array → Dual-Pivot Quicksort, in-place, O(n log n)
        int[] nums = {5, 3, 8, 1, 9, 2};
        Arrays.sort(nums);
        System.out.println("Sorted : " + Arrays.toString(nums));  // [1, 2, 3, 5, 8, 9]

        // Sort a subrange [fromIndex, toIndex)  — only indices 1..3
        int[] partial = {5, 3, 8, 1, 9, 2};
        Arrays.sort(partial, 1, 4); // sorts indices 1, 2, 3
        System.out.println("Partial: " + Arrays.toString(partial)); // [5, 1, 3, 8, 9, 2]

        // Object array → TimSort (stable)
        String[] words = {"banana", "apple", "cherry", "date"};
        Arrays.sort(words);
        System.out.println("Strings: " + Arrays.toString(words));  // [apple, banana, cherry, date]

        // Sort with Comparator (only for object arrays)
        Arrays.sort(words, (a, b) -> b.compareTo(a)); // descending
        System.out.println("Desc   : " + Arrays.toString(words));

        // --- binarySearch ---
        // PRECONDITION: array must be sorted in ascending order first.
        // Returns index if found; if not found, returns -(insertion point) - 1
        int[] sorted = {1, 3, 5, 7, 9, 11};
        System.out.println("Found 7  at idx: " + Arrays.binarySearch(sorted, 7));   //  3
        System.out.println("Missing 6 gives: " + Arrays.binarySearch(sorted, 6));   // -4

        // --- copyOf ---
        // Creates a NEW array; modifying it does not affect the original.
        // If newLength > original.length, extra slots are zero-filled.
        int[] original = {1, 2, 3, 4, 5};
        int[] copy     = Arrays.copyOf(original, original.length); // exact copy
        int[] extended = Arrays.copyOf(original, 8);               // [1,2,3,4,5,0,0,0]
        int[] shorter  = Arrays.copyOf(original, 3);               // [1,2,3]

        // --- copyOfRange ---
        // Copies [from, to) — 'to' can exceed length (zero-padded)
        int[] slice = Arrays.copyOfRange(original, 1, 4); // [2,3,4]
        System.out.println("Slice  : " + Arrays.toString(slice));

        // --- fill ---
        int[] filled = new int[5];
        Arrays.fill(filled, 7);              // [7,7,7,7,7]
        Arrays.fill(filled, 1, 4, 0);        // fill only [1,4): [7,0,0,0,7]
        System.out.println("Filled : " + Arrays.toString(filled));

        // --- equals / deepEquals ---
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        int[] c = {1, 2, 4};
        System.out.println("a equals b: " + Arrays.equals(a, b)); // true
        System.out.println("a equals c: " + Arrays.equals(a, c)); // false

        // Note: a.equals(b) uses Object.equals — checks reference, always false
        // ALWAYS use Arrays.equals() for content comparison

        int[][] m1 = {{1, 2}, {3, 4}};
        int[][] m2 = {{1, 2}, {3, 4}};
        System.out.println("m1 deepEquals m2: " + Arrays.deepEquals(m1, m2)); // true

        // --- toString / deepToString ---
        System.out.println(Arrays.toString(a));              // [1, 2, 3]
        System.out.println(Arrays.deepToString(m1));         // [[1, 2], [3, 4]]

        // --- stream ---
        // Converts int[] to IntStream for functional-style operations
        int sum = Arrays.stream(original).sum();
        int max = Arrays.stream(original).max().getAsInt();
        long count = Arrays.stream(original).filter(x -> x % 2 == 0).count();
        int[] doubled = Arrays.stream(original).map(x -> x * 2).toArray();
        System.out.printf("Sum=%d  Max=%d  EvenCount=%d%n", sum, max, count);
        System.out.println("Doubled: " + Arrays.toString(doubled));
    }

    // -------------------------------------------------------
    // System.arraycopy — the fastest element-copy in Java
    // -------------------------------------------------------

    /**
     * System.arraycopy is a native JVM method that copies blocks of
     * memory directly, bypassing the per-element overhead of a Java loop.
     * It is significantly faster than manual loops or Arrays.copyOf for
     * large arrays.
     *
     * Signature:
     *   System.arraycopy(src, srcPos, dest, destPos, length)
     *
     * This method is used internally by ArrayList.add() and ArrayList.remove()
     * for the shifting it must perform on its backing array.
     */
    public static void systemArrayCopyDemo() {
        int[] src  = {1, 2, 3, 4, 5};
        int[] dest = new int[7];

        // Copy 3 elements starting at src[1] into dest starting at dest[2]
        System.arraycopy(src, 1, dest, 2, 3);
        System.out.println("arraycopy: " + Arrays.toString(dest)); // [0, 0, 2, 3, 4, 0, 0]

        // Self-copy to shift right (the idiom used inside deleteAt above)
        int[] arr = {1, 2, 3, 4, 5};
        // Shift arr[1..4] one position right to make room at index 1
        System.arraycopy(arr, 1, arr, 2, arr.length - 2);
        arr[1] = 99;
        System.out.println("After insert via arraycopy: " + Arrays.toString(arr)); // [1, 99, 2, 3, 4]
    }

    public static void main(String[] args) {
        System.out.println("=== Insertion and Deletion ===");
        int[] arr = new int[8];
        arr[0] = 1; arr[1] = 2; arr[2] = 3; arr[3] = 4;
        int size = 4;
        size = insertAt(arr, size, 1, 99);
        System.out.println("After insert: " + Arrays.toString(Arrays.copyOf(arr, size)));
        size = deleteAt(arr, size, 1);
        System.out.println("After delete: " + Arrays.toString(Arrays.copyOf(arr, size)));

        System.out.println("\n=== Linear Search ===");
        int[] data = {10, 30, 50, 70, 90};
        System.out.println("50 at index: " + linearSearch(data, 50));
        System.out.println("40 at index: " + linearSearch(data, 40));

        System.out.println("\n=== Arrays API Demo ===");
        arraysApiDemo();

        System.out.println("\n=== System.arraycopy ===");
        systemArrayCopyDemo();
    }
}

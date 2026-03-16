package com.nelush.arrays.algorithms;

import java.util.Arrays;

/**
 * SortingAlgorithms
 *
 * Article section: "Sorting Algorithms — What Actually Happens"
 *
 * From the article:
 *   "Understanding the major sorting algorithms matters not just for theory but for
 *   choosing the right one and for reasoning about what Arrays.sort() is doing internally."
 *
 * Algorithms covered:
 *   - BubbleSort    O(n²) worst, O(n) best (early exit), stable
 *   - InsertionSort O(n²) worst, O(n) best (nearly sorted), stable
 *   - MergeSort     O(n log n) always, stable, O(n) extra space
 *   - QuickSort     O(n log n) avg, O(n²) worst, in-place
 *   - CountingSort  O(n+k), for bounded integers
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.algorithms.SortingAlgorithms"
 *
 * Author: Nelush Gayashan Fernando
 */
public class SortingAlgorithms {

    // ─── BUBBLE SORT ──────────────────────────────────────────────────────────

    /**
     * From the article:
     * "compares adjacent elements and swaps them if out of order. After each full pass,
     * the largest unsorted element has bubbled to its correct position. With the early-exit
     * optimisation (no swap in a pass means sorted), it achieves O(n) on already-sorted input."
     */
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break; // no swaps → already sorted (O(n) early exit)
        }
    }

    // ─── INSERTION SORT ───────────────────────────────────────────────────────

    /**
     * From the article:
     * "For each new element, shift sorted elements rightward until the insertion point
     * is found. It is excellent for nearly-sorted data (few shifts) and is used by
     * TimSort for small partitions of a few tens of elements."
     */
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i]; // element to be inserted
            int j   = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j]; // shift right
                j--;
            }
            arr[j + 1] = key; // insert at correct position
        }
    }

    // ─── MERGE SORT ───────────────────────────────────────────────────────────

    /**
     * Exact code from the article.
     *
     * "Split in half, sort each recursively, merge the two sorted halves.
     * The merge step is O(n) and the recursion depth is O(log n), giving O(n log n) always.
     * It is stable and predictable — no worst-case degradation."
     */
    public static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right); // O(n) merge step
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] tmp = Arrays.copyOfRange(arr, left, right + 1);
        int i = 0, j = mid - left + 1, k = left;

        while (i <= mid - left && j <= right - left) {
            if (tmp[i] <= tmp[j]) arr[k++] = tmp[i++]; // <= preserves stability
            else                  arr[k++] = tmp[j++];
        }
        while (i <= mid - left)  arr[k++] = tmp[i++];
        while (j <= right - left) arr[k++] = tmp[j++];
    }

    // ─── QUICK SORT ───────────────────────────────────────────────────────────

    /**
     * From the article:
     * "Pivot choice is critical — a bad pivot (always the minimum or maximum) degrades
     * to O(n²). In practice with random or median-of-three pivot selection, QuickSort
     * is the fastest comparison sort due to excellent cache performance and low constant factors."
     */
    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int pivotIdx = partition(arr, left, right);
        quickSort(arr, left, pivotIdx - 1);
        quickSort(arr, pivotIdx + 1, right);
    }

    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[right]; // Lomuto partition — last element as pivot
        int i     = left - 1;  // boundary: arr[left..i] <= pivot

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        int tmp = arr[i + 1]; arr[i + 1] = arr[right]; arr[right] = tmp;
        return i + 1;
    }

    // ─── SELECTION SORT ───────────────────────────────────────────────────────

    /**
     * Find minimum of unsorted region, swap to boundary.
     * Always n-1 swaps — fewest writes of any O(n²) sort.
     */
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++)
                if (arr[j] < arr[minIdx]) minIdx = j;
            if (minIdx != i) {
                int tmp = arr[i]; arr[i] = arr[minIdx]; arr[minIdx] = tmp;
            }
        }
    }

    // ─── COUNTING SORT ────────────────────────────────────────────────────────

    /**
     * From the article:
     * "Count occurrences of each value, compute prefix sums to find starting positions,
     * then place each element. O(n+k) where k is the value range — faster than O(n log n)
     * comparison sorts when k is small."
     */
    public static int[] countingSort(int[] arr, int maxVal) {
        int[] count  = new int[maxVal + 1];
        int[] output = new int[arr.length];

        for (int v : arr) count[v]++;                          // step 1: count
        for (int i = 1; i <= maxVal; i++) count[i] += count[i - 1]; // step 2: prefix sum
        for (int i = arr.length - 1; i >= 0; i--)             // step 3: place (reverse for stability)
            output[--count[arr[i]]] = arr[i];
        return output;
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Sorting Algorithms ===\n");
        int[] base = {5, 3, 8, 1, 9, 2, 7, 4, 6};

        int[] a = base.clone(); bubbleSort(a);
        System.out.println("Bubble Sort    : " + Arrays.toString(a));

        int[] b = base.clone(); insertionSort(b);
        System.out.println("Insertion Sort : " + Arrays.toString(b));

        int[] c = base.clone(); selectionSort(c);
        System.out.println("Selection Sort : " + Arrays.toString(c));

        int[] d = base.clone(); mergeSort(d, 0, d.length - 1);
        System.out.println("Merge Sort     : " + Arrays.toString(d));

        int[] e = base.clone(); quickSort(e, 0, e.length - 1);
        System.out.println("Quick Sort     : " + Arrays.toString(e));

        int[] f = {4, 2, 2, 8, 3, 3, 1};
        System.out.println("Counting Sort  : " + Arrays.toString(countingSort(f, 8)));

        System.out.println("\n=== Complexity Summary ===");
        System.out.println("Algorithm       Best      Avg       Worst     Space  Stable");
        System.out.println("Bubble Sort     O(n)      O(n²)     O(n²)     O(1)   Yes");
        System.out.println("Insertion Sort  O(n)      O(n²)     O(n²)     O(1)   Yes");
        System.out.println("Selection Sort  O(n²)     O(n²)     O(n²)     O(1)   No");
        System.out.println("Merge Sort      O(nlogn)  O(nlogn)  O(nlogn)  O(n)   Yes");
        System.out.println("Quick Sort      O(nlogn)  O(nlogn)  O(n²)     O(lgn) No");
        System.out.println("Counting Sort   O(n+k)    O(n+k)    O(n+k)    O(n+k) Yes");
    }
}

package com.nelush.arrays.algorithms;

import java.util.Arrays;

/**
 * DutchNationalFlag
 *
 * Article section: "Dutch National Flag Algorithm"
 *
 * Exact code from the article:
 *
 *   "Three pointers define three regions:
 *    arr[0..low-1]    = 0  (sorted zeros)
 *    arr[low..mid-1]  = 1  (sorted ones)
 *    arr[mid..high]   = unprocessed
 *    arr[high+1..n-1] = 2  (sorted twos)"
 *
 *   "The critical subtlety when arr[mid] == 2 and you swap with high:
 *    do not advance mid. The element that just arrived from high is unknown
 *    — it needs to be examined."
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.algorithms.DutchNationalFlag"
 *
 * Author: Nelush Gayashan Fernando
 */
public class DutchNationalFlag {

    // ─── CORE ALGORITHM — SORT 0s, 1s, 2s ───────────────────────────────────

    /**
     * Exact code from the article.
     */
    public static void sortColors(int[] arr) {
        int low = 0, mid = 0, high = arr.length - 1;

        while (mid <= high) {
            switch (arr[mid]) {
                case 0 -> { swap(arr, low, mid); low++; mid++; }
                case 1 -> mid++;
                // Do NOT advance mid after swapping with high —
                // arr[mid] now holds an unknown element from the right
                case 2 -> { swap(arr, mid, high); high--; }
            }
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    }

    // ─── THREE-WAY PARTITION (QUICKSORT PARTITION STEP) ───────────────────────

    /**
     * From the article:
     * "This is also the partition step at the heart of QuickSort's three-way variant,
     * which handles arrays with many duplicate elements more efficiently than the
     * two-way partition."
     *
     * Rearrange: all < pivot | all == pivot | all > pivot
     */
    public static int[] threeWayPartition(int[] arr, int pivot) {
        int low = 0, mid = 0, high = arr.length - 1;
        while (mid <= high) {
            if      (arr[mid] < pivot) { swap(arr, low++, mid++); }
            else if (arr[mid] > pivot) { swap(arr, mid, high--); }
            else                       { mid++; }
        }
        return new int[]{low, high}; // pivot region [low..high]
    }

    // ─── STEP-BY-STEP TRACE ───────────────────────────────────────────────────

    public static void trace(int[] arr) {
        System.out.println("Input: " + Arrays.toString(arr));
        int low = 0, mid = 0, high = arr.length - 1, step = 0;

        while (mid <= high) {
            System.out.printf("  step %-2d  low=%-2d mid=%-2d high=%-2d  arr=%s%n",
                ++step, low, mid, high, Arrays.toString(arr));
            switch (arr[mid]) {
                case 0 -> { swap(arr, low, mid); low++; mid++; }
                case 1 -> mid++;
                case 2 -> { swap(arr, mid, high); high--; }
            }
        }
        System.out.println("Output: " + Arrays.toString(arr));
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Dutch National Flag Algorithm ===\n");

        System.out.println("--- Step-by-step trace on [2,0,2,1,1,0] ---");
        trace(new int[]{2, 0, 2, 1, 1, 0});

        System.out.println();
        int[] arr = {2, 0, 1, 2, 1, 0, 0, 2};
        sortColors(arr);
        System.out.println("sortColors([2,0,1,2,1,0,0,2]) → " + Arrays.toString(arr));

        System.out.println();
        int[] arr2 = {1, 3, 5, 2, 4, 6, 0, 7};
        int[] bounds = threeWayPartition(arr2, 4);
        System.out.println("threeWayPartition(pivot=4) → " + Arrays.toString(arr2));
        System.out.println("pivot region indices        → [" + bounds[0] + ", " + bounds[1] + "]");
    }
}

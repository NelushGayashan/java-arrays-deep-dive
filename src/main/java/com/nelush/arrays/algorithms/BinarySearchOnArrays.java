package com.nelush.arrays.algorithms;

import java.util.Arrays;

/**
 * BinarySearchOnArrays
 *
 * Binary search works on any SORTED array by repeatedly halving the
 * search space.  Each step eliminates half the remaining candidates.
 *
 * Invariant: the target, if it exists, is always within [left, right].
 *
 * Time O(log n), Space O(1).
 *
 * Common pitfalls:
 *   - mid = (left + right) / 2  overflows when left+right > Integer.MAX_VALUE.
 *     Safe form: mid = left + (right - left) / 2
 *   - Off-by-one in the boundary update (left = mid vs left = mid + 1).
 *
 * Covered variants:
 *   1. Classic binary search (find exact target)
 *   2. Find first occurrence (leftmost index of target)
 *   3. Find last occurrence (rightmost index of target)
 *   4. Find insertion position (lower bound)
 *   5. Search in a rotated sorted array
 *   6. Find minimum in a rotated sorted array
 *   7. Binary search on the answer (abstract domain)
 *
 */
public class BinarySearchOnArrays {

    // ─── 1. CLASSIC BINARY SEARCH ─────────────────────────────────────────────

    /**
     * Returns the index of target in a sorted array, or -1 if not found.
     */
    public static int binarySearch(int[] arr, int target) {
        int left  = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // overflow-safe midpoint

            if      (arr[mid] == target) return mid;
            else if (arr[mid] <  target) left  = mid + 1; // target is in right half
            else                         right = mid - 1; // target is in left half
        }
        return -1;
    }

    // ─── 2. FIRST OCCURRENCE ──────────────────────────────────────────────────

    /**
     * Find the leftmost (smallest) index where arr[i] == target.
     * Even when arr[mid] == target, keep searching left: right = mid - 1.
     * Record mid as a candidate.
     */
    public static int firstOccurrence(int[] arr, int target) {
        int left   = 0;
        int right  = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                result = mid;      // record candidate
                right  = mid - 1; // keep looking left for an earlier occurrence
            } else if (arr[mid] < target) {
                left  = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    // ─── 3. LAST OCCURRENCE ───────────────────────────────────────────────────

    /**
     * Find the rightmost index where arr[i] == target.
     * Even when arr[mid] == target, keep searching right: left = mid + 1.
     */
    public static int lastOccurrence(int[] arr, int target) {
        int left   = 0;
        int right  = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                result = mid;     // record candidate
                left   = mid + 1; // keep looking right
            } else if (arr[mid] < target) {
                left  = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    /**
     * Count of target occurrences = lastOccurrence - firstOccurrence + 1.
     */
    public static int countOccurrences(int[] arr, int target) {
        int first = firstOccurrence(arr, target);
        if (first == -1) return 0;
        int last = lastOccurrence(arr, target);
        return last - first + 1;
    }

    // ─── 4. INSERTION POSITION (LOWER BOUND) ──────────────────────────────────

    /**
     * Find the leftmost index where target can be inserted to keep sorted order.
     * If target exists, returns its first occurrence index.
     * If not, returns the index where it would be inserted.
     *
     * arr=[1,3,5,6], target=2 -> 1  (insert before 3)
     * arr=[1,3,5,6], target=5 -> 2  (first occurrence of 5)
     * arr=[1,3,5,6], target=7 -> 4  (append at end)
     */
    public static int lowerBound(int[] arr, int target) {
        int left  = 0;
        int right = arr.length; // right = n (one past last index)

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) left  = mid + 1;
            else                   right = mid;     // arr[mid] >= target: keep mid as candidate
        }
        return left;
    }

    // ─── 5. SEARCH IN ROTATED SORTED ARRAY ───────────────────────────────────

    /**
     * A sorted array rotated at some pivot: [4,5,6,7,0,1,2].
     * One half is always fully sorted.
     *
     * At each step:
     *   If left half [left..mid] is sorted:
     *     If target is within [arr[left], arr[mid]], search left half.
     *     Else search right half.
     *   Else right half [mid..right] is sorted:
     *     If target is within [arr[mid], arr[right]], search right half.
     *     Else search left half.
     */
    public static int searchRotated(int[] arr, int target) {
        int left  = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) return mid;

            // Determine which half is sorted
            if (arr[left] <= arr[mid]) {
                // Left half is sorted
                if (target >= arr[left] && target < arr[mid]) {
                    right = mid - 1; // target in sorted left half
                } else {
                    left  = mid + 1;
                }
            } else {
                // Right half is sorted
                if (target > arr[mid] && target <= arr[right]) {
                    left  = mid + 1; // target in sorted right half
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    // ─── 6. FIND MINIMUM IN ROTATED SORTED ARRAY ──────────────────────────────

    /**
     * The minimum is at the pivot point — where the array "resets".
     * If arr[left] <= arr[mid], the left half is sorted and the minimum
     * is not in the left half (unless the entire array is sorted).
     * Otherwise the minimum is in the left half (including mid).
     */
    public static int findMinRotated(int[] arr) {
        int left  = 0;
        int right = arr.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] > arr[right]) {
                left  = mid + 1; // minimum is in right half
            } else {
                right = mid;     // arr[mid] could be the minimum
            }
        }
        return arr[left];
    }

    // ─── 7. BINARY SEARCH ON THE ANSWER ──────────────────────────────────────

    /**
     * "Given an array of book page counts and d days, what is the minimum
     *  number of pages one person must read per day to finish all books?"
     *
     * The answer lies in [max(pages), sum(pages)].
     * Binary search on this range, testing feasibility for each midpoint.
     * Feasibility check: can all books be read in d days if the daily limit is mid?
     *
     * This pattern appears in many problems where:
     *   - Answer is a value in a monotonic range.
     *   - For any value x, we can check in O(n) whether x is feasible.
     *   - We want the minimum feasible x.
     */
    public static int minPagesPerDay(int[] pages, int days) {
        int left  = Arrays.stream(pages).max().getAsInt(); // must read at least the largest book
        int right = Arrays.stream(pages).sum();            // worst case: read everything in one day

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (canFinish(pages, days, mid)) {
                right = mid;      // mid is feasible; try smaller
            } else {
                left  = mid + 1; // mid is too small; need more pages per day
            }
        }
        return left;
    }

    private static boolean canFinish(int[] pages, int days, int dailyLimit) {
        int daysNeeded = 1;
        int pagesRead  = 0;

        for (int p : pages) {
            if (p > dailyLimit) return false; // single book exceeds limit
            if (pagesRead + p > dailyLimit) {
                daysNeeded++;
                pagesRead = 0;
            }
            pagesRead += p;
        }
        return daysNeeded <= days;
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Binary Search Variants ===\n");

        int[] sorted = {1, 2, 2, 2, 3, 4, 5};
        System.out.println("arr: " + Arrays.toString(sorted));
        System.out.println("binarySearch(2)       = " + binarySearch(sorted, 2));
        System.out.println("firstOccurrence(2)    = " + firstOccurrence(sorted, 2));
        System.out.println("lastOccurrence(2)     = " + lastOccurrence(sorted, 2));
        System.out.println("countOccurrences(2)   = " + countOccurrences(sorted, 2));
        System.out.println("binarySearch(6)       = " + binarySearch(sorted, 6));

        System.out.println();
        int[] ins = {1, 3, 5, 6};
        System.out.println("lowerBound([1,3,5,6], 2) = " + lowerBound(ins, 2)); // 1
        System.out.println("lowerBound([1,3,5,6], 5) = " + lowerBound(ins, 5)); // 2
        System.out.println("lowerBound([1,3,5,6], 7) = " + lowerBound(ins, 7)); // 4

        System.out.println();
        int[] rotated = {4, 5, 6, 7, 0, 1, 2};
        System.out.println("searchRotated([4,5,6,7,0,1,2], 0) = " + searchRotated(rotated, 0));
        System.out.println("searchRotated([4,5,6,7,0,1,2], 3) = " + searchRotated(rotated, 3));
        System.out.println("findMinRotated([4,5,6,7,0,1,2])   = " + findMinRotated(rotated));

        System.out.println();
        int[] pages = {10, 20, 30, 40};
        System.out.println("minPagesPerDay([10,20,30,40], days=2) = " + minPagesPerDay(pages, 2));
    }
}

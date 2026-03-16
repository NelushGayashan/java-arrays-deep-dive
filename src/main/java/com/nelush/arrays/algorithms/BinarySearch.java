package com.nelush.arrays.algorithms;

import java.util.Arrays;

/**
 * BinarySearch
 *
 * Article section: "Binary Search on Arrays"
 *
 * Every method taken directly from the article:
 *   1. binarySearch           — classic, overflow-safe midpoint
 *   2. firstOccurrence        — leftmost index of target
 *   3. searchRotated          — one half is always sorted
 *   4. minPagesPerDay         — binary search on the answer space
 *
 * From the article:
 *   "The most important implementation detail is the midpoint calculation.
 *   (left + right) / 2 overflows when both values are large.
 *   The safe form is left + (right - left) / 2."
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.algorithms.BinarySearch"
 *
 * Author: Nelush Gayashan Fernando
 */
public class BinarySearch {

    // ─── 1. CLASSIC BINARY SEARCH ─────────────────────────────────────────────

    /**
     * Exact code from the article.
     * Core invariant: if target exists it is always within [left, right].
     */
    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // overflow-safe

            if      (arr[mid] == target) return mid;
            else if (arr[mid] <  target) left  = mid + 1; // target in right half
            else                         right = mid - 1; // target in left half
        }
        return -1;
    }

    // ─── 2. FIRST OCCURRENCE ──────────────────────────────────────────────────

    /**
     * Exact code from the article.
     *
     * "when you find arr[mid] == target, do not stop — record mid as a candidate
     * and keep searching left by setting right = mid - 1."
     */
    public static int firstOccurrence(int[] arr, int target) {
        int left = 0, right = arr.length - 1, result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                result = mid;      // record candidate
                right  = mid - 1; // keep searching left for an earlier one
            } else if (arr[mid] < target) left  = mid + 1;
            else                          right = mid - 1;
        }
        return result;
    }

    // ─── LAST OCCURRENCE (complement to firstOccurrence) ─────────────────────

    public static int lastOccurrence(int[] arr, int target) {
        int left = 0, right = arr.length - 1, result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                result = mid;
                left   = mid + 1; // keep searching right
            } else if (arr[mid] < target) left  = mid + 1;
            else                          right = mid - 1;
        }
        return result;
    }

    public static int countOccurrences(int[] arr, int target) {
        int first = firstOccurrence(arr, target);
        if (first == -1) return 0;
        return lastOccurrence(arr, target) - first + 1;
    }

    // ─── LOWER BOUND ─────────────────────────────────────────────────────────

    /**
     * Leftmost index where target can be inserted to keep sorted order.
     */
    public static int lowerBound(int[] arr, int target) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) left  = mid + 1;
            else                   right = mid;
        }
        return left;
    }

    // ─── 3. SEARCH IN ROTATED SORTED ARRAY ───────────────────────────────────

    /**
     * Exact code from the article.
     *
     * "at any midpoint, one of the two halves must be fully sorted — and you can
     * tell which one by comparing arr[left] with arr[mid]. Once you know which half
     * is sorted, you can determine in O(1) whether the target falls within it."
     */
    public static int searchRotated(int[] arr, int target) {
        int left = 0, right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;

            if (arr[left] <= arr[mid]) { // left half is sorted
                if (target >= arr[left] && target < arr[mid]) right = mid - 1;
                else                                          left  = mid + 1;
            } else {                     // right half is sorted
                if (target > arr[mid] && target <= arr[right]) left  = mid + 1;
                else                                            right = mid - 1;
            }
        }
        return -1;
    }

    // ─── FIND MINIMUM IN ROTATED SORTED ARRAY ────────────────────────────────

    public static int findMinRotated(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] > arr[right]) left  = mid + 1;
            else                       right = mid;
        }
        return arr[left];
    }

    // ─── 4. BINARY SEARCH ON THE ANSWER ──────────────────────────────────────

    /**
     * Exact code from the article.
     *
     * "the answer is not an index but a value in a monotonic range, and you can
     * efficiently test whether any candidate value is feasible."
     *
     * Minimum pages per day to finish all books in `days` days.
     */
    public static int minPagesPerDay(int[] pages, int days) {
        int left  = Arrays.stream(pages).max().getAsInt(); // minimum possible limit
        int right = Arrays.stream(pages).sum();            // maximum possible limit

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (canFinish(pages, days, mid)) right = mid;      // feasible — try smaller
            else                             left  = mid + 1;  // too small — need more
        }
        return left;
    }

    private static boolean canFinish(int[] pages, int days, int dailyLimit) {
        int daysNeeded = 1, pagesRead = 0;
        for (int p : pages) {
            if (p > dailyLimit) return false;
            if (pagesRead + p > dailyLimit) { daysNeeded++; pagesRead = 0; }
            pagesRead += p;
        }
        return daysNeeded <= days;
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Binary Search on Arrays ===\n");

        int[] sorted = {1, 2, 2, 2, 3, 4, 5};
        System.out.println("arr: " + Arrays.toString(sorted));
        System.out.println("binarySearch(2)         = " + binarySearch(sorted, 2));
        System.out.println("firstOccurrence(2)      = " + firstOccurrence(sorted, 2));
        System.out.println("lastOccurrence(2)       = " + lastOccurrence(sorted, 2));
        System.out.println("countOccurrences(2)     = " + countOccurrences(sorted, 2));
        System.out.println("binarySearch(9)         = " + binarySearch(sorted, 9));

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
        int[] books = {10, 20, 30, 40};
        System.out.println("minPagesPerDay([10,20,30,40], days=2) = " + minPagesPerDay(books, 2));
    }
}

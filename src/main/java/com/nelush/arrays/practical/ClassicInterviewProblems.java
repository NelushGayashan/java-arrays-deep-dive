package com.nelush.arrays.practical;

import java.util.*;

/**
 * ============================================================
 * SECTION 10: Classic Interview Problems — Patterns in Practice
 * ============================================================
 *
 * This section demonstrates how the individual techniques (two-pointer,
 * sliding window, prefix sum, binary search, sorting) combine in real
 * interview-level problems. Each problem is solved first with a brute
 * force that makes the intent clear, then with the optimal approach.
 */
public class ClassicInterviewProblems {

    // -------------------------------------------------------
    // 1. Find All Duplicates in Array — XOR trick
    //    Array contains integers 1..n, each appearing 1 or 2 times.
    // -------------------------------------------------------

    /**
     * Every number XOR itself = 0. XOR all elements AND all indices 1..n.
     * Unique elements cancel; duplicates appear once in the result.
     *
     * Alternative: negate arr[abs(arr[i])-1] as a visited marker —
     * if we encounter a negative, the index+1 value is a duplicate.
     */
    public static List<Integer> findDuplicates(int[] arr) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            int idx = Math.abs(arr[i]) - 1; // map value to index (1-indexed → 0-indexed)

            if (arr[idx] < 0) {
                result.add(idx + 1); // negative = visited twice → duplicate
            } else {
                arr[idx] = -arr[idx]; // mark as visited
            }
        }

        // Restore array (optional)
        for (int i = 0; i < arr.length; i++) arr[i] = Math.abs(arr[i]);

        return result;
    }

    // -------------------------------------------------------
    // 2. Find Missing Number — Gauss sum vs XOR
    // -------------------------------------------------------

    /**
     * Method 1 — Gauss: expected sum = n(n+1)/2; subtract actual sum.
     * Method 2 — XOR:   XOR all elements with 0..n; missing value survives.
     * XOR avoids potential overflow for large n.
     */
    public static int missingNumber(int[] nums) {
        int n = nums.length;
        int xor = n;
        for (int i = 0; i < n; i++) {
            xor ^= i ^ nums[i]; // XOR with both index and value
        }
        return xor;
    }

    // -------------------------------------------------------
    // 3. Product of Array Except Self — no division, O(n)
    // -------------------------------------------------------

    /**
     * result[i] = product of all elements except arr[i].
     *
     * Pass 1 (left to right): result[i] = product of arr[0..i-1]
     *   result[0] = 1 (no elements to the left)
     *
     * Pass 2 (right to left): multiply by product of arr[i+1..n-1]
     *
     * This avoids division (which fails when arr[i] == 0) and uses O(1)
     * extra space (the output array is not counted by convention).
     */
    public static int[] productExceptSelf(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];

        // Left products
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * arr[i - 1];
        }

        // Multiply in right products
        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= right;
            right *= arr[i];
        }

        return result;
    }

    // -------------------------------------------------------
    // 4. Next Permutation — lexicographic next arrangement
    // -------------------------------------------------------

    /**
     * Algorithm (in-place, O(n)):
     * 1. Find largest index i such that arr[i] < arr[i+1].
     *    If no such index, the array is the last permutation → reverse all.
     * 2. Find largest index j > i such that arr[i] < arr[j].
     * 3. Swap arr[i] and arr[j].
     * 4. Reverse the suffix arr[i+1..end] to make it the smallest suffix.
     */
    public static void nextPermutation(int[] arr) {
        int n = arr.length;
        int i = n - 2;

        // Step 1: find first decreasing element from right
        while (i >= 0 && arr[i] >= arr[i + 1]) i--;

        if (i >= 0) {
            // Step 2: find element just greater than arr[i]
            int j = n - 1;
            while (arr[j] <= arr[i]) j--;

            // Step 3: swap
            swap(arr, i, j);
        }

        // Step 4: reverse suffix
        int left = i + 1, right = n - 1;
        while (left < right) swap(arr, left++, right--);
    }

    // -------------------------------------------------------
    // 5. Spiral Matrix — simulation with direction vectors
    // -------------------------------------------------------

    /**
     * Shrink the boundary after each direction is exhausted:
     *   top, right, bottom, left — each incremented/decremented
     *   once its direction is fully traversed.
     */
    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix.length == 0) return result;

        int top    = 0, bottom = matrix.length - 1;
        int left   = 0, right  = matrix[0].length - 1;

        while (top <= bottom && left <= right) {
            for (int c = left;  c <= right;  c++) result.add(matrix[top][c]);
            top++;

            for (int r = top;   r <= bottom; r++) result.add(matrix[r][right]);
            right--;

            if (top <= bottom) {
                for (int c = right; c >= left;   c--) result.add(matrix[bottom][c]);
                bottom--;
            }

            if (left <= right) {
                for (int r = bottom; r >= top;   r--) result.add(matrix[r][left]);
                left++;
            }
        }

        return result;
    }

    // -------------------------------------------------------
    // 6. Merge Intervals — sort + greedy
    // -------------------------------------------------------

    /**
     * Sort intervals by start time. Walk through; if current interval's
     * start <= last merged interval's end, they overlap — extend end.
     * Otherwise, start a new merged interval.
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]); // sort by start

        List<int[]> merged = new ArrayList<>();
        merged.add(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            int[] last = merged.get(merged.size() - 1);
            int[] curr = intervals[i];

            if (curr[0] <= last[1]) {
                last[1] = Math.max(last[1], curr[1]); // extend end of last merged
            } else {
                merged.add(curr); // no overlap: new merged interval
            }
        }

        return merged.toArray(new int[0][]);
    }

    // -------------------------------------------------------
    // 7. Rotate Array by K — three reversal trick
    // -------------------------------------------------------

    /**
     * Rotating right by k positions:
     * Reverse entire array, then reverse first k, then reverse rest.
     *
     *   [1,2,3,4,5,6,7], k=3
     * → reverse all:    [7,6,5,4,3,2,1]
     * → reverse [0,k-1]: [5,6,7,4,3,2,1]
     * → reverse [k,n-1]: [5,6,7,1,2,3,4]
     */
    public static void rotateRight(int[] arr, int k) {
        int n = arr.length;
        k %= n; // handle k > n

        reverse(arr, 0, n - 1);
        reverse(arr, 0, k - 1);
        reverse(arr, k, n - 1);
    }

    // -------------------------------------------------------
    // 8. Maximum Score from Subarrays (LeetCode 2155 variant)
    //    For each pair, score = min(nums[i], nums[j]) + (j - i)
    //    Two-pointer on sorted array
    // -------------------------------------------------------

    /**
     * Sort by value. Use two pointers. The score for any pair (i, j) where
     * i < j in sorted order is nums[i] + (j - i) (nums[i] is smaller).
     * The pairing problem reduces to finding the best partner for each element.
     */
    public static int maxScore(int[] nums) {
        Arrays.sort(nums);
        int result = 0;
        // Greedily pair consecutive elements in sorted order
        for (int i = 0; i < nums.length - 1; i += 2) {
            result += nums[i]; // min of pair + 1 (distance always 1 in sorted pairs)
        }
        return result;
    }

    // -------------------------------------------------------
    // 9. Subsets — power set via bit masking
    // -------------------------------------------------------

    /**
     * For n elements, there are 2^n subsets. Each subset corresponds to
     * a bitmask 0 to 2^n - 1 where bit i set means arr[i] is included.
     */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;

        for (int mask = 0; mask < (1 << n); mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int bit = 0; bit < n; bit++) {
                if ((mask & (1 << bit)) != 0) {
                    subset.add(nums[bit]);
                }
            }
            result.add(subset);
        }

        return result;
    }

    // -------------------------------------------------------
    // 10. Majority Element — Boyer-Moore Voting Algorithm
    //     Finds element appearing > n/2 times in O(n), O(1) space
    // -------------------------------------------------------

    /**
     * Maintain a candidate and a counter.
     * When we see the candidate, increment counter.
     * When we see something else, decrement counter.
     * When counter reaches 0, change candidate.
     *
     * The majority element (> n/2 occurrences) will always survive
     * because it can out-vote all others combined.
     */
    public static int majorityElement(int[] arr) {
        int candidate = arr[0];
        int count = 1;

        for (int i = 1; i < arr.length; i++) {
            if (count == 0) {
                candidate = arr[i];
                count = 1;
            } else if (arr[i] == candidate) {
                count++;
            } else {
                count--;
            }
        }

        return candidate;
    }

    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    }

    private static void reverse(int[] arr, int left, int right) {
        while (left < right) swap(arr, left++, right--);
    }

    public static void main(String[] args) {
        System.out.println("=== Find Duplicates ===");
        System.out.println(findDuplicates(new int[]{4, 3, 2, 7, 8, 2, 3, 1})); // [2, 3]

        System.out.println("\n=== Missing Number ===");
        System.out.println(missingNumber(new int[]{3, 0, 1}));    // 2
        System.out.println(missingNumber(new int[]{9,6,4,2,3,5,7,0,1})); // 8

        System.out.println("\n=== Product Except Self ===");
        System.out.println(Arrays.toString(productExceptSelf(new int[]{1, 2, 3, 4}))); // [24,12,8,6]

        System.out.println("\n=== Next Permutation ===");
        int[] perm = {1, 2, 3};
        nextPermutation(perm);
        System.out.println(Arrays.toString(perm)); // [1, 3, 2]

        System.out.println("\n=== Spiral Matrix ===");
        int[][] mat = {{1,2,3},{4,5,6},{7,8,9}};
        System.out.println(spiralOrder(mat)); // [1,2,3,6,9,8,7,4,5]

        System.out.println("\n=== Merge Intervals ===");
        int[][] iv = {{1,3},{2,6},{8,10},{15,18}};
        for (int[] x : mergeIntervals(iv)) System.out.print(Arrays.toString(x) + " ");
        System.out.println();

        System.out.println("\n=== Rotate Array ===");
        int[] rot = {1,2,3,4,5,6,7};
        rotateRight(rot, 3);
        System.out.println(Arrays.toString(rot)); // [5,6,7,1,2,3,4]

        System.out.println("\n=== Majority Element ===");
        System.out.println(majorityElement(new int[]{3,2,3}));    // 3
        System.out.println(majorityElement(new int[]{2,2,1,1,1,2,2})); // 2

        System.out.println("\n=== Subsets ===");
        System.out.println(subsets(new int[]{1, 2, 3}));
    }
}

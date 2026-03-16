package com.nelush.arrays.algorithms;

import java.util.Arrays;

/**
 * KadaneAlgorithm
 *
 * Article section: "Kadane's Algorithm"
 *
 * Every method taken directly from the article:
 *   1. maxSubarraySum         — classic Kadane O(n) O(1)
 *   2. maxSubarrayWithIndices — returns actual subarray bounds
 *   3. maxSubarrayProduct     — handles negative-flip with min tracking
 *
 * From the article:
 *   "Kadane's algorithm solves the maximum subarray sum problem in O(n) time
 *   and O(1) space. It is one of the most elegant demonstrations of dynamic
 *   programming thinking on a flat array."
 *
 *   "If the running sum is negative, adding it to arr[i] would produce a result
 *   smaller than arr[i] alone. So reset."
 *
 *   The recurrence:
 *     best[i] = max( arr[i],  best[i-1] + arr[i] )
 *                   ^ start fresh    ^ extend current window
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.algorithms.KadaneAlgorithm"
 *
 * Author: Nelush Gayashan Fernando
 */
public class KadaneAlgorithm {

    // ─── 1. CLASSIC KADANE ────────────────────────────────────────────────────

    /**
     * Exact code from the article.
     *
     * Trace on [-2, 1, -3, 4, -1, 2, 1, -5, 4]:
     *   i=1: max(1, -2+1)=1   → globalMax=1
     *   i=3: max(4, -2+4)=4   → globalMax=4  (fresh start — prev sum was negative)
     *   i=6: max(1, 5+1)=6    → globalMax=6
     *   Answer: 6  (subarray [4,-1,2,1])
     */
    public static int maxSubarraySum(int[] arr) {
        int currentSum = arr[0]; // best sum ending AT this position
        int globalMax  = arr[0]; // best sum seen anywhere

        for (int i = 1; i < arr.length; i++) {
            // Either extend the existing subarray or start fresh at i
            currentSum = Math.max(arr[i], currentSum + arr[i]);
            globalMax  = Math.max(globalMax, currentSum);
        }
        return globalMax;
    }

    // ─── 2. KADANE WITH SUBARRAY INDICES ──────────────────────────────────────

    /**
     * Exact code from the article.
     * Returns int[]{maxSum, startIndex, endIndex}.
     *
     * "record a candidate start index each time you start fresh, and lock it in
     * whenever currentSum produces a new global maximum"
     *
     * [-2,1,-3,4,-1,2,1,-5,4] → max=6, subarray indices [3,6]
     */
    public static int[] maxSubarrayWithIndices(int[] arr) {
        int currentSum = arr[0], globalMax = arr[0];
        int start = 0, end = 0, tempStart = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > currentSum + arr[i]) {
                currentSum = arr[i];
                tempStart  = i; // fresh window begins here
            } else {
                currentSum += arr[i];
            }

            if (currentSum > globalMax) {
                globalMax = currentSum;
                start     = tempStart;
                end       = i;
            }
        }
        return new int[]{globalMax, start, end};
    }
    // [-2,1,-3,4,-1,2,1,-5,4] → max=6, subarray indices [3,6]

    // ─── 3. MAXIMUM CIRCULAR SUBARRAY SUM ─────────────────────────────────────

    /**
     * From the article:
     * "There are two cases: either the maximum subarray is entirely within the array
     * (standard Kadane), or it wraps around the ends. The wrap-around case is equivalent
     * to: the total sum minus the minimum straight subarray sum."
     */
    public static int maxCircularSubarraySum(int[] arr) {
        int totalSum = 0;
        for (int v : arr) totalSum += v;

        // Case 1: standard Kadane (no wrap)
        int maxStraight = maxSubarraySum(arr);

        // Case 2: wrap-around = total - minimum subarray
        int minStraight = minSubarraySum(arr);

        // Edge: if all elements are negative, wrap-around gives 0 (invalid)
        return (maxStraight < 0) ? maxStraight
                                 : Math.max(maxStraight, totalSum - minStraight);
    }

    private static int minSubarraySum(int[] arr) {
        int curMin = arr[0], globalMin = arr[0];
        for (int i = 1; i < arr.length; i++) {
            curMin    = Math.min(arr[i], curMin + arr[i]);
            globalMin = Math.min(globalMin, curMin);
        }
        return globalMin;
    }

    // ─── 4. MAXIMUM SUBARRAY PRODUCT ──────────────────────────────────────────

    /**
     * Exact code from the article.
     *
     * "Track both the maximum AND minimum product ending at each position — the
     * minimum (most negative) may become the maximum when multiplied by a negative."
     *
     * [-2,3,-4] → 24 (entire array: -2 * 3 * -4)
     */
    public static int maxSubarrayProduct(int[] arr) {
        int maxProd = arr[0], minProd = arr[0], result = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < 0) { int t = maxProd; maxProd = minProd; minProd = t; } // flip
            maxProd = Math.max(arr[i], maxProd * arr[i]);
            minProd = Math.min(arr[i], minProd * arr[i]);
            result  = Math.max(result, maxProd);
        }
        return result;
    }
    // [-2,3,-4] → 24 (entire array: -2 * 3 * -4)

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Kadane's Algorithm ===\n");

        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("arr: " + Arrays.toString(arr));

        System.out.println("maxSubarraySum           = " + maxSubarraySum(arr));

        int[] res = maxSubarrayWithIndices(arr);
        System.out.println("maxSubarrayWithIndices   = " + res[0]
            + "  indices [" + res[1] + ".." + res[2] + "]"
            + "  subarray: " + Arrays.toString(Arrays.copyOfRange(arr, res[1], res[2] + 1)));

        System.out.println();
        System.out.println("maxCircular [5,-3,5]     = " + maxCircularSubarraySum(new int[]{5, -3, 5}));
        System.out.println("maxCircular [-3,-2,-3]   = " + maxCircularSubarraySum(new int[]{-3, -2, -3}));

        System.out.println();
        System.out.println("maxProduct [2,3,-2,4]    = " + maxSubarrayProduct(new int[]{2, 3, -2, 4}));
        System.out.println("maxProduct [-2,3,-4]     = " + maxSubarrayProduct(new int[]{-2, 3, -4}));
    }
}

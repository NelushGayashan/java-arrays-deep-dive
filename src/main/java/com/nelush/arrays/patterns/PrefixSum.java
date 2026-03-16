package com.nelush.arrays.patterns;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * PrefixSum
 *
 * Article section: "Prefix Sum"
 *
 * Every method taken directly from the article:
 *   1. buildPrefixSum + rangeSum   — O(n) build, O(1) query
 *   2. 2D prefix sum               — rectangle sum queries
 *   3. subarraySumEqualsK          — prefix sum + HashMap
 *   4. productExceptSelf           — two-pass prefix/suffix product
 *
 * Article formula:
 *   arr    = [3,  1,  4,  1,  5,  9,  2,  6]
 *   prefix = [3,  4,  8,  9, 14, 23, 25, 31]
 *
 *   rangeSum(L, R) = prefix[R] - prefix[L-1]
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.patterns.PrefixSum"
 *
 * Author: Nelush Gayashan Fernando
 */
public class PrefixSum {

    // ─── 1. BUILD AND QUERY ───────────────────────────────────────────────────

    /**
     * From the article:
     * "prefix[R] accumulates everything from 0 to R.
     * prefix[L-1] accumulates everything from 0 to L-1.
     * Their difference leaves exactly the range L to R."
     */
    public static int[] buildPrefixSum(int[] arr) {
        int[] prefix = new int[arr.length];
        prefix[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            prefix[i] = prefix[i - 1] + arr[i];
        }
        return prefix;
    }

    public static int rangeSum(int[] prefix, int L, int R) {
        return L == 0 ? prefix[R] : prefix[R] - prefix[L - 1];
    }
    // rangeSum(prefix, 2, 5) = 23 - 4 = 19 (elements 4+1+5+9)

    public static void prefixSumDemo() {
        int[] arr    = {3, 1, 4, 1, 5, 9, 2, 6};
        int[] prefix = buildPrefixSum(arr);

        System.out.println("=== Prefix Sum ===");
        System.out.println("arr    : " + Arrays.toString(arr));
        System.out.println("prefix : " + Arrays.toString(prefix));
        System.out.println("rangeSum(0, 3) = " + rangeSum(prefix, 0, 3)); // 9
        System.out.println("rangeSum(2, 5) = " + rangeSum(prefix, 2, 5)); // 19
        System.out.println("rangeSum(0, 7) = " + rangeSum(prefix, 0, 7)); // 31
    }

    // ─── 2. 2D PREFIX SUM ─────────────────────────────────────────────────────

    /**
     * From the article:
     *   prefix[i][j] = matrix[i][j]
     *                + prefix[i-1][j]    // above
     *                + prefix[i][j-1]    // left
     *                - prefix[i-1][j-1]; // was added twice above, subtract once
     *
     * Rectangle sum query (r1,c1)→(r2,c2):
     *   prefix[r2][c2] - prefix[r1-1][c2] - prefix[r2][c1-1] + prefix[r1-1][c1-1]
     */
    public static int[][] buildPrefixSum2D(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        int[][] prefix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                prefix[i][j] = matrix[i][j];
                if (i > 0) prefix[i][j] += prefix[i - 1][j];
                if (j > 0) prefix[i][j] += prefix[i][j - 1];
                if (i > 0 && j > 0) prefix[i][j] -= prefix[i - 1][j - 1];
            }
        }
        return prefix;
    }

    public static int rectangleSum(int[][] prefix, int r1, int c1, int r2, int c2) {
        int sum = prefix[r2][c2];
        if (r1 > 0) sum -= prefix[r1 - 1][c2];
        if (c1 > 0) sum -= prefix[r2][c1 - 1];
        if (r1 > 0 && c1 > 0) sum += prefix[r1 - 1][c1 - 1];
        return sum;
    }

    public static void prefixSum2DDemo() {
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] prefix = buildPrefixSum2D(matrix);
        System.out.println("\n=== 2D Prefix Sum ===");
        System.out.println("Full matrix sum           = " + rectangleSum(prefix, 0, 0, 2, 2)); // 45
        System.out.println("Sub-rect (0,0)→(1,1)      = " + rectangleSum(prefix, 0, 0, 1, 1)); // 12
        System.out.println("Sub-rect (1,1)→(2,2)      = " + rectangleSum(prefix, 1, 1, 2, 2)); // 28
    }

    // ─── 3. SUBARRAY SUM EQUALS K ─────────────────────────────────────────────

    /**
     * From the article:
     * "if the running sum at index j minus the running sum at some earlier index i
     * equals k, then the subarray from i+1 to j sums to k."
     *
     * "Instead of checking all pairs of indices, we store the count of each prefix
     * sum seen so far and look up the complement in O(1)."
     *
     * nums=[1,1,1], k=2 → 2
     */
    public static int subarraySumEqualsK(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1); // empty prefix — handles subarrays starting at index 0

        int runningSum = 0, count = 0;
        for (int num : nums) {
            runningSum += num;
            // How many earlier prefix sums equal (runningSum - k)?
            count += prefixCount.getOrDefault(runningSum - k, 0);
            prefixCount.merge(runningSum, 1, Integer::sum);
        }
        return count;
    }
    // nums=[1,1,1], k=2 → 2

    // ─── 4. PRODUCT EXCEPT SELF ───────────────────────────────────────────────

    /**
     * From the article:
     * "Two passes: the first builds the prefix product (everything to the left of
     * each index), the second multiplies in the suffix product (everything to the right)."
     *
     * [1,2,3,4] → [24,12,8,6]
     */
    public static int[] productExceptSelf(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];

        result[0] = 1;
        for (int i = 1; i < n; i++) result[i] = result[i - 1] * arr[i - 1]; // left pass

        int rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= rightProduct; // multiply in right side
            rightProduct *= arr[i];
        }
        return result;
    }
    // [1,2,3,4] → [24,12,8,6]

    // ─── ADDITIONAL: EQUILIBRIUM INDEX ───────────────────────────────────────

    /**
     * Find index i where leftSum == rightSum.
     * Total sum precomputed; left sum maintained as we scan.
     * Time O(n), Space O(1).
     */
    public static int equilibriumIndex(int[] arr) {
        int total = 0;
        for (int v : arr) total += v;

        int leftSum = 0;
        for (int i = 0; i < arr.length; i++) {
            int rightSum = total - leftSum - arr[i];
            if (leftSum == rightSum) return i;
            leftSum += arr[i];
        }
        return -1;
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        prefixSumDemo();
        prefixSum2DDemo();

        System.out.println("\n=== Subarray Sum Equals K ===");
        System.out.println("nums=[1,1,1], k=2 → " + subarraySumEqualsK(new int[]{1, 1, 1}, 2));
        System.out.println("nums=[1,2,3], k=3 → " + subarraySumEqualsK(new int[]{1, 2, 3}, 3));

        System.out.println("\n=== Product Except Self ===");
        System.out.println("[1,2,3,4] → " + Arrays.toString(productExceptSelf(new int[]{1, 2, 3, 4})));

        System.out.println("\n=== Equilibrium Index ===");
        System.out.println("[-7,1,5,2,-4,3,0] → index " + equilibriumIndex(new int[]{-7, 1, 5, 2, -4, 3, 0}));
    }
}

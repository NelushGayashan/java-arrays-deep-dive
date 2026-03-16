package com.nelush.arrays.patterns;

/**
 * ============================================================
 * SECTION 8: Kadane's Algorithm and Dynamic Programming on Arrays
 * ============================================================
 *
 * Kadane's algorithm solves the Maximum Subarray Sum problem in O(n)
 * time using a simple but profound observation about substructure.
 * It is one of the classic examples of dynamic programming (DP) applied
 * to a one-dimensional array.
 *
 * THE PROBLEM
 * Given an integer array that may contain negatives, find the
 * contiguous subarray with the largest possible sum.
 *
 *   arr = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
 *   Answer: subarray [4, -1, 2, 1], sum = 6
 *
 * NAIVE APPROACHES
 * Brute force O(n³): try all (i, j) pairs and sum arr[i..j].
 * Prefix sum O(n²): precompute prefix, then check all O(n²) pairs.
 *
 * WHY KADANE'S IS O(n)
 * DP asks: "what is the best solution to a problem if we fix
 * some boundary?" For subarrays, fix the RIGHT endpoint.
 *
 * Define: maxEndingHere(i) = maximum sum of any subarray ending at i.
 *
 * Two choices for a subarray ending at i:
 *   1. Start fresh at i:  subarray = [arr[i]], sum = arr[i]
 *   2. Extend the best subarray ending at i-1: sum = maxEndingHere(i-1) + arr[i]
 *
 * Take whichever is larger:
 *   maxEndingHere(i) = max(arr[i], maxEndingHere(i-1) + arr[i])
 *
 * This is the DP recurrence. We only need the PREVIOUS value, so
 * we use a single variable instead of an array — O(1) space.
 *
 * KEY INSIGHT
 * If maxEndingHere(i-1) is negative, extending it into arr[i] makes
 * the sum SMALLER than arr[i] alone. So we reset: start a new subarray
 * at arr[i] rather than carry forward a negative burden.
 * Equivalently: max(arr[i], maxEndingHere + arr[i]) resets when
 * maxEndingHere < 0.
 */
public class KadaneAlgorithm {

    // -------------------------------------------------------
    // 1. Classic Kadane — return maximum sum
    // -------------------------------------------------------

    public static int maxSubarraySum(int[] arr) {
        // maxSoFar    = global answer (best sum seen across all right endpoints)
        // maxEndHere  = best sum of any subarray ending exactly at current index

        int maxSoFar   = arr[0];
        int maxEndHere = arr[0];

        for (int i = 1; i < arr.length; i++) {
            // Extend or start fresh at i
            maxEndHere = Math.max(arr[i], maxEndHere + arr[i]);

            // Update global best
            maxSoFar = Math.max(maxSoFar, maxEndHere);
        }

        return maxSoFar;
    }

    // -------------------------------------------------------
    // 2. Kadane with Subarray Indices
    //    Returns [sum, startIndex, endIndex]
    // -------------------------------------------------------

    /**
     * Track where the current best window starts and ends.
     * tempStart advances whenever we start fresh (maxEndHere resets to arr[i]).
     * When a new global best is found, we snapshot start and end.
     */
    public static int[] maxSubarrayWithIndices(int[] arr) {
        int maxSoFar   = arr[0];
        int maxEndHere = arr[0];
        int start      = 0;
        int end        = 0;
        int tempStart  = 0;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maxEndHere + arr[i]) {
                // Starting fresh is better — new window begins at i
                maxEndHere = arr[i];
                tempStart  = i;
            } else {
                maxEndHere += arr[i];
            }

            if (maxEndHere > maxSoFar) {
                maxSoFar = maxEndHere;
                start    = tempStart;
                end      = i;
            }
        }

        return new int[]{maxSoFar, start, end};
    }

    // -------------------------------------------------------
    // 3. Maximum Product Subarray
    //    Harder variant: must track both max AND min at each step
    //    because a negative × negative = large positive
    // -------------------------------------------------------

    /**
     * At each index keep:
     *   maxProd = maximum product of any subarray ending here
     *   minProd = minimum product of any subarray ending here
     *             (can become the max if multiplied by a negative)
     *
     * When arr[i] < 0, max and min swap roles (multiplying by negative
     * flips the sign, turning the max into the min and vice versa).
     */
    public static int maxProductSubarray(int[] arr) {
        int maxProd  = arr[0];
        int minProd  = arr[0];
        int result   = arr[0];

        for (int i = 1; i < arr.length; i++) {
            int curr = arr[i];

            // When curr is negative, max and min swap — handle by computing both
            int newMax = Math.max(curr, Math.max(maxProd * curr, minProd * curr));
            int newMin = Math.min(curr, Math.min(maxProd * curr, minProd * curr));

            maxProd = newMax;
            minProd = newMin;
            result  = Math.max(result, maxProd);
        }

        return result;
    }

    // -------------------------------------------------------
    // 4. Maximum Sum Circular Subarray
    //    The subarray can wrap around (use the circular nature)
    // -------------------------------------------------------

    /**
     * Two cases:
     *   Case 1: Maximum subarray does NOT wrap — standard Kadane.
     *   Case 2: Maximum subarray WRAPS.
     *           A wrapping subarray is the total sum MINUS the minimum
     *           subarray (the elements NOT included, which must be contiguous
     *           in the center).
     *           max(circular) = totalSum - minSubarraySum(arr)
     *
     * Answer: max(case1, case2).
     * Edge case: if ALL elements are negative, case 2 = 0 (wrong —
     * empty subarray is not allowed). In that case, case 1 correctly
     * returns the least negative element.
     */
    public static int maxSumCircular(int[] arr) {
        int totalSum   = 0;
        int maxSum     = arr[0];
        int maxEndHere = arr[0];
        int minSum     = arr[0];
        int minEndHere = arr[0];

        for (int i = 1; i < arr.length; i++) {
            // Standard Kadane for max
            maxEndHere = Math.max(arr[i], maxEndHere + arr[i]);
            maxSum     = Math.max(maxSum, maxEndHere);

            // Kadane for min (to find middle part to remove)
            minEndHere = Math.min(arr[i], minEndHere + arr[i]);
            minSum     = Math.min(minSum, minEndHere);

            totalSum += arr[i];
        }
        totalSum += arr[0]; // arr[0] was not counted in the loop's first step

        // If all elements are negative, case 2 gives totalSum - totalSum = 0 (invalid)
        // maxSum will be negative (the best single element), which is the correct answer
        int circular = totalSum - minSum;
        return (circular == 0) ? maxSum : Math.max(maxSum, circular);
    }

    // -------------------------------------------------------
    // 5. Best Time to Buy and Sell Stock — Kadane variant
    //    Maximum profit from one transaction (one buy + one sell)
    //    Constraint: must buy before sell
    // -------------------------------------------------------

    /**
     * Equivalent to: maximum subarray sum of the DIFFERENCE array
     * diff[i] = prices[i] - prices[i-1]  (daily price changes).
     *
     * Applying Kadane on diff gives the maximum gain over any period,
     * which equals the best single buy-sell transaction.
     *
     * We implement it directly on prices for clarity:
     * Track the minimum price seen so far (best buy point).
     * At each day, profit = prices[i] - minPrice.
     * Keep the maximum such profit.
     */
    public static int maxProfit(int[] prices) {
        int minPrice  = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int price : prices) {
            if (price < minPrice) {
                minPrice = price;        // found a cheaper buy point
            } else {
                maxProfit = Math.max(maxProfit, price - minPrice); // sell at today's price
            }
        }

        return maxProfit;
    }

    // -------------------------------------------------------
    // 6. Jump Game — DP / greedy on arrays
    //    Can you reach the last index?
    // -------------------------------------------------------

    /**
     * At each index i, track the farthest index reachable.
     * If i ever exceeds maxReach, we are stuck (cannot reach i).
     * Otherwise, update maxReach = max(maxReach, i + nums[i]).
     */
    public static boolean canJump(int[] nums) {
        int maxReach = 0;

        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false; // cannot reach this index

            maxReach = Math.max(maxReach, i + nums[i]);

            if (maxReach >= nums.length - 1) return true; // already reachable
        }

        return true;
    }

    // -------------------------------------------------------
    // 7. Jump Game II — minimum jumps to reach the last index
    // -------------------------------------------------------

    /**
     * Greedy BFS on levels: treat each "level" as all positions
     * reachable in exactly k jumps. The next level is the union of
     * all positions reachable from the current level.
     * Increment jumps when we exhaust the current level.
     */
    public static int minJumps(int[] nums) {
        int jumps      = 0;
        int currentEnd = 0; // farthest index reachable with current number of jumps
        int farthest   = 0; // farthest index reachable with one more jump

        for (int i = 0; i < nums.length - 1; i++) { // stop before last index
            farthest = Math.max(farthest, i + nums[i]);

            if (i == currentEnd) { // exhausted current level
                jumps++;
                currentEnd = farthest;
            }
        }

        return jumps;
    }

    public static void main(String[] args) {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        System.out.println("=== Classic Kadane ===");
        System.out.println("Max sum: " + maxSubarraySum(arr)); // 6

        System.out.println("\n=== Kadane with Indices ===");
        int[] res = maxSubarrayWithIndices(arr);
        System.out.printf("Sum=%d  from index %d to %d%n", res[0], res[1], res[2]);

        System.out.println("\n=== Max Product Subarray ===");
        System.out.println(maxProductSubarray(new int[]{2, 3, -2, 4}));       // 6
        System.out.println(maxProductSubarray(new int[]{-2, 0, -1}));         // 0
        System.out.println(maxProductSubarray(new int[]{-2, 3, -4}));         // 24

        System.out.println("\n=== Max Sum Circular ===");
        System.out.println(maxSumCircular(new int[]{1, -2, 3, -2}));          // 3
        System.out.println(maxSumCircular(new int[]{5, -3, 5}));              // 10
        System.out.println(maxSumCircular(new int[]{-3, -2, -3}));            // -2

        System.out.println("\n=== Best Time to Buy and Sell Stock ===");
        System.out.println(maxProfit(new int[]{7, 1, 5, 3, 6, 4}));           // 5
        System.out.println(maxProfit(new int[]{7, 6, 4, 3, 1}));              // 0

        System.out.println("\n=== Jump Game ===");
        System.out.println(canJump(new int[]{2, 3, 1, 1, 4})); // true
        System.out.println(canJump(new int[]{3, 2, 1, 0, 4})); // false
        System.out.println(minJumps(new int[]{2, 3, 1, 1, 4})); // 2
    }
}

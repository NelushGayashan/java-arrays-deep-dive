package com.nelush.arrays.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TwoPointerTechnique
 *
 * Article section: "The Two-Pointer Technique"
 *
 * Every method in this file is taken directly from the article's code examples:
 *   1. reverseInPlace     — canonical two-pointer demonstration
 *   2. twoSumSorted       — two-pointer on sorted array
 *   3. removeDuplicatesSorted — slow/fast same-direction pointers
 *   4. maxWater           — container with most water
 *   5. threeSum           — outer loop + inner two-pointer
 *
 * From the article:
 *   "instead of checking all pairs (O(n²)), you extract information from each
 *   comparison to eliminate large portions of the search space, getting to O(n)."
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.patterns.TwoPointerTechnique"
 *
 * Author: Nelush Gayashan Fernando
 */
public class TwoPointerTechnique {

    // ─── 1. REVERSE IN-PLACE ──────────────────────────────────────────────────

    /**
     * From the article:
     * "Place left at index 0 and right at the last index. Swap arr[left] and arr[right],
     * then move both inward. When they meet or cross, every element has been swapped
     * exactly once."
     *
     * Time O(n), Space O(1)
     */
    public static void reverseInPlace(int[] arr) {
        int left  = 0;
        int right = arr.length - 1;

        while (left < right) {
            int temp   = arr[left];
            arr[left]  = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }
    // [1,2,3,4,5] → [5,4,3,2,1]
    // Time O(n), Space O(1)

    // ─── 2. TWO-SUM ON SORTED ARRAY ───────────────────────────────────────────

    /**
     * From the article:
     * "Start with left=0 and right=n-1. If their sum is less than the target, you need
     * a larger number — move left right. If the sum exceeds the target, you need a smaller
     * number — move right left. No pair is skipped."
     */
    public static int[] twoSumSorted(int[] arr, int target) {
        int left  = 0;
        int right = arr.length - 1;

        while (left < right) {
            int sum = arr[left] + arr[right];
            if      (sum == target) return new int[]{left, right};
            else if (sum <  target) left++;   // need larger — move left right
            else                    right--;  // need smaller — move right left
        }
        return new int[]{-1, -1};
    }

    // ─── 3. REMOVE DUPLICATES FROM SORTED ARRAY ───────────────────────────────

    /**
     * From the article:
     * "slow marks the boundary of unique elements — the write pointer.
     * fast scans ahead. When fast finds a value different from slow, it is a new
     * unique element — write it to slow+1 and advance slow."
     */
    public static int removeDuplicatesSorted(int[] arr) {
        if (arr.length == 0) return 0;
        int slow = 0;

        for (int fast = 1; fast < arr.length; fast++) {
            if (arr[fast] != arr[slow]) {
                slow++;
                arr[slow] = arr[fast];
            }
            // If equal — fast moves on, slow stays — duplicate is skipped
        }
        return slow + 1; // count of unique elements
    }
    // [1,1,2,3,3,4,5,5] → [1,2,3,4,5,_,_,_], returns 5

    // ─── 4. CONTAINER WITH MOST WATER ─────────────────────────────────────────

    /**
     * From the article:
     * "Volume is min(height[L], height[R]) × (R - L).
     * The two-pointer insight: always move the shorter side inward. Moving the taller
     * side cannot improve the volume (width shrinks while height is still limited by
     * the shorter side), but moving the shorter side might find a taller replacement."
     */
    public static int maxWater(int[] heights) {
        int left  = 0;
        int right = heights.length - 1;
        int max   = 0;

        while (left < right) {
            int water = Math.min(heights[left], heights[right]) * (right - left);
            max = Math.max(max, water);

            if (heights[left] <= heights[right]) left++;   // shorter side moves inward
            else                                 right--;
        }
        return max;
    }

    // ─── 5. THREE-SUM ─────────────────────────────────────────────────────────

    /**
     * From the article:
     * "Sort the array. Fix the first element at index i, then apply two-pointer
     * on the subarray [i+1, n-1]. Skip duplicates at each level to avoid repeated
     * triplets in the output."
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // skip duplicate first element

            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left]  == nums[left + 1])  left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    left++; right--;
                } else if (sum < 0) left++;
                else                right--;
            }
        }
        return result;
    }

    // ─── ADDITIONAL: MOVE ZEROES ──────────────────────────────────────────────

    /**
     * Move all zeroes to the end while preserving relative order of non-zeros.
     * Same slow/fast pattern as removeDuplicates.
     * Time O(n), Space O(1).
     */
    public static void moveZeroes(int[] arr) {
        int slow = 0;
        for (int fast = 0; fast < arr.length; fast++) {
            if (arr[fast] != 0) {
                int tmp = arr[slow]; arr[slow] = arr[fast]; arr[fast] = tmp;
                slow++;
            }
        }
    }

    // ─── ADDITIONAL: MERGE TWO SORTED ARRAYS IN-PLACE ────────────────────────

    /**
     * arr1 has m valid elements and n empty slots at the end.
     * Merge arr2 (n elements) into arr1 in sorted order.
     * Fill from RIGHT to avoid overwriting unprocessed elements.
     * Time O(m+n), Space O(1).
     */
    public static void mergeSortedInPlace(int[] arr1, int m, int[] arr2, int n) {
        int p1 = m - 1;
        int p2 = n - 1;
        int p  = m + n - 1;

        while (p2 >= 0) {
            if (p1 >= 0 && arr1[p1] > arr2[p2]) {
                arr1[p--] = arr1[p1--];
            } else {
                arr1[p--] = arr2[p2--];
            }
        }
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Two-Pointer Technique ===\n");

        // 1. Reverse
        int[] r = {1, 2, 3, 4, 5};
        reverseInPlace(r);
        System.out.println("1. reverseInPlace([1,2,3,4,5])              → " + Arrays.toString(r));

        // 2. Two-sum sorted
        int[] ts = {2, 7, 11, 15};
        System.out.println("2. twoSumSorted([2,7,11,15], target=9)      → "
            + Arrays.toString(twoSumSorted(ts, 9)));

        // 3. Remove duplicates
        int[] rd = {1, 1, 2, 3, 3, 4, 5, 5};
        int len = removeDuplicatesSorted(rd);
        System.out.println("3. removeDuplicatesSorted([1,1,2,3,3,4,5,5])→ "
            + len + " unique: " + Arrays.toString(Arrays.copyOf(rd, len)));

        // 4. Container with most water
        int[] h = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        System.out.println("4. maxWater([1,8,6,2,5,4,8,3,7])            → " + maxWater(h));

        // 5. Three-sum
        int[] nums = {-1, 0, 1, 2, -1, -4};
        System.out.println("5. threeSum([-1,0,1,2,-1,-4])               → " + threeSum(nums));

        // 6. Move zeroes
        int[] mz = {0, 1, 0, 3, 12};
        moveZeroes(mz);
        System.out.println("6. moveZeroes([0,1,0,3,12])                 → " + Arrays.toString(mz));

        // 7. Merge sorted in-place
        int[] a1 = {1, 3, 5, 0, 0, 0};
        mergeSortedInPlace(a1, 3, new int[]{2, 4, 6}, 3);
        System.out.println("7. mergeSortedInPlace                       → " + Arrays.toString(a1));
    }
}

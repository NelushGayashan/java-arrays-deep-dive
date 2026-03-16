package com.nelush.arrays.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ============================================================
 * SECTION 5: Two Pointer Technique
 * ============================================================
 *
 * The two-pointer technique uses two index variables — often called
 * left and right, or slow and fast — to scan an array in a coordinated
 * way. The payoff is converting many O(n²) brute-force solutions into
 * O(n) by ensuring each element is visited at most once per pointer.
 *
 * WHEN TWO POINTERS APPLY
 * Two pointers work when the problem has a monotonic property: moving
 * a pointer in one direction makes the current state "better" or "worse"
 * in a predictable way. This lets you decide which pointer to move
 * without having to try both directions.
 *
 * Classic signals in a problem statement:
 *   - "sorted array" or "sort first"
 *   - "two elements that sum to..."
 *   - "remove duplicates in-place"
 *   - "longest/shortest subarray with property..."
 *   - "palindrome check"
 *   - "merge two sorted arrays"
 *   - "reverse in-place"
 *
 * FLAVOURS
 *
 * Opposite-end pointers: left starts at 0, right at n-1, they move
 * toward each other. Used when reasoning about the relationship between
 * the smallest and largest remaining candidates.
 * Example: two-sum on sorted array, palindrome check, container with most water.
 *
 * Same-direction (slow-fast) pointers: both start at 0, fast advances
 * every iteration, slow advances only when a condition is met. Used for
 * in-place removal of duplicates or specific values.
 * Example: remove duplicates, remove element, sort colors.
 *
 * Partition pointers: a pivot-based scan used in quicksort and Dutch
 * National Flag. Multiple pointers each own a "region" of the array.
 *
 * CORRECTNESS GUARANTEE FOR SORTED TWO-SUM
 * Suppose arr is sorted and we want arr[L] + arr[R] == target.
 * - If arr[L] + arr[R] < target: arr[L] is too small; moving R left
 *   can only make the sum smaller. So we must move L right.
 * - If arr[L] + arr[R] > target: arr[R] is too big; moving L right
 *   can only make the sum larger. So we must move R left.
 * - Each step eliminates one candidate, so we process O(n) pairs,
 *   not O(n²).
 */
public class TwoPointer {

    // -------------------------------------------------------
    // 1. Two Sum on Sorted Array — O(n) time, O(1) space
    // -------------------------------------------------------

    /**
     * Returns indices [i, j] (1-indexed as LeetCode expects) such that
     * arr[i-1] + arr[j-1] == target, or [-1, -1] if no pair exists.
     * PRECONDITION: arr is sorted in non-decreasing order.
     */
    public static int[] twoSumSorted(int[] arr, int target) {
        int left  = 0;
        int right = arr.length - 1;

        while (left < right) {
            int sum = arr[left] + arr[right];

            if (sum == target) {
                return new int[]{left + 1, right + 1}; // 1-indexed
            } else if (sum < target) {
                left++;   // sum too small; next left element is larger
            } else {
                right--;  // sum too large; next right element is smaller
            }
        }

        return new int[]{-1, -1};
    }

    // -------------------------------------------------------
    // 2. Three Sum — find all unique triplets summing to zero
    //    Reduces to repeated Two Sum after fixing one element
    //    O(n²) time, O(1) extra space (excluding output)
    // -------------------------------------------------------

    /**
     * Strategy:
     * 1. Sort arr — enables two-pointer and duplicate skipping.
     * 2. Fix arr[i] as the first element of the triplet (i from 0 to n-3).
     * 3. Run two-pointer on arr[i+1..n-1] looking for a pair that sums
     *    to -arr[i].
     * 4. Skip duplicate values to avoid duplicate triplets in output.
     */
    public static List<List<Integer>> threeSum(int[] arr) {
        Arrays.sort(arr); // O(n log n)
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < arr.length - 2; i++) {
            // Skip duplicate values for the first element
            if (i > 0 && arr[i] == arr[i - 1]) continue;

            // Optimisation: if arr[i] > 0 all remaining elements are also
            // positive (sorted), so no triplet can sum to 0
            if (arr[i] > 0) break;

            int left  = i + 1;
            int right = arr.length - 1;

            while (left < right) {
                int sum = arr[i] + arr[left] + arr[right];

                if (sum == 0) {
                    result.add(List.of(arr[i], arr[left], arr[right]));

                    // Skip duplicates for second and third elements
                    while (left < right && arr[left]  == arr[left + 1])  left++;
                    while (left < right && arr[right] == arr[right - 1]) right--;

                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return result;
    }

    // -------------------------------------------------------
    // 3. Container With Most Water — classic opposite-end problem
    //    Given heights[i] = height of vertical line at position i,
    //    find two lines that together with the x-axis form the
    //    container holding the most water.
    // -------------------------------------------------------

    /**
     * Area = min(height[left], height[right]) × (right - left).
     *
     * Key insight: always move the pointer with the SMALLER height.
     * - If we move the taller side, width decreases AND height cannot
     *   increase (it is capped by the shorter side). Area can only fall.
     * - If we move the shorter side, width decreases but height might
     *   increase enough to compensate.
     * Moving the shorter side is our only chance of improvement.
     */
    public static int maxWater(int[] heights) {
        int left  = 0;
        int right = heights.length - 1;
        int max   = 0;

        while (left < right) {
            int h    = Math.min(heights[left], heights[right]);
            int area = h * (right - left);
            max = Math.max(max, area);

            if (heights[left] <= heights[right]) {
                left++;
            } else {
                right--;
            }
        }

        return max;
    }

    // -------------------------------------------------------
    // 4. Trapping Rain Water — harder variant
    //    For each bar, trapped water = min(maxLeft, maxRight) - height[i]
    //    Two-pointer avoids needing prefix/suffix max arrays
    // -------------------------------------------------------

    /**
     * At each step, the pointer with the smaller max-so-far is the
     * constraining side. We process that pointer because we already
     * know its water level is determined by its own max (not the other side).
     */
    public static int trapRainWater(int[] height) {
        int left  = 0;
        int right = height.length - 1;
        int leftMax  = 0;
        int rightMax = 0;
        int water    = 0;

        while (left < right) {
            if (height[left] <= height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left]; // new max on left — no water here
                } else {
                    water += leftMax - height[left]; // water trapped at left
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }

        return water;
    }

    // -------------------------------------------------------
    // 5. Remove Duplicates from Sorted Array — slow-fast pointers
    //    Returns the count of unique elements; first k elements
    //    of arr hold them in order. In-place, O(1) extra space.
    // -------------------------------------------------------

    /**
     * slow pointer marks the position for the next unique element.
     * fast pointer scans ahead looking for elements ≠ arr[slow].
     * When fast finds a new unique value, slow advances and
     * arr[slow] is overwritten.
     */
    public static int removeDuplicates(int[] arr) {
        if (arr.length == 0) return 0;

        int slow = 0; // index of the last confirmed unique element

        for (int fast = 1; fast < arr.length; fast++) {
            if (arr[fast] != arr[slow]) {
                slow++;               // advance slot for next unique
                arr[slow] = arr[fast]; // write the new unique value
            }
            // if equal, fast just keeps moving — duplicate is ignored
        }

        return slow + 1; // number of unique elements
    }

    // -------------------------------------------------------
    // 6. Remove Element — remove all occurrences of val in-place
    //    Returns new length. O(n) time, O(1) space.
    // -------------------------------------------------------

    public static int removeElement(int[] arr, int val) {
        int slow = 0;

        for (int fast = 0; fast < arr.length; fast++) {
            if (arr[fast] != val) {
                arr[slow++] = arr[fast]; // copy non-val elements forward
            }
            // arr[fast] == val: just skip it
        }

        return slow;
    }

    // -------------------------------------------------------
    // 7. Sort Colors (Dutch National Flag algorithm)
    //    In-place sort of array containing only 0, 1, 2
    //    One pass, O(n) time, O(1) space
    // -------------------------------------------------------

    /**
     * Three-pointer partition:
     *   low  : everything to the left of low is 0
     *   mid  : current element being examined
     *   high : everything to the right of high is 2
     *
     * Invariant at all times:
     *   arr[0..low-1]     = 0s
     *   arr[low..mid-1]   = 1s
     *   arr[mid..high]    = unknown
     *   arr[high+1..n-1]  = 2s
     *
     * When arr[mid] == 0: swap with low region, advance both
     * When arr[mid] == 1: it belongs in the middle, just advance mid
     * When arr[mid] == 2: swap with high region, shrink high
     *                     (do NOT advance mid — swapped element is unexamined)
     */
    public static void sortColors(int[] arr) {
        int low  = 0;
        int mid  = 0;
        int high = arr.length - 1;

        while (mid <= high) {
            if (arr[mid] == 0) {
                swap(arr, low++, mid++);
            } else if (arr[mid] == 1) {
                mid++;
            } else { // arr[mid] == 2
                swap(arr, mid, high--);
                // do NOT increment mid: arr[mid] is now the element we swapped in
            }
        }
    }

    // -------------------------------------------------------
    // 8. Palindrome Check — opposite-end pointers
    // -------------------------------------------------------

    public static boolean isPalindrome(String s) {
        int left  = 0;
        int right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }

        return true;
    }

    // Palindrome check that ignores non-alphanumeric characters
    public static boolean isPalindromeAlphanumeric(String s) {
        int left  = 0;
        int right = s.length() - 1;

        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left)))  left++;
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) right--;

            if (Character.toLowerCase(s.charAt(left)) !=
                Character.toLowerCase(s.charAt(right))) return false;

            left++;
            right--;
        }

        return true;
    }

    // -------------------------------------------------------
    // 9. Reverse Array in Place — two-pointer swap
    // -------------------------------------------------------

    public static void reverse(int[] arr) {
        int left  = 0;
        int right = arr.length - 1;

        while (left < right) {
            swap(arr, left++, right--);
        }
    }

    // -------------------------------------------------------
    // 10. Merge Two Sorted Arrays — same-direction scan
    //     into a result array. O(m + n).
    // -------------------------------------------------------

    public static int[] mergeSorted(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;

        while (i < a.length && j < b.length) {
            if (a[i] <= b[j]) result[k++] = a[i++];
            else               result[k++] = b[j++];
        }

        while (i < a.length) result[k++] = a[i++];
        while (j < b.length) result[k++] = b[j++];

        return result;
    }

    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
    }

    public static void main(String[] args) {
        System.out.println("=== Two Sum Sorted ===");
        int[] ts = {2, 7, 11, 15};
        System.out.println(Arrays.toString(twoSumSorted(ts, 9)));   // [1, 2]
        System.out.println(Arrays.toString(twoSumSorted(ts, 100))); // [-1, -1]

        System.out.println("\n=== Three Sum ===");
        int[] arr3 = {-1, 0, 1, 2, -1, -4};
        System.out.println(threeSum(arr3)); // [[-1,-1,2],[-1,0,1]]

        System.out.println("\n=== Max Water ===");
        System.out.println(maxWater(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7})); // 49

        System.out.println("\n=== Trap Rain Water ===");
        System.out.println(trapRainWater(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})); // 6

        System.out.println("\n=== Remove Duplicates ===");
        int[] rd = {1, 1, 2, 3, 3, 4};
        int k = removeDuplicates(rd);
        System.out.println("k=" + k + " arr=" + Arrays.toString(Arrays.copyOf(rd, k)));

        System.out.println("\n=== Sort Colors ===");
        int[] colors = {2, 0, 2, 1, 1, 0};
        sortColors(colors);
        System.out.println(Arrays.toString(colors)); // [0, 0, 1, 1, 2, 2]

        System.out.println("\n=== Palindrome ===");
        System.out.println(isPalindrome("racecar"));  // true
        System.out.println(isPalindromeAlphanumeric("A man, a plan, a canal: Panama")); // true
    }
}

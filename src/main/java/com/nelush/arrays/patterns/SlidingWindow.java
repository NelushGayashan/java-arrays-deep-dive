package com.nelush.arrays.patterns;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * SlidingWindow
 *
 * Article section: "The Sliding Window Technique"
 *
 * Every method taken directly from the article:
 *   1. maxSumFixed                — fixed-size window, max subarray sum
 *   2. lengthOfLongestSubstring   — variable window, no repeating characters
 *   3. minSizeSubarraySum         — variable window, minimum length >= target
 *   4. slidingWindowMax           — monotonic deque for window maximum
 *
 * From the article:
 *   "Because each element enters the window once and leaves once, the total
 *   work across all iterations is O(n) — not O(n×k)."
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.patterns.SlidingWindow"
 *
 * Author: Nelush Gayashan Fernando
 */
public class SlidingWindow {

    // ─── 1. FIXED-SIZE WINDOW — MAX SUM ──────────────────────────────────────

    /**
     * From the article:
     * "Compute the sum of the first window, then for each step: subtract the element
     * leaving from the left and add the element entering from the right."
     *
     * arr=[2,1,5,1,3,2], k=3 → 9 (the window [5,1,3])
     */
    public static int maxSumFixed(int[] arr, int k) {
        int windowSum = 0;
        for (int i = 0; i < k; i++) windowSum += arr[i]; // first window

        int maxSum = windowSum;
        for (int i = k; i < arr.length; i++) {
            windowSum += arr[i];      // new element enters from right
            windowSum -= arr[i - k]; // old element leaves from left
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // ─── 2. VARIABLE WINDOW — LONGEST SUBSTRING WITHOUT REPEATING ────────────

    /**
     * From the article:
     * "Track the most recent index of each character. When a duplicate enters the
     * window, jump left past the earlier occurrence."
     *
     * "abcabcbb" → 3 ("abc")
     */
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeen = new HashMap<>();
        int maxLen = 0, left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);

            // If c was inside the current window, shrink left past its old position
            if (lastSeen.containsKey(c) && lastSeen.get(c) >= left) {
                left = lastSeen.get(c) + 1;
            }

            lastSeen.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
    // "abcabcbb" → 3 ("abc")

    // ─── 3. VARIABLE WINDOW — MINIMUM SIZE SUBARRAY SUM ≥ TARGET ─────────────

    /**
     * From the article:
     * "expand right until the constraint is met, record the window size, then shrink
     * from the left to see if a smaller window still satisfies the constraint."
     *
     * nums=[2,3,1,2,4,3], target=7 → 2 (the subarray [4,3])
     */
    public static int minSizeSubarraySum(int[] nums, int target) {
        int left = 0, sum = 0, minLen = Integer.MAX_VALUE;

        for (int right = 0; right < nums.length; right++) {
            sum += nums[right]; // expand

            while (sum >= target) { // constraint met — record and try to shrink
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left++]; // shrink from left
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }
    // nums=[2,3,1,2,4,3], target=7 → 2 (the subarray [4,3])

    // ─── 4. MONOTONIC DEQUE — SLIDING WINDOW MAXIMUM ─────────────────────────

    /**
     * From the article:
     * "a double-ended queue that maintains indices in decreasing order of their
     * array values. The front always holds the index of the current window's maximum.
     * When a new element is larger than or equal to the rear element, the rear is
     * popped (it can never be the maximum of any future window)."
     *
     * arr=[1,3,-1,-3,5,3,6,7], k=3 → [3,3,5,5,6,7]
     */
    public static int[] slidingWindowMax(int[] arr, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        int[] result = new int[arr.length - k + 1];

        for (int i = 0; i < arr.length; i++) {
            // Remove indices that have left the window
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) deque.pollFirst();

            // Remove rear indices whose values are smaller — they can never be max
            while (!deque.isEmpty() && arr[deque.peekLast()] < arr[i]) deque.pollLast();

            deque.offerLast(i);
            if (i >= k - 1) result[i - k + 1] = arr[deque.peekFirst()];
        }
        return result;
    }
    // arr=[1,3,-1,-3,5,3,6,7], k=3 → [3,3,5,5,6,7]

    // ─── ADDITIONAL: PERMUTATION IN STRING ───────────────────────────────────

    /**
     * Check if any permutation of pattern p exists as a substring of s.
     * Fixed window of size p.length() with frequency comparison.
     * Time O(n), Space O(1) — only 26 lowercase letters.
     */
    public static boolean permutationInString(String s, String p) {
        if (p.length() > s.length()) return false;
        int[] pFreq = new int[26], wFreq = new int[26];
        for (int i = 0; i < p.length(); i++) {
            pFreq[p.charAt(i) - 'a']++;
            wFreq[s.charAt(i) - 'a']++;
        }
        if (Arrays.equals(pFreq, wFreq)) return true;
        for (int i = p.length(); i < s.length(); i++) {
            wFreq[s.charAt(i) - 'a']++;
            wFreq[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(pFreq, wFreq)) return true;
        }
        return false;
    }

    // ─── ADDITIONAL: LONGEST SUBARRAY WITH AT MOST K DISTINCT VALUES ─────────

    /**
     * Expand right; when distinct count exceeds k, shrink left.
     * Time O(n), Space O(k).
     */
    public static int longestSubarrayKDistinct(int[] arr, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0, maxLen = 0;
        for (int right = 0; right < arr.length; right++) {
            freq.merge(arr[right], 1, Integer::sum);
            while (freq.size() > k) {
                int lv = arr[left];
                freq.put(lv, freq.get(lv) - 1);
                if (freq.get(lv) == 0) freq.remove(lv);
                left++;
            }
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Sliding Window Technique ===\n");

        System.out.println("1. maxSumFixed([2,1,5,1,3,2], k=3)                    → "
            + maxSumFixed(new int[]{2, 1, 5, 1, 3, 2}, 3));

        System.out.println("2. lengthOfLongestSubstring(\"abcabcbb\")               → "
            + lengthOfLongestSubstring("abcabcbb"));
        System.out.println("   lengthOfLongestSubstring(\"pwwkew\")                 → "
            + lengthOfLongestSubstring("pwwkew"));

        System.out.println("3. minSizeSubarraySum([2,3,1,2,4,3], target=7)        → "
            + minSizeSubarraySum(new int[]{2, 3, 1, 2, 4, 3}, 7));

        System.out.println("4. slidingWindowMax([1,3,-1,-3,5,3,6,7], k=3)        → "
            + Arrays.toString(slidingWindowMax(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));

        System.out.println("5. permutationInString(\"eidbaooo\", \"ab\")             → "
            + permutationInString("eidbaooo", "ab"));

        System.out.println("6. longestSubarrayKDistinct([1,2,1,2,3], k=2)        → "
            + longestSubarrayKDistinct(new int[]{1, 2, 1, 2, 3}, 2));
    }
}

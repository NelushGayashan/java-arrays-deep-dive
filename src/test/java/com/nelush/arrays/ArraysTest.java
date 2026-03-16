package com.nelush.arrays;

import com.nelush.arrays.algorithms.BinarySearch;
import com.nelush.arrays.algorithms.DutchNationalFlag;
import com.nelush.arrays.algorithms.KadaneAlgorithm;
import com.nelush.arrays.algorithms.SortingAlgorithms;
import com.nelush.arrays.patterns.PrefixSum;
import com.nelush.arrays.patterns.SlidingWindow;
import com.nelush.arrays.patterns.TwoPointerTechnique;
import com.nelush.arrays.advanced.MatrixOperations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ArraysTest
 *
 * Comprehensive JUnit 5 test suite.
 * Every test case is drawn from examples stated in the article.
 *
 * Run: mvn test
 *
 * Author: Nelush Gayashan Fernando
 */
@DisplayName("Java Arrays Deep Dive — Full Test Suite")
class ArraysTest {

    // ═══════════════════════════════════════════════════════════════
    //  TWO-POINTER TECHNIQUE
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§5 Two-Pointer Technique")
    class TwoPointerTests {

        @Test
        @DisplayName("reverseInPlace [1,2,3,4,5] → [5,4,3,2,1]")
        void reverseOdd() {
            int[] arr = {1, 2, 3, 4, 5};
            TwoPointerTechnique.reverseInPlace(arr);
            assertArrayEquals(new int[]{5, 4, 3, 2, 1}, arr);
        }

        @Test
        @DisplayName("reverseInPlace single element unchanged")
        void reverseSingle() {
            int[] arr = {42};
            TwoPointerTechnique.reverseInPlace(arr);
            assertArrayEquals(new int[]{42}, arr);
        }

        @Test
        @DisplayName("twoSumSorted [2,7,11,15] target=9 → [0,1]")
        void twoSumFound() {
            assertArrayEquals(new int[]{0, 1},
                TwoPointerTechnique.twoSumSorted(new int[]{2, 7, 11, 15}, 9));
        }

        @Test
        @DisplayName("twoSumSorted no pair → [-1,-1]")
        void twoSumNotFound() {
            assertArrayEquals(new int[]{-1, -1},
                TwoPointerTechnique.twoSumSorted(new int[]{1, 2, 3, 4}, 100));
        }

        @Test
        @DisplayName("removeDuplicatesSorted [1,1,2,3,3,4,5,5] → length 5")
        void removeDups() {
            int[] arr = {1, 1, 2, 3, 3, 4, 5, 5};
            int len = TwoPointerTechnique.removeDuplicatesSorted(arr);
            assertEquals(5, len);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, Arrays.copyOf(arr, len));
        }

        @Test
        @DisplayName("maxWater [1,8,6,2,5,4,8,3,7] → 49")
        void maxWater() {
            assertEquals(49, TwoPointerTechnique.maxWater(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
        }

        @Test
        @DisplayName("maxWater two bars")
        void maxWaterTwo() {
            assertEquals(1, TwoPointerTechnique.maxWater(new int[]{1, 1}));
        }

        @Test
        @DisplayName("threeSum [-1,0,1,2,-1,-4] → [[-1,-1,2],[-1,0,1]]")
        void threeSumBasic() {
            List<List<Integer>> result =
                TwoPointerTechnique.threeSum(new int[]{-1, 0, 1, 2, -1, -4});
            assertEquals(2, result.size());
            assertTrue(result.contains(Arrays.asList(-1, -1, 2)));
            assertTrue(result.contains(Arrays.asList(-1, 0, 1)));
        }

        @Test
        @DisplayName("threeSum all zeros → [[0,0,0]]")
        void threeSumZeros() {
            List<List<Integer>> result =
                TwoPointerTechnique.threeSum(new int[]{0, 0, 0});
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("moveZeroes [0,1,0,3,12] → [1,3,12,0,0]")
        void moveZeroes() {
            int[] arr = {0, 1, 0, 3, 12};
            TwoPointerTechnique.moveZeroes(arr);
            assertArrayEquals(new int[]{1, 3, 12, 0, 0}, arr);
        }

        @Test
        @DisplayName("mergeSortedInPlace [1,3,5,0,0,0] + [2,4,6] → [1,2,3,4,5,6]")
        void mergeSorted() {
            int[] arr1 = {1, 3, 5, 0, 0, 0};
            TwoPointerTechnique.mergeSortedInPlace(arr1, 3, new int[]{2, 4, 6}, 3);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, arr1);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  SLIDING WINDOW
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§6 Sliding Window")
    class SlidingWindowTests {

        @Test
        @DisplayName("maxSumFixed [2,1,5,1,3,2] k=3 → 9")
        void maxSumFixed() {
            assertEquals(9, SlidingWindow.maxSumFixed(new int[]{2, 1, 5, 1, 3, 2}, 3));
        }

        @Test
        @DisplayName("lengthOfLongestSubstring 'abcabcbb' → 3")
        void longestSubstringAbc() {
            assertEquals(3, SlidingWindow.lengthOfLongestSubstring("abcabcbb"));
        }

        @Test
        @DisplayName("lengthOfLongestSubstring 'bbbbb' → 1")
        void longestSubstringAllSame() {
            assertEquals(1, SlidingWindow.lengthOfLongestSubstring("bbbbb"));
        }

        @Test
        @DisplayName("lengthOfLongestSubstring 'pwwkew' → 3")
        void longestSubstringPwwkew() {
            assertEquals(3, SlidingWindow.lengthOfLongestSubstring("pwwkew"));
        }

        @Test
        @DisplayName("minSizeSubarraySum [2,3,1,2,4,3] target=7 → 2")
        void minSizeSum() {
            assertEquals(2, SlidingWindow.minSizeSubarraySum(new int[]{2, 3, 1, 2, 4, 3}, 7));
        }

        @Test
        @DisplayName("minSizeSubarraySum impossible → 0")
        void minSizeSumImpossible() {
            assertEquals(0, SlidingWindow.minSizeSubarraySum(new int[]{1, 1, 1}, 10));
        }

        @Test
        @DisplayName("slidingWindowMax [1,3,-1,-3,5,3,6,7] k=3 → [3,3,5,5,6,7]")
        void slidingWindowMax() {
            assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7},
                SlidingWindow.slidingWindowMax(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        }

        @Test
        @DisplayName("permutationInString 'eidbaooo','ab' → true")
        void permutationTrue() {
            assertTrue(SlidingWindow.permutationInString("eidbaooo", "ab"));
        }

        @Test
        @DisplayName("permutationInString 'eidboaoo','ab' → false")
        void permutationFalse() {
            assertFalse(SlidingWindow.permutationInString("eidboaoo", "ab"));
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  PREFIX SUM
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§7 Prefix Sum")
    class PrefixSumTests {

        @Test
        @DisplayName("rangeSum(0,3) = 9 on [3,1,4,1,5,9,2,6]")
        void rangeSum03() {
            int[] prefix = PrefixSum.buildPrefixSum(new int[]{3, 1, 4, 1, 5, 9, 2, 6});
            assertEquals(9, PrefixSum.rangeSum(prefix, 0, 3));
        }

        @Test
        @DisplayName("rangeSum(2,5) = 19")
        void rangeSum25() {
            int[] prefix = PrefixSum.buildPrefixSum(new int[]{3, 1, 4, 1, 5, 9, 2, 6});
            assertEquals(19, PrefixSum.rangeSum(prefix, 2, 5));
        }

        @Test
        @DisplayName("rangeSum full array = 31")
        void rangeSumFull() {
            int[] prefix = PrefixSum.buildPrefixSum(new int[]{3, 1, 4, 1, 5, 9, 2, 6});
            assertEquals(31, PrefixSum.rangeSum(prefix, 0, 7));
        }

        @Test
        @DisplayName("subarraySumEqualsK [1,1,1] k=2 → 2")
        void subarraySumK2() {
            assertEquals(2, PrefixSum.subarraySumEqualsK(new int[]{1, 1, 1}, 2));
        }

        @Test
        @DisplayName("subarraySumEqualsK [1,2,3] k=3 → 2 (subarray [3] and [1,2])")
        void subarraySumK3() {
            assertEquals(2, PrefixSum.subarraySumEqualsK(new int[]{1, 2, 3}, 3));
        }

        @Test
        @DisplayName("productExceptSelf [1,2,3,4] → [24,12,8,6]")
        void productExceptSelf() {
            assertArrayEquals(new int[]{24, 12, 8, 6},
                PrefixSum.productExceptSelf(new int[]{1, 2, 3, 4}));
        }

        @Test
        @DisplayName("equilibriumIndex [-7,1,5,2,-4,3,0] → 3")
        void equilibrium() {
            assertEquals(3, PrefixSum.equilibriumIndex(new int[]{-7, 1, 5, 2, -4, 3, 0}));
        }

        @Test
        @DisplayName("equilibriumIndex no solution → -1")
        void equilibriumNone() {
            assertEquals(-1, PrefixSum.equilibriumIndex(new int[]{1, 2, 3}));
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  KADANE'S ALGORITHM
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§8 Kadane's Algorithm")
    class KadaneTests {

        @Test
        @DisplayName("maxSubarraySum [-2,1,-3,4,-1,2,1,-5,4] → 6")
        void classic() {
            assertEquals(6, KadaneAlgorithm.maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        }

        @Test
        @DisplayName("maxSubarraySum all negative → least negative")
        void allNegative() {
            assertEquals(-1, KadaneAlgorithm.maxSubarraySum(new int[]{-5, -3, -1, -4}));
        }

        @Test
        @DisplayName("maxSubarrayWithIndices → max=6, indices [3..6]")
        void withIndices() {
            int[] res = KadaneAlgorithm.maxSubarrayWithIndices(
                new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4});
            assertEquals(6, res[0]);
            assertEquals(3, res[1]);
            assertEquals(6, res[2]);
        }

        @Test
        @DisplayName("maxSubarrayProduct [2,3,-2,4] → 6")
        void productPositive() {
            assertEquals(6, KadaneAlgorithm.maxSubarrayProduct(new int[]{2, 3, -2, 4}));
        }

        @Test
        @DisplayName("maxSubarrayProduct [-2,3,-4] → 24")
        void productNegativeFlip() {
            assertEquals(24, KadaneAlgorithm.maxSubarrayProduct(new int[]{-2, 3, -4}));
        }

        @Test
        @DisplayName("maxSubarrayProduct [-2,0,-1] → 0")
        void productWithZero() {
            assertEquals(0, KadaneAlgorithm.maxSubarrayProduct(new int[]{-2, 0, -1}));
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  BINARY SEARCH
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§9 Binary Search")
    class BinarySearchTests {

        @Test
        @DisplayName("binarySearch found → correct index")
        void found() {
            assertEquals(2, BinarySearch.binarySearch(new int[]{1, 3, 5, 7, 9}, 5));
        }

        @Test
        @DisplayName("binarySearch not found → -1")
        void notFound() {
            assertEquals(-1, BinarySearch.binarySearch(new int[]{1, 3, 5, 7, 9}, 4));
        }

        @Test
        @DisplayName("firstOccurrence returns leftmost index")
        void firstOcc() {
            assertEquals(1, BinarySearch.firstOccurrence(new int[]{1, 2, 2, 2, 3}, 2));
        }

        @Test
        @DisplayName("lastOccurrence returns rightmost index")
        void lastOcc() {
            assertEquals(3, BinarySearch.lastOccurrence(new int[]{1, 2, 2, 2, 3}, 2));
        }

        @Test
        @DisplayName("countOccurrences = 3")
        void count() {
            assertEquals(3, BinarySearch.countOccurrences(new int[]{1, 2, 2, 2, 3}, 2));
        }

        @Test
        @DisplayName("countOccurrences missing → 0")
        void countMissing() {
            assertEquals(0, BinarySearch.countOccurrences(new int[]{1, 2, 3}, 9));
        }

        @Test
        @DisplayName("searchRotated [4,5,6,7,0,1,2] target=0 → index 4")
        void rotatedFound() {
            assertEquals(4, BinarySearch.searchRotated(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        }

        @Test
        @DisplayName("searchRotated missing → -1")
        void rotatedNotFound() {
            assertEquals(-1, BinarySearch.searchRotated(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
        }

        @Test
        @DisplayName("findMinRotated [4,5,6,7,0,1,2] → 0")
        void minRotated() {
            assertEquals(0, BinarySearch.findMinRotated(new int[]{4, 5, 6, 7, 0, 1, 2}));
        }

        @Test
        @DisplayName("minPagesPerDay [10,20,30,40] days=2 → 60")
        void minPages() {
            assertEquals(60, BinarySearch.minPagesPerDay(new int[]{10, 20, 30, 40}, 2));
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  DUTCH NATIONAL FLAG
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§10 Dutch National Flag")
    class DutchNationalFlagTests {

        @Test
        @DisplayName("sortColors [2,0,2,1,1,0] → [0,0,1,1,2,2]")
        void sortBasic() {
            int[] arr = {2, 0, 2, 1, 1, 0};
            DutchNationalFlag.sortColors(arr);
            assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, arr);
        }

        @Test
        @DisplayName("sortColors all same → unchanged")
        void sortAllSame() {
            int[] arr = {1, 1, 1};
            DutchNationalFlag.sortColors(arr);
            assertArrayEquals(new int[]{1, 1, 1}, arr);
        }

        @Test
        @DisplayName("sortColors single element → unchanged")
        void sortSingle() {
            int[] arr = {2};
            DutchNationalFlag.sortColors(arr);
            assertArrayEquals(new int[]{2}, arr);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  SORTING ALGORITHMS
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§11 Sorting Algorithms")
    class SortingTests {

        private final int[] unsorted = {5, 3, 8, 1, 9, 2, 7, 4, 6};
        private final int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        @Test @DisplayName("Bubble Sort")
        void bubble() {
            int[] a = unsorted.clone(); SortingAlgorithms.bubbleSort(a);
            assertArrayEquals(expected, a);
        }

        @Test @DisplayName("Insertion Sort")
        void insertion() {
            int[] a = unsorted.clone(); SortingAlgorithms.insertionSort(a);
            assertArrayEquals(expected, a);
        }

        @Test @DisplayName("Selection Sort")
        void selection() {
            int[] a = unsorted.clone(); SortingAlgorithms.selectionSort(a);
            assertArrayEquals(expected, a);
        }

        @Test @DisplayName("Merge Sort")
        void merge() {
            int[] a = unsorted.clone();
            SortingAlgorithms.mergeSort(a, 0, a.length - 1);
            assertArrayEquals(expected, a);
        }

        @Test @DisplayName("Quick Sort")
        void quick() {
            int[] a = unsorted.clone();
            SortingAlgorithms.quickSort(a, 0, a.length - 1);
            assertArrayEquals(expected, a);
        }

        @Test @DisplayName("Counting Sort")
        void counting() {
            assertArrayEquals(new int[]{1, 2, 2, 3, 3, 4, 8},
                SortingAlgorithms.countingSort(new int[]{4, 2, 2, 8, 3, 3, 1}, 8));
        }

        @Test @DisplayName("Bubble Sort already sorted — O(n) early exit")
        void bubbleSorted() {
            int[] a = {1, 2, 3, 4, 5}; SortingAlgorithms.bubbleSort(a);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, a);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  MATRIX OPERATIONS
    // ═══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("§12 Matrix Operations")
    class MatrixTests {

        @Test
        @DisplayName("rotate90Clockwise [[1,2,3],[4,5,6],[7,8,9]] → [[7,4,1],[8,5,2],[9,6,3]]")
        void rotate90() {
            int[][] m = {{1,2,3},{4,5,6},{7,8,9}};
            MatrixOperations.rotate90Clockwise(m);
            assertArrayEquals(new int[]{7, 4, 1}, m[0]);
            assertArrayEquals(new int[]{8, 5, 2}, m[1]);
            assertArrayEquals(new int[]{9, 6, 3}, m[2]);
        }

        @Test
        @DisplayName("spiralOrder [[1,2,3],[4,5,6],[7,8,9]] → [1,2,3,6,9,8,7,4,5]")
        void spiral() {
            int[][] m = {{1,2,3},{4,5,6},{7,8,9}};
            assertArrayEquals(new int[]{1,2,3,6,9,8,7,4,5}, MatrixOperations.spiralOrder(m));
        }

        @Test
        @DisplayName("setZeroes [[1,1,1],[1,0,1],[1,1,1]] → zeros in row1 and col1")
        void setZeroes() {
            int[][] m = {{1,1,1},{1,0,1},{1,1,1}};
            MatrixOperations.setZeroes(m);
            assertArrayEquals(new int[]{1, 0, 1}, m[0]);
            assertArrayEquals(new int[]{0, 0, 0}, m[1]);
            assertArrayEquals(new int[]{1, 0, 1}, m[2]);
        }
    }
}

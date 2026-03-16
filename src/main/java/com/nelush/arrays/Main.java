package com.nelush.arrays;

import com.nelush.arrays.basics.ArrayMemoryModel;
import com.nelush.arrays.basics.ArrayOperations;
import com.nelush.arrays.arraylist.DynamicArrayInternals;
import com.nelush.arrays.algorithms.BinarySearch;
import com.nelush.arrays.algorithms.SortingAlgorithms;
import com.nelush.arrays.patterns.TwoPointer;
import com.nelush.arrays.patterns.SlidingWindow;
import com.nelush.arrays.patterns.PrefixSum;
import com.nelush.arrays.patterns.KadaneAlgorithm;
import com.nelush.arrays.practical.ClassicInterviewProblems;

/**
 * ============================================================
 * Main Runner — Java Arrays Deep Dive
 * ============================================================
 *
 * Companion project for the Medium article:
 * "Arrays — The Foundation Every Java Developer Must Master"
 * by Nelush Gayashan (github.com/NelushGayashan)
 *
 * Run this class to see the output of every section, or run
 * individual classes directly for focused exploration.
 *
 * Project structure:
 *   basics/       — memory model, JVM internals, core operations
 *   arraylist/    — dynamic array internals, ArrayList API
 *   algorithms/   — binary search, sorting algorithms
 *   patterns/     — two-pointer, sliding window, prefix sum, Kadane's
 *   practical/    — classic interview problems combining all patterns
 */
public class Main {

    public static void main(String[] args) {
        separator("SECTION 1: Array Memory Model and JVM Internals");
        ArrayMemoryModel.main(args);

        separator("SECTION 2: Array Operations and the Arrays API");
        ArrayOperations.main(args);

        separator("SECTION 3: ArrayList — Dynamic Array Internals");
        DynamicArrayInternals.main(args);

        separator("SECTION 4: Binary Search — All Variants");
        BinarySearch.main(args);

        separator("SECTION 5: Two Pointer Technique");
        TwoPointer.main(args);

        separator("SECTION 6: Sliding Window Technique");
        SlidingWindow.main(args);

        separator("SECTION 7: Prefix Sum");
        PrefixSum.main(args);

        separator("SECTION 8: Kadane's Algorithm and DP on Arrays");
        KadaneAlgorithm.main(args);

        separator("SECTION 9: Sorting Algorithms");
        SortingAlgorithms.main(args);

        separator("SECTION 10: Classic Interview Problems");
        ClassicInterviewProblems.main(args);
    }

    private static void separator(String title) {
        System.out.println("\n");
        System.out.println("═".repeat(60));
        System.out.println("  " + title);
        System.out.println("═".repeat(60));
    }
}

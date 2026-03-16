package com.nelush.arrays;

import com.nelush.arrays.basics.ArrayMemoryModel;
import com.nelush.arrays.basics.ArraysUtility;
import com.nelush.arrays.basics.CacheLocality;
import com.nelush.arrays.dynamicarray.DynamicArrayInternals;
import com.nelush.arrays.patterns.TwoPointerTechnique;
import com.nelush.arrays.patterns.SlidingWindow;
import com.nelush.arrays.patterns.PrefixSum;
import com.nelush.arrays.algorithms.KadaneAlgorithm;
import com.nelush.arrays.algorithms.BinarySearch;
import com.nelush.arrays.algorithms.DutchNationalFlag;
import com.nelush.arrays.algorithms.SortingAlgorithms;
import com.nelush.arrays.advanced.MatrixOperations;

/**
 * ArrayRunner
 *
 * Central entry point for the Java Arrays Deep Dive project.
 * Runs all section demos in the order they appear in the article.
 *
 * Companion project for the Medium article:
 * "Arrays in Java: The Complete Foundation Every Developer Must Master"
 * by Nelush Gayashan Fernando
 *
 * GitHub: https://github.com/NelushGayashan/java-arrays-deep-dive
 *
 * Usage:
 *   Run everything:
 *     mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.ArrayRunner"
 *
 *   Run a single section:
 *     mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.patterns.TwoPointerTechnique"
 *
 *   Run tests:
 *     mvn test
 *
 * Author: Nelush Gayashan Fernando
 */
public class ArrayRunner {

    public static void main(String[] args) {
        section("§1  WHAT AN ARRAY ACTUALLY IS + PRIMITIVE vs OBJECT ARRAYS");
        ArrayMemoryModel.main(args);

        section("§2  THE COMPLETE java.util.Arrays TOOLKIT");
        ArraysUtility.main(args);

        section("§3  CACHE LOCALITY: WHY ARRAYS WIN IN PRACTICE");
        CacheLocality.main(args);

        section("§4  DYNAMIC ARRAYS — ArrayList INTERNALS");
        DynamicArrayInternals.main(args);

        section("§5  THE TWO-POINTER TECHNIQUE");
        TwoPointerTechnique.main(args);

        section("§6  THE SLIDING WINDOW TECHNIQUE");
        SlidingWindow.main(args);

        section("§7  PREFIX SUM");
        PrefixSum.main(args);

        section("§8  KADANE'S ALGORITHM");
        KadaneAlgorithm.main(args);

        section("§9  BINARY SEARCH ON ARRAYS");
        BinarySearch.main(args);

        section("§10 DUTCH NATIONAL FLAG ALGORITHM");
        DutchNationalFlag.main(args);

        section("§11 SORTING ALGORITHMS — WHAT ACTUALLY HAPPENS");
        SortingAlgorithms.main(args);

        section("§12 MATRIX OPERATIONS ON 2D ARRAYS");
        MatrixOperations.main(args);
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("═".repeat(65));
        System.out.println("  " + title);
        System.out.println("═".repeat(65));
    }
}

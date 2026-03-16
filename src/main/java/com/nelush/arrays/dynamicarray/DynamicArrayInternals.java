package com.nelush.arrays.dynamicarray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DynamicArrayInternals
 *
 * Article section: "Dynamic Arrays — ArrayList Internals"
 *
 * Covers:
 *   - Why ArrayList is O(1) amortized for append despite occasional O(n) resize
 *   - The 1.5x growth factor (OpenJDK)
 *   - Pre-allocation pattern when size is known
 *   - Core operation complexity
 *   - Programming to the List interface
 *
 * Also contains CustomDynamicArray — a from-scratch implementation that makes
 * the resize mechanics visible at runtime.
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.dynamicarray.DynamicArrayInternals"
 *
 * Author: Nelush Gayashan Fernando
 */
public class DynamicArrayInternals {

    // ─── RESIZE MECHANICS & PRE-ALLOCATION ───────────────────────────────────

    /**
     * From the article:
     * "When the internal array is full and you call add(), ArrayList allocates
     * a new array approximately 1.5 times larger, copies every element into it,
     * then appends the new element."
     *
     * "if you double the capacity each time, a list that grew to size n required
     * at most 1 + 2 + 4 + ... + n/2 + n copies total, which sums to roughly 2n.
     * So 2n total copy work for n elements means O(1) per element amortised."
     */
    public static void resizeMechanicsDemo() {
        System.out.println("=== ArrayList Resize Mechanics ===");

        // Without pre-allocation — ~20 resizes for 1M elements
        long start = System.nanoTime();
        List<Integer> dynamic = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) dynamic.add(i);
        long dynamicMs = (System.nanoTime() - start) / 1_000_000;

        // With pre-allocation — 0 resizes, from the article
        start = System.nanoTime();
        List<Integer> preAlloc = new ArrayList<>(1_000_000);
        for (int i = 0; i < 1_000_000; i++) preAlloc.add(i);
        long preAllocMs = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("Default capacity  (1,000,000 elements): %d ms%n", dynamicMs);
        System.out.printf("Pre-allocated     (1,000,000 elements): %d ms%n", preAllocMs);
        System.out.println("Pre-allocation avoids all intermediate array copies.");
    }

    // ─── CORE OPERATION COMPLEXITY ────────────────────────────────────────────

    /**
     * Exact code from the article's complexity section:
     *
     *   list.add(element)       O(1) amortized — append to tail
     *   list.add(0, element)    O(n) — shift everything right first
     *   list.get(5)             O(1) — direct index
     *   list.remove(0)          O(n) — shift everything left
     *   list.remove(element)    O(n) — linear scan + shift
     */
    public static void operationComplexityDemo() {
        System.out.println("\n=== Core Operation Complexity ===");
        List<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));

        list.add(60);        // O(1) amortized — append to tail
        System.out.println("After add(60)       : " + list);

        list.add(0, 99);     // O(n) — shift everything right first
        System.out.println("After add(0, 99)    : " + list);

        System.out.println("get(2)              : " + list.get(2)); // O(1)

        list.remove(0);      // O(n) — shift everything left
        System.out.println("After remove(0)     : " + list);

        list.remove(Integer.valueOf(30)); // O(n) — linear scan + shift
        System.out.println("After remove(30)    : " + list);
    }

    // ─── PROGRAMMING TO THE LIST INTERFACE ───────────────────────────────────

    /**
     * From the article:
     * "Always declare the variable as List<T>, not ArrayList<T>.
     * Programming to the interface decouples your code from the concrete implementation."
     */
    public static void interfaceBasedProgramming() {
        System.out.println("\n=== Interface-Based Programming ===");

        // Correct — decoupled from ArrayList implementation
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");
        System.out.println("List<String> names = new ArrayList<>() : " + names);

        // Can swap implementation without changing consuming code
        // List<String> names = new LinkedList<>();  // same interface, different impl
    }

    public static void main(String[] args) {
        resizeMechanicsDemo();
        operationComplexityDemo();
        interfaceBasedProgramming();

        System.out.println("\n--- Running CustomDynamicArray demo ---");
        CustomDynamicArray.main(args);
    }
}

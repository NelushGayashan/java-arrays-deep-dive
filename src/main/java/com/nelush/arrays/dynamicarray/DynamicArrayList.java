package com.nelush.arrays.dynamicarray;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DynamicArrayList
 *
 * Deep dive into ArrayList<T> — Java's dynamic array.
 * Covers internal resizing mechanics, amortized O(1) append,
 * capacity pre-allocation, List interface programming, and
 * performance characteristics versus primitive arrays.
 *
 */
public class DynamicArrayList {

    // ─── INTERNAL MECHANICS ──────────────────────────────────────────────────

    /**
     * ArrayList internally holds:
     *   Object[] elementData;  // the backing array
     *   int size;              // number of elements actually stored
     *
     * Default initial capacity = 10.
     * When size == elementData.length and add() is called:
     *   newCapacity = oldCapacity + (oldCapacity >> 1)  => ~1.5x growth
     *   A new array is allocated, all elements are copied (O(n) one-time cost),
     *   then the new element is appended.
     *
     * Because doubling happens exponentially less often,
     * the amortized cost per append is O(1).
     *
     * Visual growth trace:
     *   capacity=10  -> 15 -> 22 -> 33 -> 49 -> 73 -> ...
     */
    public static void resizingMechanics() {
        System.out.println("=== ArrayList Resizing Mechanics ===");

        // Pre-allocated capacity — avoids multiple resizes when size is known
        List<Integer> preAllocated = new ArrayList<>(1000);
        System.out.println("Created with initial capacity 1000 — no resize until > 1000 adds");

        // Default capacity 10
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(i);
            // Internally: resize happens at 10, 15, 22, 33, 49 — 5 resizes for 50 adds
        }
        System.out.println("Added 50 elements (resized ~5 times): size=" + list.size());

        // trimToSize — releases unused capacity (useful when done building a list)
        ((ArrayList<Integer>) list).trimToSize();
        System.out.println("After trimToSize — capacity now == size");
    }

    // ─── CORE OPERATIONS AND COMPLEXITY ──────────────────────────────────────

    /**
     * Operation complexities:
     *   add(E e)        — O(1) amortized   (append to end)
     *   add(int i, E e) — O(n)             (shift elements right from i)
     *   get(int i)      — O(1)             (direct index into backing array)
     *   set(int i, E e) — O(1)             (direct index assignment)
     *   remove(int i)   — O(n)             (shift elements left after i)
     *   remove(Object o)— O(n)             (linear scan + shift)
     *   contains(Object)— O(n)             (linear scan)
     *   size()          — O(1)
     */
    public static void coreOperations() {
        System.out.println("\n=== Core ArrayList Operations ===");

        List<String> fruits = new ArrayList<>();

        // O(1) amortized — append
        fruits.add("apple");
        fruits.add("banana");
        fruits.add("cherry");
        fruits.add("date");
        System.out.println("After adds: " + fruits);

        // O(n) — insert at index 1 (shifts banana, cherry, date right)
        fruits.add(1, "avocado");
        System.out.println("add(1,'avocado'): " + fruits);

        // O(1) — random access
        System.out.println("get(0): " + fruits.get(0));
        System.out.println("get(2): " + fruits.get(2));

        // O(1) — update at index
        fruits.set(0, "APPLE");
        System.out.println("set(0,'APPLE'): " + fruits);

        // O(n) — remove by index (shifts everything after it left)
        fruits.remove(1);
        System.out.println("remove(1): " + fruits);

        // O(n) — remove by value (linear scan + shift)
        fruits.remove("cherry");
        System.out.println("remove('cherry'): " + fruits);

        // O(1) — size check
        System.out.println("size(): " + fruits.size());

        // O(n) — contains
        System.out.println("contains('date'): " + fruits.contains("date"));
    }

    // ─── PROGRAMMING TO THE INTERFACE ────────────────────────────────────────

    /**
     * Declare variables as List<T>, not ArrayList<T>.
     * This decouples code from the concrete implementation —
     * you can swap ArrayList for LinkedList, CopyOnWriteArrayList, etc.
     * without changing any consuming code.
     */
    public static void interfaceBasedProgramming() {
        System.out.println("\n=== Interface-Based Programming ===");

        // Prefer: List<Integer> list = new ArrayList<>();
        // Avoid : ArrayList<Integer> list = new ArrayList<>();

        List<Integer> numbers = new ArrayList<>(List.of(5, 3, 8, 1, 9, 2));
        Collections.sort(numbers);
        System.out.println("Sorted via Collections.sort: " + numbers);

        // Unmodifiable view — any mutation throws UnsupportedOperationException
        List<Integer> immutable = Collections.unmodifiableList(numbers);
        try {
            immutable.add(99);
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list (expected)");
        }

        // Java 9+ — List.of produces an immutable list directly
        List<String> fixed = List.of("x", "y", "z");
        System.out.println("List.of: " + fixed);
    }

    // ─── USEFUL OPERATIONS ───────────────────────────────────────────────────

    /**
     * addAll, removeAll, retainAll, subList, indexOf, lastIndexOf.
     */
    public static void bulkOperations() {
        System.out.println("\n=== Bulk / Query Operations ===");

        List<Integer> a = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        List<Integer> b = new ArrayList<>(Arrays.asList(4, 5, 6, 7));

        // addAll — appends all elements of b to a
        List<Integer> union = new ArrayList<>(a);
        union.addAll(b);
        System.out.println("addAll   : " + union);

        // removeAll — removes all elements that appear in b
        List<Integer> diff = new ArrayList<>(a);
        diff.removeAll(b);
        System.out.println("removeAll: " + diff);

        // retainAll — keeps only elements that appear in b (intersection)
        List<Integer> inter = new ArrayList<>(a);
        inter.retainAll(b);
        System.out.println("retainAll: " + inter);

        // subList — returns a VIEW (changes reflect back into original list)
        List<Integer> base = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));
        List<Integer> view = base.subList(1, 4); // [20, 30, 40]
        System.out.println("subList(1,4): " + view);
        view.set(0, 999);
        System.out.println("base after subList.set(0,999): " + base); // base[1] changed

        // indexOf / lastIndexOf
        List<Integer> dup = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 1));
        System.out.println("indexOf(2)    : " + dup.indexOf(2));      // 1
        System.out.println("lastIndexOf(2): " + dup.lastIndexOf(2));  // 3
    }

    // ─── SORTING AND SEARCHING ────────────────────────────────────────────────

    public static void sortingAndSearching() {
        System.out.println("\n=== Sorting and Searching on Lists ===");

        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 3, 8, 1, 9, 2));

        // Natural order
        Collections.sort(nums);
        System.out.println("Sorted asc  : " + nums);

        // Reverse order
        nums.sort(Comparator.reverseOrder());
        System.out.println("Sorted desc : " + nums);

        // Custom: sort Person objects by name length
        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Bob", "Alice", "Dave"));
        names.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        System.out.println("By length   : " + names);

        // Binary search on a sorted list — O(log n)
        List<Integer> sorted = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
        int pos = Collections.binarySearch(sorted, 7);
        System.out.println("binarySearch(7): index " + pos);
    }

    // ─── CONVERSION UTILITIES ─────────────────────────────────────────────────

    public static void conversions() {
        System.out.println("\n=== Conversions ===");

        // int[] -> List<Integer> (requires stream boxing)
        int[] primitive = {1, 2, 3, 4, 5};
        List<Integer> fromPrimitive = Arrays.stream(primitive).boxed().collect(Collectors.toList());
        System.out.println("int[] -> List<Integer>: " + fromPrimitive);

        // List<Integer> -> int[]
        int[] backToPrimitive = fromPrimitive.stream().mapToInt(Integer::intValue).toArray();
        System.out.println("List<Integer> -> int[]: " + Arrays.toString(backToPrimitive));

        // String[] -> List<String>
        String[] strArr = {"a", "b", "c"};
        List<String> strList = new ArrayList<>(Arrays.asList(strArr));
        System.out.println("String[] -> List<String>: " + strList);

        // List<String> -> String[]
        String[] backToArr = strList.toArray(new String[0]);
        System.out.println("List<String> -> String[]: " + Arrays.toString(backToArr));
    }

    // ─── PERFORMANCE COMPARISON ───────────────────────────────────────────────

    /**
     * Measures append time for ArrayList vs pre-allocated ArrayList.
     * Demonstrates the cost of resizing when capacity is not reserved.
     */
    public static void performanceComparison() {
        System.out.println("\n=== Performance: default vs pre-allocated capacity ===");
        int N = 1_000_000;

        long start = System.nanoTime();
        List<Integer> dynamic = new ArrayList<>();
        for (int i = 0; i < N; i++) dynamic.add(i);
        long dynamicMs = (System.nanoTime() - start) / 1_000_000;

        start = System.nanoTime();
        List<Integer> preAlloc = new ArrayList<>(N);
        for (int i = 0; i < N; i++) preAlloc.add(i);
        long preAllocMs = (System.nanoTime() - start) / 1_000_000;

        System.out.printf("Default capacity  (%d elements): %d ms%n", N, dynamicMs);
        System.out.printf("Pre-allocated (%d elements): %d ms%n", N, preAllocMs);
        System.out.println("Pre-allocation avoids repeated array copy during resize.");
    }

    public static void main(String[] args) {
        resizingMechanics();
        coreOperations();
        interfaceBasedProgramming();
        bulkOperations();
        sortingAndSearching();
        conversions();
        performanceComparison();
    }
}

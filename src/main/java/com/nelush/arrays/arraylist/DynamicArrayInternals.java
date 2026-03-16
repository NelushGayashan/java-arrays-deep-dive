package com.nelush.arrays.arraylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 * SECTION 3: ArrayList — Dynamic Array Internals
 * ============================================================
 *
 * A primitive array's size is fixed at creation. ArrayList solves this
 * by wrapping a primitive Object[] array and replacing it with a larger
 * one whenever it runs out of space. This replacement — called resizing —
 * is what makes ArrayList "dynamic."
 *
 * INTERNAL STRUCTURE
 * ArrayList has exactly three instance fields:
 *   Object[]  elementData;  // the backing array
 *   int       size;         // number of valid elements (≤ elementData.length)
 *   int       modCount;     // structural modification count (for fail-fast iterators)
 *
 * The capacity is elementData.length; size is how many elements you have
 * added. They are different. Callers only see size via .size().
 *
 * GROWTH STRATEGY (Java 17 source)
 * When add() is called and size == capacity, ArrayList calls grow():
 *
 *   newCapacity = oldCapacity + (oldCapacity >> 1)
 *                ≈ oldCapacity × 1.5
 *
 * It then allocates a new Object[], copies all elements using
 * Arrays.copyOf (which calls System.arraycopy internally), and
 * replaces elementData.
 *
 * Earlier Java versions used 2× growth. The 1.5× strategy wastes
 * less memory while still giving amortized O(1) append.
 *
 * AMORTIZED O(1) APPEND — Why It Works
 * Suppose capacity starts at 10 and we add n elements.
 * Resizes happen at sizes: 10, 15, 22, 33, 49, …
 * Each resize copies all existing elements. Total copy work =
 *   10 + 15 + 22 + ... ≈ 2n  (geometric series)
 * Spread over n appends: 2n / n = O(1) per append on average.
 * One individual append may be O(n), but that is so rare that the
 * average stays constant. This is the definition of amortized O(1).
 *
 * PRE-SIZING
 * If you know approximate size upfront, pass it to the constructor:
 *   new ArrayList<>(expectedSize)
 * This prevents all resizes, saving both CPU (no copies) and memory
 * (no over-allocation). For tight loops adding millions of elements,
 * pre-sizing can cut time meaningfully.
 *
 * TRIMMING
 * trimToSize() shrinks the backing array to exactly size elements.
 * Use this when an ArrayList is built once and then only read, to
 * reclaim wasted capacity.
 *
 * WHY ArrayList.get() IS O(1)
 * get(i) is literally: return (E) elementData[i];
 * One array access — the same O(1) address formula as a raw array.
 *
 * WHY ArrayList.add(int index, E e) IS O(n)
 * add(index, value) calls System.arraycopy to shift elements right,
 * then writes the new element. Shifting takes O(n - index) steps.
 * Worst case is index=0: all n elements shift.
 *
 * ArrayList vs LinkedList (summary — full LinkedList article later)
 *   ArrayList  get(i)       O(1)   LinkedList  O(n)
 *   ArrayList  add(end)     O(1)*  LinkedList  O(1)
 *   ArrayList  add(front)   O(n)   LinkedList  O(1)
 *   ArrayList  iteration    FAST   LinkedList  SLOW (cache misses)
 *   In practice ArrayList wins almost every benchmark due to cache effects.
 *
 * MANUAL DYNAMIC ARRAY — written from scratch to make internals visible
 */
public class DynamicArrayInternals {

    /**
     * A minimal generic dynamic array that mirrors ArrayList's
     * core mechanics. Reading this clarifies what ArrayList does
     * under the hood without the noise of a production implementation.
     */
    @SuppressWarnings("unchecked")
    static class DynamicArray<T> {
        private Object[] data;
        private int size;
        private static final int DEFAULT_CAPACITY = 4; // small to trigger resizes quickly

        DynamicArray() {
            data = new Object[DEFAULT_CAPACITY];
        }

        DynamicArray(int initialCapacity) {
            data = new Object[initialCapacity];
        }

        /** Appends element to the end. Amortized O(1). */
        public void add(T value) {
            ensureCapacity();
            data[size++] = value;
        }

        /** Inserts element at index, shifting subsequent elements right. O(n). */
        public void add(int index, T value) {
            checkIndexBounds(index, size); // index 0..size inclusive
            ensureCapacity();
            // Shift elements [index, size) right by one
            System.arraycopy(data, index, data, index + 1, size - index);
            data[index] = value;
            size++;
        }

        /** Returns element at index. O(1). */
        public T get(int index) {
            checkIndexBounds(index, size - 1);
            return (T) data[index];
        }

        /** Replaces element at index. O(1). */
        public T set(int index, T value) {
            checkIndexBounds(index, size - 1);
            T old = (T) data[index];
            data[index] = value;
            return old;
        }

        /** Removes element at index, shifting subsequent elements left. O(n). */
        public T remove(int index) {
            checkIndexBounds(index, size - 1);
            T removed = (T) data[index];
            System.arraycopy(data, index + 1, data, index, size - index - 1);
            data[--size] = null; // clear last slot so GC can collect the object
            return removed;
        }

        public int size()    { return size; }
        public boolean isEmpty() { return size == 0; }

        /** Current backing array capacity (not exposed by ArrayList). */
        public int capacity() { return data.length; }

        /**
         * Trims the backing array to exactly size elements.
         * Equivalent to ArrayList.trimToSize().
         */
        public void trimToSize() {
            if (size < data.length) {
                data = Arrays.copyOf(data, size);
            }
        }

        // -------------------------------------------------------
        // Resize logic — the heart of the dynamic array
        // -------------------------------------------------------

        private void ensureCapacity() {
            if (size == data.length) {
                int newCapacity = data.length + (data.length >> 1); // ×1.5
                System.out.printf("  [resize] %d → %d%n", data.length, newCapacity);
                data = Arrays.copyOf(data, newCapacity);
            }
        }

        private void checkIndexBounds(int index, int max) {
            if (index < 0 || index > max)
                throw new IndexOutOfBoundsException("index=" + index + " max=" + max);
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOf(data, size));
        }
    }

    // -------------------------------------------------------
    // ArrayList API — everything you need in interviews
    // -------------------------------------------------------

    public static void arrayListApiDemo() {
        // Always declare with interface type List<T> — good OO practice
        List<Integer> list = new ArrayList<>();

        // add(E) — amortized O(1)
        list.add(10);
        list.add(20);
        list.add(30);

        // add(int, E) — O(n) — elements shift right
        list.add(1, 99);    // [10, 99, 20, 30]

        // get(int) — O(1) — direct array access
        System.out.println("get(1) = " + list.get(1)); // 99

        // set(int, E) — O(1) — direct array write
        list.set(1, 88);    // [10, 88, 20, 30]

        // remove(int) — O(n) — shifts left after removal
        list.remove(1);     // removes index 1 → [10, 20, 30]

        // remove(Object) — O(n) linear scan + O(n) shift
        // NOTE: for Integer lists, remove(Integer.valueOf(20)) targets by value
        list.remove(Integer.valueOf(20)); // [10, 30]

        // contains — O(n) linear scan
        System.out.println("contains 30: " + list.contains(30)); // true

        // size vs capacity — only size is exposed
        System.out.println("size: " + list.size());

        // sort via Collections.sort or List.sort
        List<Integer> unsorted = new ArrayList<>(List.of(5, 3, 8, 1));
        Collections.sort(unsorted);                          // ascending
        unsorted.sort((a, b) -> b - a);                      // descending (Comparator)
        System.out.println("Sorted desc: " + unsorted);

        // subList — a VIEW, not a copy; modifications affect original
        List<Integer> sub = list.subList(0, list.size());
        System.out.println("subList: " + sub);

        // toArray — when you need the raw array back
        Integer[] arr = list.toArray(new Integer[0]);
        System.out.println("toArray: " + Arrays.toString(arr));

        // Pre-sizing to avoid resizes
        List<Integer> preSized = new ArrayList<>(10_000);
        // All 10_000 adds are done without a single resize
        for (int i = 0; i < 10_000; i++) preSized.add(i);
    }

    public static void main(String[] args) {
        System.out.println("=== Manual DynamicArray — resize trace ===");
        DynamicArray<Integer> da = new DynamicArray<>();
        System.out.println("Initial capacity: " + da.capacity()); // 4
        for (int i = 1; i <= 10; i++) {
            da.add(i);
            System.out.printf("  add(%2d)  size=%d  capacity=%d%n", i, da.size(), da.capacity());
        }
        System.out.println("Contents: " + da);

        da.trimToSize();
        System.out.println("After trimToSize capacity: " + da.capacity());

        System.out.println("\n=== ArrayList API Demo ===");
        arrayListApiDemo();
    }
}

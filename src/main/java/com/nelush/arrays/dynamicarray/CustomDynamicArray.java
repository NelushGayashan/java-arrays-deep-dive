package com.nelush.arrays.dynamicarray;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * CustomDynamicArray<T>
 *
 * Article section: "Dynamic Arrays — ArrayList Internals"
 *
 * The article shows this resize logic:
 *
 *   private void ensureCapacity() {
 *       if (size < data.length) return;
 *       int newCapacity = data.length + (data.length >> 1); // grow by 50%
 *       data = Arrays.copyOf(data, newCapacity);
 *   }
 *
 * This class wraps that into a fully working generic dynamic array.
 * Starting capacity is 4 (small) so resizes are visible immediately in output.
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.dynamicarray.CustomDynamicArray"
 *
 * Author: Nelush Gayashan Fernando
 */
@SuppressWarnings("unchecked")
public class CustomDynamicArray<T> implements Iterable<T> {

    private static final int DEFAULT_CAPACITY = 4; // small to show resizes quickly

    private Object[] data;
    private int size;
    private int resizeCount;

    public CustomDynamicArray() {
        data = new Object[DEFAULT_CAPACITY];
    }

    public CustomDynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        data = new Object[initialCapacity];
    }

    // ─── ADD (APPEND) — O(1) AMORTIZED ────────────────────────────────────────

    public void add(T e) {
        ensureCapacity();
        data[size++] = e;
    }

    // ─── ADD AT INDEX — O(n) ──────────────────────────────────────────────────

    public void add(int index, T e) {
        checkIndexForAdd(index);
        ensureCapacity();
        // Shift elements right to open a slot
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = e;
        size++;
    }

    // ─── GET / SET — O(1) ─────────────────────────────────────────────────────

    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    public T set(int index, T e) {
        checkIndex(index);
        T old = (T) data[index];
        data[index] = e;
        return old;
    }

    // ─── REMOVE — O(n) ────────────────────────────────────────────────────────

    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        int numToShift = size - index - 1;
        if (numToShift > 0) System.arraycopy(data, index + 1, data, index, numToShift);
        data[--size] = null; // allow GC to reclaim
        return removed;
    }

    // ─── QUERY ────────────────────────────────────────────────────────────────

    public int size()           { return size; }
    public boolean isEmpty()    { return size == 0; }
    public int capacity()       { return data.length; }
    public int getResizeCount() { return resizeCount; }

    public boolean contains(T e) { return indexOf(e) >= 0; }

    public int indexOf(T e) {
        for (int i = 0; i < size; i++) {
            if (e == null ? data[i] == null : e.equals(data[i])) return i;
        }
        return -1;
    }

    // ─── RESIZE — THE CORE MECHANISM FROM THE ARTICLE ─────────────────────────

    /**
     * Exact resize logic from the article:
     *
     *   private void ensureCapacity() {
     *       if (size < data.length) return; // still room — no resize needed
     *       int newCapacity = data.length + (data.length >> 1); // Grow by 50%
     *       data = Arrays.copyOf(data, newCapacity);
     *   }
     *
     * The 1.5x growth (data.length >> 1 = data.length / 2) is the same
     * factor used by OpenJDK's ArrayList.
     */
    private void ensureCapacity() {
        if (size < data.length) return; // still room — no resize needed

        // Grow by 50%: same factor as OpenJDK's ArrayList
        int newCapacity = data.length + (data.length >> 1);
        System.out.printf("  [resize] capacity %d → %d  (resize #%d)%n",
            data.length, newCapacity, ++resizeCount);
        data = Arrays.copyOf(data, newCapacity);
    }

    // ─── BOUNDS CHECKING ──────────────────────────────────────────────────────

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index " + index + ", size " + size);
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index " + index + " for add, size " + size);
    }

    // ─── ITERABLE ─────────────────────────────────────────────────────────────

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (T) data[cursor++];
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]  (size=").append(size)
                 .append(", capacity=").append(data.length).append(")").toString();
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== CustomDynamicArray — from-scratch resize demo ===");
        System.out.println("Initial capacity: " + DEFAULT_CAPACITY);
        System.out.println();

        CustomDynamicArray<Integer> arr = new CustomDynamicArray<>();

        // Add 15 elements — watch the resize events
        for (int i = 1; i <= 15; i++) {
            arr.add(i);
        }

        System.out.println("\nFinal state   : " + arr);
        System.out.println("Total resizes : " + arr.getResizeCount());
        System.out.println("get(0)        = " + arr.get(0));
        System.out.println("get(14)       = " + arr.get(14));
        System.out.println("contains(7)   = " + arr.contains(7));

        System.out.println("\nInsert 999 at index 0 (O(n) shift):");
        arr.add(0, 999);
        System.out.println("After add(0, 999) : " + arr);

        System.out.println("\nRemove index 0 (O(n) shift):");
        arr.remove(0);
        System.out.println("After remove(0)   : " + arr);

        System.out.print("\nEnhanced-for iteration: ");
        for (int v : arr) System.out.print(v + " ");
        System.out.println();
    }
}

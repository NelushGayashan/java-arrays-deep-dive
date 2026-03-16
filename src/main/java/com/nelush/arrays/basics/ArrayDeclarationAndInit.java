package com.nelush.arrays.basics;

import java.util.Arrays;

/**
 * ArrayDeclarationAndInit
 *
 * All syntactic forms of declaring, initialising, and accessing arrays in Java.
 * Covers single-line literals, new-expression forms, anonymous arrays,
 * enhanced-for, and index arithmetic.
 *
 */
public class ArrayDeclarationAndInit {

    // ─── DECLARATION STYLES ────────────────────────────────────────────────────

    /**
     * Three canonical forms of declaration + initialisation:
     *
     *   (a) int[] arr = {1,2,3};              // array literal — most concise
     *   (b) int[] arr = new int[]{1,2,3};     // explicit new, also a literal
     *   (c) int[] arr = new int[n];           // fixed-size, zero-filled
     *
     * Style (c) is the only option when size is determined at runtime.
     */
    public static void declarationForms() {
        // (a) Shorthand literal — type inferred from the left-hand side.
        int[] literal = {10, 20, 30, 40, 50};

        // (b) Full new-expression literal — useful when passing an array as an argument:
        //     someMethod(new int[]{1, 2, 3});
        int[] fullLiteral = new int[]{10, 20, 30, 40, 50};

        // (c) Size-only declaration — all slots initialised to 0.
        int n = 5;
        int[] sizedArray = new int[n];  // n can be a variable

        System.out.println("=== Declaration Forms ===");
        System.out.println("literal      : " + Arrays.toString(literal));
        System.out.println("fullLiteral  : " + Arrays.toString(fullLiteral));
        System.out.println("sizedArray   : " + Arrays.toString(sizedArray)); // [0,0,0,0,0]

        // IMPORTANT: int arr[]; is legal Java but avoid — C-style, misleading.
        // The type modifier [] belongs with the type, not the variable name.
    }

    // ─── READING AND WRITING ELEMENTS ─────────────────────────────────────────

    /**
     * Index access is O(1).  Valid indices: 0 to length-1.
     * Accessing index -1 or >= length throws ArrayIndexOutOfBoundsException.
     */
    public static void readAndWrite() {
        int[] scores = {90, 75, 88, 62, 95};

        System.out.println("\n=== Read / Write ===");

        // Read — direct index
        System.out.println("scores[0] = " + scores[0]);
        System.out.println("scores[4] = " + scores[4]); // last element

        // Write — direct assignment
        scores[2] = 100;
        System.out.println("After scores[2]=100 : " + Arrays.toString(scores));

        // Common idiom: last element
        int last = scores[scores.length - 1];
        System.out.println("Last element : " + last);

        // Guarded access — always check bounds before dynamic indexing
        int index = 10;
        if (index >= 0 && index < scores.length) {
            System.out.println("scores[" + index + "] = " + scores[index]);
        } else {
            System.out.println("Index " + index + " out of bounds (length=" + scores.length + ")");
        }

        // ArrayIndexOutOfBoundsException demo
        try {
            int bad = scores[99];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e);
        }
    }

    // ─── TRAVERSAL PATTERNS ────────────────────────────────────────────────────

    /**
     * Four traversal forms — each suited to different contexts.
     */
    public static void traversalPatterns() {
        int[] data = {3, 1, 4, 1, 5, 9, 2, 6};
        System.out.println("\n=== Traversal Patterns ===");

        // (1) Classic for loop — use when index matters (searching, two-pointer, etc.)
        System.out.print("Classic for   : ");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();

        // (2) Enhanced for — use when only values matter (no index access needed)
        System.out.print("Enhanced for  : ");
        for (int val : data) {
            System.out.print(val + " ");
        }
        System.out.println();

        // (3) Reverse traversal — iterate from end to start
        System.out.print("Reverse       : ");
        for (int i = data.length - 1; i >= 0; i--) {
            System.out.print(data[i] + " ");
        }
        System.out.println();

        // (4) Step-by-step (e.g., process every second element)
        System.out.print("Every 2nd     : ");
        for (int i = 0; i < data.length; i += 2) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }

    // ─── PASSING ARRAYS TO METHODS ────────────────────────────────────────────

    /**
     * Arrays are objects.  Passing an array to a method passes a reference.
     * The method can mutate the original array's contents.
     * Reassigning the parameter inside the method does NOT affect the caller.
     */
    public static void modifyInMethod(int[] arr) {
        arr[0] = 999; // mutates the original — visible to caller
    }

    public static void reassignParameter(int[] arr) {
        arr = new int[]{1, 2, 3}; // local rebind — caller is NOT affected
    }

    public static void arrayPassingDemo() {
        int[] original = {10, 20, 30};
        System.out.println("\n=== Array Passing Semantics ===");
        System.out.println("Before modifyInMethod : " + Arrays.toString(original));

        modifyInMethod(original);
        System.out.println("After  modifyInMethod : " + Arrays.toString(original)); // [999,20,30]

        reassignParameter(original);
        System.out.println("After  reassignParameter : " + Arrays.toString(original)); // still [999,20,30]
    }

    // ─── ANONYMOUS ARRAY AS METHOD ARGUMENT ───────────────────────────────────

    /**
     * An anonymous array (new int[]{...}) allows passing an inline array
     * without declaring a variable.  Common when calling utility methods.
     */
    public static int sum(int[] arr) {
        int total = 0;
        for (int v : arr) total += v;
        return total;
    }

    public static void anonymousArrayDemo() {
        System.out.println("\n=== Anonymous Array ===");
        // No named variable needed:
        System.out.println("sum = " + sum(new int[]{1, 2, 3, 4, 5}));
    }

    public static void main(String[] args) {
        declarationForms();
        readAndWrite();
        traversalPatterns();
        arrayPassingDemo();
        anonymousArrayDemo();
    }
}

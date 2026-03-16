package com.nelush.arrays.advanced;

import java.util.Arrays;

/**
 * MatrixOperations
 *
 * Article section: "Matrix Operations on 2D Arrays"
 *
 * From the article:
 *   - Rotate 90° clockwise in-place: transpose + reverse each row
 *   - Spiral order traversal: four shrinking boundaries
 *   - Set matrix zeroes: use first row/column as O(1) space markers
 *
 * Original matrix from the article's rotation example:
 *   [1, 2, 3]      Transpose:   [1, 4, 7]      Reverse rows:  [7, 4, 1]
 *   [4, 5, 6]   →              [2, 5, 8]     →               [8, 5, 2]
 *   [7, 8, 9]                  [3, 6, 9]                      [9, 6, 3]
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.nelush.arrays.advanced.MatrixOperations"
 *
 * Author: Nelush Gayashan Fernando
 */
public class MatrixOperations {

    private static void print(int[][] m, String label) {
        System.out.println(label + ":");
        for (int[] row : m) System.out.println("  " + Arrays.toString(row));
    }

    // ─── TRANSPOSE ────────────────────────────────────────────────────────────

    public static void transpose(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                int tmp = m[i][j]; m[i][j] = m[j][i]; m[j][i] = tmp;
            }
    }

    // ─── ROTATE 90° CLOCKWISE ─────────────────────────────────────────────────

    /**
     * From the article:
     * "Rotate 90° clockwise in-place decomposes into two simpler operations:
     * transpose (swap matrix[i][j] with matrix[j][i] for all i < j),
     * then reverse each row."
     */
    public static void rotate90Clockwise(int[][] m) {
        transpose(m);
        for (int[] row : m) {
            int l = 0, r = row.length - 1;
            while (l < r) { int t = row[l]; row[l++] = row[r]; row[r--] = t; }
        }
    }

    public static void rotate90CounterClockwise(int[][] m) {
        for (int[] row : m) { // reverse each row first
            int l = 0, r = row.length - 1;
            while (l < r) { int t = row[l]; row[l++] = row[r]; row[r--] = t; }
        }
        transpose(m);
    }

    // ─── SPIRAL ORDER TRAVERSAL ───────────────────────────────────────────────

    /**
     * From the article:
     * "maintains four boundary variables (top, bottom, left, right) and traverses
     * each layer: right across the top, down along the right, left across the bottom,
     * up along the left. After each direction, shrink the corresponding boundary."
     */
    public static int[] spiralOrder(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[] result = new int[m * n];
        int idx = 0;
        int top = 0, bottom = m - 1, left = 0, right = n - 1;

        while (top <= bottom && left <= right) {
            for (int c = left;  c <= right;  c++) result[idx++] = matrix[top][c];
            top++;
            for (int r = top;   r <= bottom; r++) result[idx++] = matrix[r][right];
            right--;
            if (top <= bottom)
                for (int c = right; c >= left;   c--) result[idx++] = matrix[bottom][c];
            bottom--;
            if (left <= right)
                for (int r = bottom; r >= top;   r--) result[idx++] = matrix[r][left];
            left++;
        }
        return result;
    }

    // ─── SET MATRIX ZEROES ────────────────────────────────────────────────────

    /**
     * From the article:
     * "Use the first row and column as markers: scan the matrix and record which rows
     * and columns need zeroing there, then apply the zeros, handling the first row and
     * column separately since they are the markers."
     */
    public static void setZeroes(int[][] m) {
        int rows = m.length, cols = m[0].length;
        boolean firstRowZero = false, firstColZero = false;

        for (int j = 0; j < cols; j++) if (m[0][j] == 0) { firstRowZero = true; break; }
        for (int i = 0; i < rows; i++) if (m[i][0] == 0) { firstColZero = true; break; }

        // Mark zeroes using first row and column
        for (int i = 1; i < rows; i++)
            for (int j = 1; j < cols; j++)
                if (m[i][j] == 0) { m[i][0] = 0; m[0][j] = 0; }

        // Zero out cells based on markers
        for (int i = 1; i < rows; i++)
            for (int j = 1; j < cols; j++)
                if (m[i][0] == 0 || m[0][j] == 0) m[i][j] = 0;

        if (firstRowZero) Arrays.fill(m[0], 0);
        if (firstColZero) for (int i = 0; i < rows; i++) m[i][0] = 0;
    }

    // ─── MATRIX MULTIPLICATION ────────────────────────────────────────────────

    public static int[][] multiply(int[][] A, int[][] B) {
        int m = A.length, k = A[0].length, n = B[0].length;
        int[][] C = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                for (int p = 0; p < k; p++)
                    C[i][j] += A[i][p] * B[p][j];
        return C;
    }

    // ─── DEMO ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Matrix Operations ===\n");

        int[][] original = {{1,2,3},{4,5,6},{7,8,9}};
        print(original, "Original");

        // Rotate 90° clockwise — article example
        int[][] rotated = Arrays.stream(original).map(int[]::clone).toArray(int[][]::new);
        rotate90Clockwise(rotated);
        print(rotated, "\nRotate 90° Clockwise");
        // Article shows: [7,4,1], [8,5,2], [9,6,3]

        // Spiral
        System.out.println("\nSpiral order of original: "
            + Arrays.toString(spiralOrder(original)));

        // Set zeroes
        int[][] mz = {{1,1,1},{1,0,1},{1,1,1}};
        setZeroes(mz);
        print(mz, "\nSet Zeroes [[1,1,1],[1,0,1],[1,1,1]]");

        // Multiply
        int[][] A = {{1,2},{3,4}};
        int[][] B = {{5,6},{7,8}};
        print(multiply(A, B), "\nA x B (matrix multiply)");
    }
}

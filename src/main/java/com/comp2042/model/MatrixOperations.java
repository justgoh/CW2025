package com.comp2042.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides utility methods for matrix operations in Tetris gameplay.
 * <p>
 * This utility class contains static methods for common matrix operations such as
 * collision detection, merging pieces with the board, clearing completed rows,
 * and copying matrices. All methods are stateless and thread-safe.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Detects collisions between pieces and the game board</li>
 *   <li>Merges piece matrices with the board matrix</li>
 *   <li>Identifies and removes completed rows</li>
 *   <li>Creates deep copies of matrices for immutability</li>
 * </ul>
 */
public class MatrixOperations {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MatrixOperations() {
    }

    /**
     * Checks if a brick matrix intersects with the board or goes out of bounds.
     * <p>
     * Collision occurs if any non-zero cell in the brick overlaps with a non-zero
     * cell on the board or extends beyond the board boundaries.
     *
     * @param boardMatrix  the game board matrix
     * @param brickmatrix  the brick's shape matrix
     * @param columnoffset the brick's horizontal position on the board
     * @param rowoffset    the brick's vertical position on the board
     * @return true if the brick collides with the board or boundaries, false otherwise
     */
    public static boolean intersect(final int[][] boardMatrix, final int[][] brickmatrix, int columnoffset, int rowoffset) {
        for (int row = 0; row < brickmatrix.length; row++) {
            for (int column = 0; column < brickmatrix[row].length; column++) {
                if (brickmatrix[row][column] == 0) {
                    continue;
                }
                int boardcolumn = columnoffset + column;
                int boardrow = rowoffset + row;
                if ((isOutOfBound(boardMatrix, boardcolumn, boardrow) || boardMatrix[boardrow][boardcolumn] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a position is outside the board boundaries.
     *
     * @param boardMatrix the game board matrix
     * @param column      the column index to check
     * @param row         the row index to check
     * @return true if the position is out of bounds, false otherwise
     */
    private static boolean isOutOfBound(int[][] boardMatrix, int column, int row) {
        return row < 0 || row >= boardMatrix.length || column < 0 || column >= boardMatrix[0].length;
    }

    /**
     * Creates a deep copy of a 2D matrix.
     * <p>
     * The returned matrix is completely independent of the original, allowing
     * modifications without affecting the source.
     *
     * @param originalmatrix the matrix to copy
     * @return a new matrix with the same dimensions and values as the original
     */
    public static int[][] copy(int[][] originalmatrix) {
        int[][] copiedmatrix = new int[originalmatrix.length][];
        for (int row = 0; row < originalmatrix.length; row++) {
            int[] sourcerow = originalmatrix[row];
            copiedmatrix[row] = new int[sourcerow.length];
            System.arraycopy(sourcerow, 0, copiedmatrix[row], 0, sourcerow.length);
        }
        return copiedmatrix;
    }

    /**
     * Merges a brick matrix into the board matrix at the specified position.
     * <p>
     * Non-zero values from the brick overwrite corresponding positions on the board.
     * The original board matrix remains unchanged.
     *
     * @param boardMatrix  the game board matrix
     * @param brickmatrix  the brick's shape matrix to merge
     * @param columnoffset the brick's horizontal position on the board
     * @param rowoffset    the brick's vertical position on the board
     * @return a new matrix with the brick merged into the board
     */
    public static int[][] merge(int[][] boardMatrix, int[][] brickmatrix, int columnoffset, int rowoffset) {
        int[][] mergedmatrix = copy(boardMatrix);
        for (int row = 0; row < brickmatrix.length; row++) {
            for (int column = 0; column < brickmatrix[row].length; column++) {
                int boardcolumn = columnoffset + column;
                int boardrow = rowoffset + row;
                if (brickmatrix[row][column] != 0) {
                    mergedmatrix[boardrow][boardcolumn] = brickmatrix[row][column];
                }
            }
        }
        return mergedmatrix;
    }

    /**
     * Checks for and removes completed rows from the board matrix.
     * <p>
     * Completed rows (all non-zero values) are removed, and remaining rows
     * are shifted down. The score bonus is calculated as 50 × (lines cleared)².
     *
     * @param boardMatrix the game board matrix to check
     * @return a ClearRow object containing the number of lines removed,
     * the updated board matrix, and the score bonus earned
     */
    public static ClearRow checkRemoving(final int[][] boardMatrix) {
        int[][] updatedmatrix = new int[boardMatrix.length][boardMatrix[0].length];
        Deque<int[]> remainingrows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int row = 0; row < boardMatrix.length; row++) {
            int[] tmpRow = new int[boardMatrix[row].length];
            boolean isrowfull = true;
            for (int column = 0; column < boardMatrix[0].length; column++) {
                if (boardMatrix[row][column] == 0) {
                    isrowfull = false;
                }
                tmpRow[column] = boardMatrix[row][column];
            }
            if (isrowfull) {
                clearedRows.add(row);
            } else {
                remainingrows.add(tmpRow);
            }
        }
        for (int i = boardMatrix.length - 1; i >= 0; i--) {
            int[] nextrow = remainingrows.pollLast();
            if (nextrow != null) {
                updatedmatrix[i] = nextrow;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), updatedmatrix, scoreBonus);
    }

    /**
     * Creates a deep copy of a list of 2D matrices.
     * <p>
     * Each matrix in the list is independently copied, ensuring complete isolation
     * from the original list and its contents.
     *
     * @param list the list of matrices to copy
     * @return a new list containing deep copies of all matrices
     */
    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}

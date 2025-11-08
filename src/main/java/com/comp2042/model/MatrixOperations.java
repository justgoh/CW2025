package com.comp2042.model;

<<<<<<< HEAD
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
=======
import java.util.*;
>>>>>>> 2maintanence
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
<<<<<<< HEAD
    private MatrixOperations(){

    }

    public static boolean intersect(final int[][] boardMatrix, final int[][] brickmatrix, int columnoffset, int rowoffset) {
        for (int row = 0; row < brickmatrix.length; row++) {
            for (int column = 0; column < brickmatrix[row].length; column++) {
                int boardcolumn = columnoffset + column;
                int boardrow = rowoffset + row;
                if (brickmatrix[column][row] != 0 && (isOutOfBound(boardMatrix, boardcolumn, boardrow) || boardMatrix[boardrow][boardcolumn] != 0)) {
=======
    private MatrixOperations() {

    }

    public static boolean intersect(final int[][] boardMatrix, final int[][] brickMatrix, int columnoffset, int rowoffset) {
        for (int row = 0; row < brickMatrix.length; row++) {
            for (int column = 0; column < brickMatrix[row].length; column++) {
                int boardColumn = columnoffset + column;
                int boardRow = rowoffset + row;
                if (brickMatrix[row][column] != 0 && (isOutOfBound(boardMatrix, boardColumn, boardRow) || boardMatrix[boardRow][boardColumn] != 0)) {
>>>>>>> 2maintanence
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isOutOfBound(int[][] boardMatrix, int column, int row) {
<<<<<<< HEAD
        boolean outofbound = true;
        if (column >= 0 && row < boardMatrix.length && column < boardMatrix[row].length) {
            outofbound = false;
        }
        return outofbound;
    }

    public static int[][] copy(int[][] originalmatrix) {
        int[][] copiedmatrix = new int[originalmatrix.length][];
        for (int row = 0; row < originalmatrix.length; row++) {
            int[] sourcerow = originalmatrix[row];
            copiedmatrix[row] = new int[sourcerow.length];
            System.arraycopy(sourcerow, 0, copiedmatrix[row], 0, sourcerow.length);
        }
        return copiedmatrix;
    }

    public static int[][] merge(int[][] boardMatrix, int[][] brickmatrix, int columnoffset, int rowoffset) {
        int[][] mergedmatrix = copy(boardMatrix);
        for (int row = 0; row < brickmatrix.length; row++) {
            for (int column = 0; column < brickmatrix[row].length; column++) {
                int boardcolumn = columnoffset + column;
                int boardrow = rowoffset + row;
                if (brickmatrix[column][row] != 0) {
                    mergedmatrix[boardrow][boardcolumn] = brickmatrix[column][row];
                }
            }
        }
        return mergedmatrix;
    }

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
=======
        return (row < 0 || row >= boardMatrix.length || column < 0 || column >= boardMatrix[row].length);
    }

    public static int[][] copy(int[][] originalMatrix) {
        int[][] copiedMatrix = new int[originalMatrix.length][];
        for (int row = 0; row < originalMatrix.length; row++) {
            copiedMatrix[row] = Arrays.copyOf(originalMatrix[row], originalMatrix[row].length);
        }
        return copiedMatrix;
    }

    public static int[][] merge(int[][] boardMatrix, int[][] brickMatrix, int columnOffset, int rowOffset) {
        int[][] mergedMatrix = copy(boardMatrix);
        for (int row = 0; row < brickMatrix.length; row++) {
            for (int column = 0; column < brickMatrix[row].length; column++) {
                int boardColumn = columnOffset + column;
                int boardRow = rowOffset + row;
                if (brickMatrix[row][column] != 0) {
                    mergedMatrix[boardRow][boardColumn] = brickMatrix[row][column];
                }
            }
        }
        return mergedMatrix;
    }

    public static ClearRow checkRemoving(final int[][] boardMatrix) {
        int[][] updatedMatrix = new int[boardMatrix.length][boardMatrix[0].length];
        Deque<int[]> remainingRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int row = 0; row < boardMatrix.length; row++) {
            boolean isRowFull = true;
            for (int column = 0; column < boardMatrix[0].length; column++) {
                if (boardMatrix[row][column] == 0) {
                    isRowFull = false;
                    break;
                }
            }
            if (isRowFull) {
                clearedRows.add(row);
            } else {
                remainingRows.add(Arrays.copyOf(boardMatrix[row], boardMatrix[row].length));
            }
        }
        for (int i = boardMatrix.length - 1; i >= 0; i--) {
            int[] nextRow = remainingRows.pollLast();
            if (nextRow != null) {
                updatedMatrix[i] = nextRow;
>>>>>>> 2maintanence
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
<<<<<<< HEAD
        return new ClearRow(clearedRows.size(), updatedmatrix, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
=======
        return new ClearRow(clearedRows.size(), updatedMatrix, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list) {
>>>>>>> 2maintanence
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}

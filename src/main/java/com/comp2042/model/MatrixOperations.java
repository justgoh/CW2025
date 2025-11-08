package com.comp2042.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations(){

    }

    public static boolean intersect(final int[][] boardMatrix, final int[][] brickmatrix, int columnoffset, int rowoffset) {
        for (int row = 0; row < brickmatrix.length; row++) {
            for (int column = 0; column < brickmatrix[row].length; column++) {
                int boardcolumn = columnoffset + column;
                int boardrow = rowoffset + row;
                if (brickmatrix[column][row] != 0 && (isOutOfBound(boardMatrix, boardcolumn, boardrow) || boardMatrix[boardrow][boardcolumn] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isOutOfBound(int[][] boardMatrix, int column, int row) {
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
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), updatedmatrix, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}

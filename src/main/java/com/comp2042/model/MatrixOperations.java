package com.comp2042.model;

import java.util.*;
import java.util.stream.Collectors;

public class MatrixOperations {


    //We don't want to instantiate this utility class
    private MatrixOperations() {

    }

    public static boolean intersect(final int[][] boardMatrix, final int[][] brickMatrix, int columnoffset, int rowoffset) {
        for (int row = 0; row < brickMatrix.length; row++) {
            for (int column = 0; column < brickMatrix[row].length; column++) {
                int boardColumn = columnoffset + column;
                int boardRow = rowoffset + row;
                if (brickMatrix[row][column] != 0 && (isOutOfBound(boardMatrix, boardColumn, boardRow) || boardMatrix[boardRow][boardColumn] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isOutOfBound(int[][] boardMatrix, int column, int row) {
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
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), updatedMatrix, scoreBonus);
    }

    public static List<int[][]> deepCopyList(List<int[][]> list) {
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}

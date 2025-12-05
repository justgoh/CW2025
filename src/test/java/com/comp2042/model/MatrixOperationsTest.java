package com.comp2042.model;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testIntersect_NoCollision_EmptyBoard() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        assertFalse(MatrixOperations.intersect(board, brick, 1, 1));
    }

    @Test
    void testIntersect_Collision_WithExistingBlocks() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 2, 2, 0},
                {0, 2, 2, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1},
                {1, 1}
        };

        assertTrue(MatrixOperations.intersect(board, brick, 1, 1));
    }

    @Test
    void testIntersect_OutOfBounds_Left() {
        int[][] board = new int[4][4];
        int[][] brick = {{1}};

        assertTrue(MatrixOperations.intersect(board, brick, -1, 0));
    }

    @Test
    void testIntersect_OutOfBounds_Right() {
        int[][] board = new int[4][4];
        int[][] brick = {{1}};

        assertTrue(MatrixOperations.intersect(board, brick, 4, 0));
    }

    @Test
    void testIntersect_OutOfBounds_Top() {
        int[][] board = new int[4][4];
        int[][] brick = {{1}};

        assertTrue(MatrixOperations.intersect(board, brick, 0, -1));
    }

    @Test
    void testIntersect_OutOfBounds_Bottom() {
        int[][] board = new int[4][4];
        int[][] brick = {{1}};

        assertTrue(MatrixOperations.intersect(board, brick, 0, 4));
    }

    @Test
    void testIntersect_PartialOverlap() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 3, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 1, 1},
                {0, 1, 0}
        };

        assertTrue(MatrixOperations.intersect(board, brick, 1, 0));
    }

    @Test
    void testIntersect_IgnoresZeroCellsInBrick() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {0, 1, 0},
                {1, 1, 1}
        };

        assertFalse(MatrixOperations.intersect(board, brick, 0, 1));
    }

    @Test
    void testMerge_BrickIntoEmptyBoard() {
        int[][] board = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        int[][] brick = {
                {1, 2},
                {3, 4}
        };

        int[][] expected = {
                {0, 0, 0, 0},
                {0, 1, 2, 0},
                {0, 3, 4, 0},
                {0, 0, 0, 0}
        };

        int[][] result = MatrixOperations.merge(board, brick, 1, 1);
        assertArrayEquals(expected, result);
    }

    @Test
    void testMerge_OriginalBoardNotModified() {
        int[][] board = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        int[][] brick = {{1}};
        int[][] originalBoardCopy = MatrixOperations.copy(board);

        MatrixOperations.merge(board, brick, 1, 1);

        assertArrayEquals(originalBoardCopy, board);
    }

    @Test
    void testMerge_IgnoresZerosInBrick() {
        int[][] board = {
                {5, 5, 5},
                {5, 5, 5},
                {5, 5, 5}
        };

        int[][] brick = {
                {0, 1},
                {2, 0}
        };

        int[][] expected = {
                {5, 5, 5},
                {5, 5, 1},
                {5, 2, 5}
        };

        int[][] result = MatrixOperations.merge(board, brick, 1, 1);
        assertArrayEquals(expected, result);
    }

    @Test
    void testCheckRemoving_NoFullRows() {
        int[][] board = {
                {0, 1, 0, 1},
                {1, 0, 1, 0},
                {0, 0, 0, 1},
                {1, 1, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());
        assertArrayEquals(board, result.getNewMatrix());
    }

    @Test
    void testCheckRemoving_OneFullRow() {
        int[][] board = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };

        int[][] expected = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(1, result.getLinesRemoved());
        assertEquals(50, result.getScoreBonus());
        assertArrayEquals(expected, result.getNewMatrix());
    }

    @Test
    void testCheckRemoving_TwoFullRows() {
        int[][] board = {
                {2, 2, 2, 2},
                {3, 3, 3, 3},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };

        int[][] expected = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 1, 0, 1},
                {1, 0, 1, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(2, result.getLinesRemoved());
        assertEquals(200, result.getScoreBonus());
        assertArrayEquals(expected, result.getNewMatrix());
    }

    @Test
    void testCheckRemoving_FourFullRows() {
        int[][] board = {
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        };

        int[][] expected = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        ClearRow result = MatrixOperations.checkRemoving(board);

        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus());
        assertArrayEquals(expected, result.getNewMatrix());
    }

    @Test
    void testCheckRemoving_RowsShiftDownCorrectly() {
        int[][] board = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 2, 0},
                {3, 0, 0, 3}
        };

        int[][] expected = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 2, 0},
                {3, 0, 0, 3}
        };

        ClearRow result = MatrixOperations.checkRemoving(board);
        assertArrayEquals(expected, result.getNewMatrix());
    }

    @Test
    void testCopy_CreatesDeepCopy() {
        int[][] original = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] copy = MatrixOperations.copy(original);

        assertArrayEquals(original, copy);

        copy[0][0] = 99;
        assertEquals(1, original[0][0]);
        assertEquals(99, copy[0][0]);
    }

    @Test
    void testCopy_EmptyMatrix() {
        int[][] original = new int[0][0];
        int[][] copy = MatrixOperations.copy(original);
        assertEquals(0, copy.length);
    }

    @Test
    void testCopy_RectangularMatrix() {
        int[][] original = {
                {1, 2},
                {3, 4},
                {5, 6}
        };

        int[][] copy = MatrixOperations.copy(original);
        assertArrayEquals(original, copy);
        assertEquals(3, copy.length);
        assertEquals(2, copy[0].length);
    }

    static Stream<Object[]> intersectScenarios() {
        return Stream.of(
                new Object[]{
                        new int[][]{{0, 0}, {0, 0}},
                        new int[][]{{1}},
                        0, 0, false
                },
                new Object[]{
                        new int[][]{{1, 0}, {0, 0}},
                        new int[][]{{1}},
                        0, 0, true
                },
                new Object[]{
                        new int[][]{{0, 0}, {0, 0}},
                        new int[][]{{1, 1}, {0, 1}},
                        0, 0, false
                }
        );
    }
}
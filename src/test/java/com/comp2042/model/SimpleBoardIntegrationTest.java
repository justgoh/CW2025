package com.comp2042.model;

import com.comp2042.view.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardIntegrationTest {

    private SimpleBoard board;
    private static final int ROWS = 10;
    private static final int COLUMNS = 20;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(ROWS, COLUMNS);
        board.newGame();
    }

    @Test
    void testBasicFunctionality() {
        assertEquals(0, board.getScore().getScore());

        int[][] matrix = board.getBoardMatrix();
        assertEquals(ROWS, matrix.length);
        assertEquals(COLUMNS, matrix[0].length);

        for (int[] row : matrix) {
            for (int cell : row) {
                assertEquals(0, cell);
            }
        }

        assertNotNull(board.getCurrentBrick());

        ViewData viewData = board.getViewData();
        assertNotNull(viewData);

        assertNotNull(board.getBrickGenerator());

        assertFalse(board.createNewBrick());

        ClearRow hardDropResult = board.hardDropBrick();
        assertNotNull(hardDropResult);

        ClearRow clearResult = board.clearRows();
        assertNotNull(clearResult);
    }

    @Test
    void testMovementMethods_DontCrash() {
        assertDoesNotThrow(() -> board.moveBrickDown());
        assertDoesNotThrow(() -> board.moveBrickLeft());
        assertDoesNotThrow(() -> board.moveBrickRight());
        assertDoesNotThrow(() -> board.rotateLeftBrick());
    }

    @Test
    void testResetBrickPosition_DontCrash() {
        assertDoesNotThrow(() -> board.resetBrickPosition());
    }

    @Test
    void testSetCurrentBrick_WithNull() {
        assertDoesNotThrow(() -> board.setCurrentBrick(null));
    }

    @Test
    void testCheckCollision_DontCrash() {
        assertDoesNotThrow(() -> board.checkCollision());
    }

    @Test
    void testScoreOperations() {
        Score score = board.getScore();

        score.add(100);
        assertEquals(100, score.getScore());

        score.reset();
        assertEquals(0, score.getScore());

        score.add(50);
        score.add(-20);
        assertEquals(30, score.getScore());
    }
}
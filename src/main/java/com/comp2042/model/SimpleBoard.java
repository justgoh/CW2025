package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.view.ViewData;

import java.awt.*;

/**
 * Implements the game board for Tetris gameplay.
 * <p>
 * This class manages the game board state, including the board matrix, current piece
 * position and rotation, piece movement, collision detection, and score tracking.
 * It coordinates between the brick generator, rotator, and matrix operations to
 * provide complete board functionality.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Manages the game board matrix and piece positions</li>
 *   <li>Handles piece movement and rotation with collision detection</li>
 *   <li>Generates and spawns new pieces</li>
 *   <li>Merges locked pieces into the board</li>
 *   <li>Detects and clears completed rows</li>
 *   <li>Tracks player score</li>
 * </ul>
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] boardMatrix;
    private Point brickOffset;
    private final Score score;
    private Brick currentBrick;

    /**
     * Constructs a SimpleBoard with the specified dimensions.
     *
     * @param width  the number of rows in the board
     * @param height the number of columns in the board
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        boardMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Moves the current brick down by one row.
     *
     * @return true if the move was successful, false if collision occurred
     */
    @Override
    public boolean moveBrickDown() {
        Point newoffset = new Point(brickOffset);
        newoffset.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) newoffset.getX(), (int) newoffset.getY());
        if (conflict) {
            return false;
        } else {
            brickOffset = newoffset;
            return true;
        }
    }

    /**
     * Moves the current brick left by one column.
     *
     * @return true if the move was successful, false if collision occurred
     */
    @Override
    public boolean moveBrickLeft() {
        Point newOffset = new Point(brickOffset);
        newOffset.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) newOffset.getX(), (int) newOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickOffset = newOffset;
            return true;
        }
    }

    /**
     * Moves the current brick right by one column.
     *
     * @return true if the move was successful, false if collision occurred
     */
    @Override
    public boolean moveBrickRight() {
        Point newOffset = new Point(brickOffset);
        newOffset.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) newOffset.getX(), (int) newOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickOffset = newOffset;
            return true;
        }
    }

    /**
     * Rotates the current brick counterclockwise.
     *
     * @return true if the rotation was successful, false if collision occurred
     */
    @Override
    public boolean rotateLeftBrick() {
        NextShapeInfo nextrotationShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(boardMatrix, nextrotationShape.getShape(), (int) brickOffset.getX(), (int) brickOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextrotationShape.getPosition());
            return true;
        }
    }

    /**
     * Creates and spawns a new brick at the top of the board.
     *
     * @return true if the new brick collides immediately (game over), false otherwise
     */
    @Override
    public boolean createNewBrick() {
        Brick newBrick = brickGenerator.getBrick();
        spawnBrick(newBrick);
        return MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), brickOffset.x, brickOffset.y);
    }

    /**
     * Spawns a specific brick at the default starting position.
     *
     * @param brick the brick to spawn
     */
    public void spawnBrick(Brick brick) {
        this.currentBrick = brick;
        brickRotator.setBrick(brick);
        this.brickOffset = new Point(4, 0);
    }

    /**
     * Resets the brick position to the default spawn location.
     */
    @Override
    public void resetBrickPosition() {
        brickOffset = new Point(4, 0);
    }

    /**
     * Gets the current board matrix.
     *
     * @return the 2D array representing the board state
     */
    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    /**
     * Gets the current view data for rendering.
     *
     * @return a ViewData object containing the current brick shape, position, and next piece
     */
    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), brickOffset.x, brickOffset.y, brickGenerator.getNextBrick().getShapeMatrix().getFirst());
    }

    /**
     * Merges the current brick into the board matrix.
     * <p>
     * Called when a brick is locked in place.
     */
    @Override
    public void mergeBrickToBackground() {
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), brickOffset.x, brickOffset.y);
    }

    /**
     * Checks for and clears any completed rows on the board.
     *
     * @return a ClearRow object containing information about cleared rows and score bonus
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    /**
     * Gets the score manager for this board.
     *
     * @return the Score object tracking the player's score
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Starts a new game by resetting the board and score.
     */
    @Override
    public void newGame() {
        boardMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }

    /**
     * Performs a hard drop of the current brick.
     * <p>
     * Instantly drops the brick to its lowest possible position, locks it,
     * clears any completed rows, and calculates score bonus based on drop
     * distance and lines cleared.
     *
     * @return a ClearRow object containing cleared rows info and total score bonus
     */
    public ClearRow hardDropBrick() {
        int dropDistance = 0;
        while (!MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), brickOffset.x, brickOffset.y + 1)) {
            brickOffset.translate(0, 1);
            dropDistance++;
        }

        mergeBrickToBackground();
        ClearRow clearRow = clearRows();

        int totalBonus = clearRow.getScoreBonus() + dropDistance;
        return new ClearRow(clearRow.getLinesRemoved(), clearRow.getNewMatrix(), totalBonus);
    }

    /**
     * Gets the brick generator used by this board.
     *
     * @return the BrickGenerator instance
     */
    @Override
    public BrickGenerator getBrickGenerator() {
        return brickGenerator;
    }

    /**
     * Sets the current brick to a specific piece.
     *
     * @param brick the brick to set as current, or null to clear
     */
    @Override
    public void setCurrentBrick(Brick brick) {
        this.currentBrick = brick;
        if (brick != null) {
            brickRotator.setBrick(brick);
        }
    }

    /**
     * Gets the current brick being controlled.
     *
     * @return the current Brick, or null if none
     */
    @Override
    public Brick getCurrentBrick() {
        return currentBrick;
    }

    /**
     * Checks if the current brick collides with the board.
     *
     * @return true if collision detected, false otherwise
     */
    @Override
    public boolean checkCollision() {
        return MatrixOperations.intersect(
                boardMatrix,
                brickRotator.getCurrentShape(),
                brickOffset.x,
                brickOffset.y
        );
    }
}

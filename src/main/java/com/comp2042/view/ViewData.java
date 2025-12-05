package com.comp2042.view;

import com.comp2042.model.MatrixOperations;

/**
 * Encapsulates rendering data for the current and next Tetris pieces.
 * <p>
 * This immutable class packages together all the information needed by the
 * view layer to render the current piece at its position and preview the
 * next piece. It provides defensive copies of matrix data to maintain
 * immutability.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Stores current piece shape and position data</li>
 *   <li>Provides next piece preview data</li>
 *   <li>Creates modified copies with different positions</li>
 *   <li>Maintains immutability through defensive copying</li>
 * </ul>
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    /**
     * Constructs a ViewData with the specified piece information.
     *
     * @param brickData     the 2D array representing the current piece shape
     * @param xPosition     the horizontal position of the piece on the board
     * @param yPosition     the vertical position of the piece on the board
     * @param nextBrickData the 2D array representing the next piece shape
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
    }

    /**
     * Gets a copy of the current piece's shape data.
     *
     * @return a defensive copy of the brick matrix
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the horizontal position of the current piece.
     *
     * @return the x-coordinate (column) on the board
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the vertical position of the current piece.
     *
     * @return the y-coordinate (row) on the board
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets a copy of the next piece's shape data.
     *
     * @return a defensive copy of the next brick matrix
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Creates a new ViewData with different position coordinates.
     * <p>
     * Useful for collision detection and ghost piece calculations without
     * modifying the original ViewData.
     *
     * @param x the new horizontal position
     * @param y the new vertical position
     * @return a new ViewData object with the updated position
     */
    public ViewData copyWithPosition(int x, int y) {
        return new ViewData(MatrixOperations.copy(brickData), x, y, MatrixOperations.copy(nextBrickData));
    }
}

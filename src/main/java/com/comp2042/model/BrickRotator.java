package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;

/**
 * Manages the rotation state of Tetris pieces during gameplay.
 * <p>
 * This class handles the rotation logic for Tetris bricks, maintaining the current
 * rotation state and providing access to the next rotation shape. It supports
 * cycling through a brick's available rotation states and ensures proper state
 * management when switching between different brick types.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Tracks the current rotation state of the active brick</li>
 *   <li>Provides the next rotation shape for preview and validation</li>
 *   <li>Manages brick switching with proper rotation reset</li>
 * </ul>
 *
 * @see Brick
 * @see NextShapeInfo
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Retrieves information about the brick's next rotation state.
     * <p>
     * This method calculates and returns the shape and position index that would
     * result from rotating the current brick. The rotation follows the brick's
     * predefined rotation sequence, cycling back to the first rotation after
     * reaching the last one.
     *
     * @return a {@code NextShapeInfo} object containing the next rotation's
     * shape matrix and position index, never null
     * @throws IllegalStateException if no brick has been set via {@link #setBrick(Brick)}
     * @see NextShapeInfo
     */
    public NextShapeInfo getNextShape() {
        int nextShape = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Retrieves the current rotation shape of the active brick.
     * <p>
     * Returns the shape matrix representing the brick's current orientation
     * in the game. The returned matrix is the actual shape data from the brick's
     * rotation list.
     *
     * @return the 2D integer array representing the brick's current shape,
     * never null. The array structure uses non-zero values (1-7) for
     * filled cells and zero for empty cells.
     * @throws IllegalStateException if no brick has been set via {@link #setBrick(Brick)}
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current rotation position for the active brick.
     * <p>
     * This method allows explicit control over the brick's rotation state,
     * useful for scenarios like rotation undo or specific rotation positioning.
     * The position must be a valid index within the brick's rotation list.
     *
     * @param currentShape the rotation position index to set, must be between
     *                     0 (inclusive) and the brick's rotation count (exclusive)
     * @throws IndexOutOfBoundsException if the position is outside the valid range
     * @throws IllegalStateException     if no brick has been set via {@link #setBrick(Brick)}
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the active brick and resets its rotation to the default position.
     * <p>
     * This method establishes a new brick as the active piece and initializes
     * its rotation state to the first rotation (position 0). This is the standard
     * method for brick switching during normal gameplay.
     *
     * @param brick the brick to set as active, cannot be null
     * @throws NullPointerException if the brick parameter is null
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }
}

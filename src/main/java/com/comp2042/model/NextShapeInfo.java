package com.comp2042.model;

/**
 * Represents information about a piece's next rotation state.
 * <p>
 * This immutable class encapsulates the shape matrix and position index
 * for the next rotational state of a Tetris piece. It is used during
 * rotation operations to determine the piece's new appearance.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Stores the next rotation's shape matrix</li>
 *   <li>Tracks the rotation position index</li>
 *   <li>Provides defensive copies to maintain immutability</li>
 * </ul>
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a NextShapeInfo with the specified shape and position.
     *
     * @param shape    the 2D array representing the piece's rotated shape
     * @param position the index of this rotation in the piece's rotation list
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets a copy of the shape matrix for the next rotation.
     * <p>
     * Returns a defensive copy to preserve immutability.
     *
     * @return a copy of the shape matrix, never null
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the position index of this rotation state.
     *
     * @return the rotation index in the piece's rotation list
     */
    public int getPosition() {
        return position;
    }
}

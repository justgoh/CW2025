package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Represents a Tetris piece (tetromino) in the game.
 * <p>
 * This interface defines the contract for all Tetris pieces, providing access to
 * their shape data across different rotational states. Each brick is represented
 * by a matrix where non-zero values indicate filled cells and zero values indicate
 * empty cells. Implementations of this interface encapsulate the geometric data
 * needed to render and manipulate pieces during gameplay.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides all rotational states of a Tetris piece</li>
 *   <li>Defines the shape structure using 2D integer arrays</li>
 *   <li>Supports piece rotation by offering multiple orientations</li>
 * </ul>
 *
 * @see BrickGenerator
 */
public interface Brick {

    /**
     * Retrieves all rotational states of this Tetris piece.
     * <p>
     * Each rotation is represented as a separate 2D array in the returned list.
     * The matrix structure uses non-zero values (1-7) to represent filled cells
     * with specific color codes, and zero values for empty cells.
     *
     * @return a list of 2D integer arrays where each array represents one rotation
     * state of the brick. The list is never null and contains at least one
     * rotation state. Index 0 contains the default orientation.
     */
    List<int[][]> getShapeMatrix();
}

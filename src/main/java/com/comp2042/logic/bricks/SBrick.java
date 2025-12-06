package com.comp2042.logic.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the S-shaped Tetris piece (S-tetromino).
 * <p>
 * This brick consists of four blocks arranged in an S shape, with two blocks
 * on the top row offset to the right of two blocks on the bottom row. It has
 * two rotational states: horizontal and vertical orientations.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides two distinct rotational states</li>
 *   <li>Forms an S-shape in its default orientation</li>
 *   <li>Returns defensive copies to maintain immutability</li>
 * </ul>
 */
final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an S-brick with its two rotational states.
     * <p>
     * Rotation 0: Horizontal S-shape
     * <br>
     * Rotation 1: Vertical S-shape
     */
    public SBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Gets all rotational states of the S-brick.
     *
     * @return a defensive copy of the list containing two rotation states
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}

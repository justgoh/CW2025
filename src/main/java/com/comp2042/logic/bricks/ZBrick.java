package com.comp2042.logic.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Z-shaped Tetris piece (Z-tetromino).
 * <p>
 * This brick consists of four blocks arranged in a Z shape, with two blocks
 * on the top row offset to the left of two blocks on the bottom row. It has
 * two rotational states: horizontal and vertical orientations.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides two distinct rotational states</li>
 *   <li>Forms a Z-shape in its default orientation</li>
 *   <li>Returns defensive copies to maintain immutability</li>
 * </ul>
 */
final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a Z-brick with its two rotational states.
     * <p>
     * Rotation 0: Horizontal Z-shape
     * <br>
     * Rotation 1: Vertical Z-shape
     */
    public ZBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Gets all rotational states of the Z-brick.
     *
     * @return a defensive copy of the list containing two rotation states
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}

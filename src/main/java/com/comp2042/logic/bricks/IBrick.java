package com.comp2042.logic.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the I-shaped Tetris piece (I-tetromino).
 * <p>
 * This brick consists of four blocks arranged in a straight line. It has two
 * rotational states: horizontal and vertical. The I-piece is particularly
 * useful for clearing multiple lines at once when positioned vertically.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides horizontal orientation (4 blocks in a row)</li>
 *   <li>Provides vertical orientation (4 blocks in a column)</li>
 *   <li>Returns defensive copies to maintain immutability</li>
 * </ul>
 */
final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an I-brick with its two rotational states.
     * <p>
     * Rotation 0: Horizontal orientation (━━━━)
     * <br>
     * Rotation 1: Vertical orientation (arranged in a column)
     */
    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    /**
     * Gets all rotational states of the I-brick.
     *
     * @return a defensive copy of the list containing two rotation states
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}

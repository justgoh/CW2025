package com.comp2042.logic.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the O-shaped Tetris piece (O-tetromino or square piece).
 * <p>
 * This brick consists of four blocks arranged in a 2x2 square. Unlike other
 * Tetris pieces, the O-piece has only one rotational state because it looks
 * identical in all orientations.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides a single rotational state (square shape)</li>
 *   <li>Forms a 2x2 block configuration</li>
 *   <li>Returns defensive copies to maintain immutability</li>
 * </ul>
 */
final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an O-brick with its single rotational state.
     * <p>
     * The O-piece appears as a 2x2 square and remains unchanged when rotated.
     */
    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Gets all rotational states of the O-brick.
     *
     * @return a defensive copy of the list containing one rotation state
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}

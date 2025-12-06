package com.comp2042.logic.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the T-shaped Tetris piece (T-tetromino).
 * <p>
 * This brick consists of four blocks arranged in a T shape, with three blocks
 * in a row and one block extending downward from the center. It has four
 * rotational states covering all possible orientations.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides four distinct rotational states</li>
 *   <li>Forms a T-shape in its default orientation</li>
 *   <li>Returns defensive copies to maintain immutability</li>
 * </ul>
 */
final class TBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a T-brick with its four rotational states.
     * <p>
     * Rotations progress clockwise through all four orientations of the T-shape.
     */
    public TBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {6, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 6, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 6, 0, 0},
                {6, 6, 0, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Gets all rotational states of the T-brick.
     *
     * @return a defensive copy of the list containing four rotation states
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}

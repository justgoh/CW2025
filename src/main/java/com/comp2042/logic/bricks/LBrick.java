package com.comp2042.logic.bricks;

import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the L-shaped Tetris piece (L-tetromino).
 * <p>
 * This brick consists of four blocks arranged in an L shape, with three blocks
 * in a row and one block extending downward from the left end. It has four
 * rotational states covering all possible orientations.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides four distinct rotational states</li>
 *   <li>Forms an L-shape in its default orientation</li>
 *   <li>Returns defensive copies to maintain immutability</li>
 * </ul>
 */
final class LBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs an L-brick with its four rotational states.
     * <p>
     * Rotations progress clockwise through all four orientations of the L-shape.
     */
    public LBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 3},
                {0, 3, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 3, 0},
                {0, 0, 3, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 3, 0},
                {3, 3, 3, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 3, 0, 0},
                {0, 3, 0, 0},
                {0, 3, 3, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Gets all rotational states of the L-brick.
     *
     * @return a defensive copy of the list containing four rotation states
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}

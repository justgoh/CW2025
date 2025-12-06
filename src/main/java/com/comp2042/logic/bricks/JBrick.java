package com.comp2042.logic.bricks;


import com.comp2042.model.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the J-shaped Tetris piece (J-tetromino).
 * <p>
 * This brick consists of four blocks arranged in a J shape, with three blocks
 * in a row and one block extending downward from the right end. It has four
 * rotational states covering all possible orientations.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Provides four distinct rotational states</li>
 *   <li>Forms a J-shape in its default orientation</li>
 *   <li>Returns defensive copies to maintain immutability</li>
 * </ul>
 */
final class JBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs a J-brick with its four rotational states.
     * <p>
     * Rotations progress clockwise through all four orientations of the J-shape.
     */
    public JBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 2, 2, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 2, 2, 2},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 0, 2, 0},
                {0, 0, 2, 0},
                {0, 2, 2, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Gets all rotational states of the J-brick.
     *
     * @return a defensive copy of the list containing four rotation states
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}

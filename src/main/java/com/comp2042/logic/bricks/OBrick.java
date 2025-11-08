package com.comp2042.logic.bricks;

<<<<<<< HEAD
<<<<<<< HEAD
import com.comp2042.MatrixOperations;
=======
import com.comp2042.model.MatrixOperations;
>>>>>>> 1refactor
=======
import com.comp2042.model.MatrixOperations;
>>>>>>> 2maintanence

import java.util.ArrayList;
import java.util.List;

final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}

package com.comp2042.view;

import com.comp2042.model.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
<<<<<<< HEAD
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }
=======
        this.brickData = MatrixOperations.copy(brickData);
        this.nextBrickData = MatrixOperations.copy(nextBrickData);
        this.xPosition = xPosition;
        this.yPosition = yPosition;

    }

    public int[][] getBrickData() {
        return brickData;
    }

    public int[][] getNextBrickData() {
        return nextBrickData;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

>>>>>>> 2maintanence
}

package com.comp2042.view;

import com.comp2042.model.MatrixOperations;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
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

}

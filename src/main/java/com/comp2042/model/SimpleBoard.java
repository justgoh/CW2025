package com.comp2042.model;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.view.ViewData;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] boardMatrix;
    private Point brickOffset;
    private final Score score;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        boardMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
<<<<<<< HEAD
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        Point newoffset = new Point(brickOffset);
        newoffset.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) newoffset.getX(), (int) newoffset.getY());
        if (conflict) {
            return false;
        } else {
            brickOffset = newoffset;
            return true;
        }
=======
        return tryMoveBrick(0, 1);
>>>>>>> 2maintanence
    }


    @Override
    public boolean moveBrickLeft() {
<<<<<<< HEAD
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        Point newOffset = new Point(brickOffset);
        newOffset.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) newOffset.getX(), (int) newOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickOffset = newOffset;
            return true;
        }
=======
        return tryMoveBrick(-1, 0);
>>>>>>> 2maintanence
    }

    @Override
    public boolean moveBrickRight() {
<<<<<<< HEAD
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        Point newOffset = new Point(brickOffset);
        newOffset.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) newOffset.getX(), (int) newOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickOffset = newOffset;
            return true;
        }
=======
        return tryMoveBrick(1, 0);
>>>>>>> 2maintanence
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
<<<<<<< HEAD
        NextShapeInfo nextrotationShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextrotationShape.getShape(), (int) brickOffset.getX(), (int) brickOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextrotationShape.getPosition());
=======
        NextShapeInfo nextRotationShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextRotationShape.getShape(), (int) brickOffset.getX(), (int) brickOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextRotationShape.getPosition());
>>>>>>> 2maintanence
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        brickOffset = new Point(4, 10);
        return MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), (int) brickOffset.getX(), (int) brickOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) brickOffset.getX(), (int) brickOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    @Override
    public void mergeBrickToBackground() {
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), (int) brickOffset.getX(), (int) brickOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(boardMatrix);
        boardMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        boardMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }
<<<<<<< HEAD
=======

    /**
     * Helper to move current brick by a given (dx, dy) offset
     *
     * @param dx horizontal movement (-1= left, 1= right, 0= none)
     * @param dy vertical movement (-1= up, 1= down, 0= none)
     * @return true if the brick moved successfully, false if unsuccessful
     */
    private boolean tryMoveBrick(int dx, int dy) {
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        Point newOffset = new Point(brickOffset);
        newOffset.translate(dx, dy);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) newOffset.getX(), (int) newOffset.getY());

        if (!conflict) {
            brickOffset = newOffset;
            return true;
        }
        return false;
    }
>>>>>>> 2maintanence
}

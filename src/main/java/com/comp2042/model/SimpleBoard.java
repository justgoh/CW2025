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
    private Brick currentBrick;

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
    }


    @Override
    public boolean moveBrickLeft() {
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
    }

    @Override
    public boolean moveBrickRight() {
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
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        NextShapeInfo nextrotationShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextrotationShape.getShape(), (int) brickOffset.getX(), (int) brickOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextrotationShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick newBrick = brickGenerator.getBrick();
        setCurrentBrick(newBrick);
        brickOffset = new Point(4, 0);
        return MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), brickOffset.x, brickOffset.y);
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
        brickRotator.setBrick(brickGenerator.getBrick());
        brickOffset = new Point(4, 0);
        createNewBrick();
    }

    public ClearRow hardDropBrick() {
        while (!MatrixOperations.intersect(boardMatrix, brickRotator.getCurrentShape(), brickOffset.x, brickOffset.y + 1)) {
            brickOffset.translate(0, 1);
        }

        mergeBrickToBackground();

        return clearRows();
    }

    @Override
    public BrickGenerator getBrickGenerator() {
        return brickGenerator;
    }

    @Override
    public void setCurrentBrick(Brick brick) {
        this.currentBrick = brick;
        if (brick != null) {
            brickRotator.setBrick(brick);
        }
    }

    @Override
    public Brick getCurrentBrick() {
        return currentBrick;
    }

    @Override
    public void resetBrickPosition() {
        brickOffset = new Point(4, 0);
    }

    @Override
    public boolean checkCollision() {
        return MatrixOperations.intersect(
                boardMatrix,
                brickRotator.getCurrentShape(),
                brickOffset.x,
                brickOffset.y
        );
    }
}

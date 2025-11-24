package com.comp2042.controller;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.Board;
import com.comp2042.model.ClearRow;
import com.comp2042.model.HighScoreManager;
import com.comp2042.model.SimpleBoard;
import com.comp2042.view.*;

import java.util.ArrayList;
import java.util.List;

public class GameController implements InputEventListener {

    private Board gameBoard = new SimpleBoard(25, 10);

    private final GuiController guiController;

    private final HighScoreManager highScoreManager = new HighScoreManager();

    public GameController(GuiController guiController) {
        this.guiController = guiController;
        gameBoard.createNewBrick();
        this.guiController.setEventListener(this);
        this.guiController.initGameView(gameBoard.getBoardMatrix(), gameBoard.getViewData());
        this.guiController.bindScore(gameBoard.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = gameBoard.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            gameBoard.mergeBrickToBackground();
            clearRow = gameBoard.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                gameBoard.getScore().add(clearRow.getScoreBonus());
            }
            if (gameBoard.createNewBrick()) {
                int currentScore = gameBoard.getScore().getScore();

                highScoreManager.addScore(currentScore);

                guiController.updateHighScoreLabel(highScoreManager.loadHighScore());
                guiController.gameOver();
            }

            guiController.refreshGameBackground(gameBoard.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                gameBoard.getScore().add(1);
            }
        }
        return new DownData(clearRow, gameBoard.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        gameBoard.moveBrickLeft();
        return gameBoard.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        gameBoard.moveBrickRight();
        return gameBoard.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        gameBoard.rotateLeftBrick();
        return gameBoard.getViewData();
    }


    @Override
    public void createNewGame() {
        gameBoard.newGame();
        guiController.refreshGameBackground(gameBoard.getBoardMatrix());

        guiController.updateHighScoreLabel(highScoreManager.loadHighScore());
    }

    @Override
    public boolean onGhostCheck(ViewData brick) {
        int[][] matrix = gameBoard.getBoardMatrix();
        int[][] brickData = brick.getBrickData();
        int xPos = brick.getxPosition();
        int yPos = brick.getyPosition();

        for (int row = 0; row < brickData.length; row++) {
            for (int column = 0; column < brickData[row].length; column++) {
                if (brickData[row][column] == 0) continue;
                int boardRow = yPos + row;
                int boardColumn = xPos + column;

                if (boardRow < 0 || boardRow >= matrix.length || boardColumn < 0 || boardColumn >= matrix[0].length)
                    return true;

                if (matrix[boardRow][boardColumn] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public DownData onHardDrop(ViewData brick) {
        ClearRow clearRow = gameBoard.hardDropBrick();
        boolean collision = gameBoard.createNewBrick();

        if (collision) {
            guiController.gameOver();
        }
        guiController.refreshGameBackground(gameBoard.getBoardMatrix());
        return new DownData(clearRow, gameBoard.getViewData());
    }

    @Override
    public ViewData getCurrentBrick() {
        return gameBoard.getViewData();
    }

    @Override
    public List<int[][]> getNextPieces(int count) {
        List<Brick> nextBricks = ((RandomBrickGenerator) gameBoard.getBrickGenerator()).getNextBricks(count);
        List<int[][]> nextPiecesData = new ArrayList<>();

        for (Brick brick : nextBricks) {
            nextPiecesData.add(brick.getShapeMatrix().get(0));

        }
        return nextPiecesData;
    }
}

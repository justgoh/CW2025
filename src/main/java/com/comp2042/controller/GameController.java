package com.comp2042.controller;

import com.comp2042.model.Board;
import com.comp2042.model.ClearRow;
import com.comp2042.model.SimpleBoard;
import com.comp2042.view.*;

public class GameController implements InputEventListener {

    private Board gameBoard = new SimpleBoard(25, 10);

    private final GuiController guiController;

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
    }
}

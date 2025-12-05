package com.comp2042.controller;

import com.comp2042.constants.GameConstants;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.model.Board;
import com.comp2042.model.ClearRow;
import com.comp2042.model.HighScoreManager;
import com.comp2042.model.HighScoreManager.GameMode;
import com.comp2042.model.SimpleBoard;
import com.comp2042.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the game logic and manages interaction between the model and view.
 * Handles game modes (Classic and Time Attack), scoring, piece generation, and game state transitions.
 */
 public class GameController implements InputEventListener {

    /** The game board managing piece placement and collision detection */
    private final Board gameBoard = new SimpleBoard(25, 10);

    /** Reference to the GUI controller for view updates */
    private final GuiController guiController;

    /** Manager for high scores and leaderboards */
    private final HighScoreManager highScoreManager = new HighScoreManager();

    /** Current game mode (Classic or Time Attack) */
    private GameMode currentGameMode = GameMode.CLASSIC;

    /** Timer for Time Attack mode */
    private Timeline timeAttackTimer;

    /** Time remaining in seconds for Time Attack mode */
    private int timeRemaining = 120;

    /** Flag indicating if Time Attack mode is active */
    private boolean isTimeAttackMode = false;

    /** The piece currently held by the player */
    private Brick heldBrick;

    /** Flag indicating if the hold feature can be used */
    private boolean canHold = true;

    /** Property tracking game over state */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    /**
     * Constructs a GameController with the specified GUI controller.
     * Initializes the game board, binds score, and sets up the initial view.
     *
     * @param guiController the GuiController to use for view updates
     */
    public GameController(GuiController guiController) {
        this.guiController = guiController;
        gameBoard.createNewBrick();
        this.guiController.setEventListener(this);
        this.guiController.initGameView(gameBoard.getBoardMatrix(), gameBoard.getViewData());
        this.guiController.bindScore(gameBoard.getScore().scoreProperty());

        setupTimeAttackTimer();
    }

    /**
     * Sets up the timer for Time Attack mode.
     * The timer counts down from 120 seconds and ends the game when time runs out.
     */
    private void setupTimeAttackTimer() {
        timeAttackTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), _ -> {
                    if (isTimeAttackMode && timeRemaining > 0) {
                        timeRemaining--;
                        guiController.updateTimer(timeRemaining);

                        if (timeRemaining <= 0) {
                            endTimeAttackGame();
                        }
                    }
                })
        );
        timeAttackTimer.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Ends the Time Attack game when time runs out.
     * Stops the timer and triggers game over sequence.
     */
    private void endTimeAttackGame() {
        timeAttackTimer.stop();
        isGameOver.set(true);
        handleGameOver();
        guiController.gameOver();
    }

    /**
     * Handles the game over state.
     * Saves the score, updates high score, and displays game over screen.
     */
    public void handleGameOver() {
        if (isGameOver.getValue()) {
            int finalScore = gameBoard.getScore().getScore();

            highScoreManager.addScore(currentGameMode, finalScore);

            int highScore = highScoreManager.loadHighScore(currentGameMode);
            guiController.updateHighScoreLabel(highScore);

            String modeText = (currentGameMode == GameMode.CLASSIC) ? "Classic" : "Time Attack";
            guiController.showGameOver(finalScore, modeText);
        }
    }

    /**
     * Starts a new game in Classic mode.
     * Disables time attack features and resets game state.
     */
    public void startClassicMode() {
        currentGameMode = GameMode.CLASSIC;
        isTimeAttackMode = false;

        if (timeAttackTimer != null) {
            timeAttackTimer.stop();
        }

        timeRemaining = 120;
        createNewGame();
    }

    /**
     * Starts a new game in Time Attack mode.
     * Enables the countdown timer and time-based gameplay.
     */
    public void startTimeAttackMode() {
        currentGameMode = GameMode.TIME_ATTACK;
        isTimeAttackMode = true;
        timeRemaining = 120;

        createNewGame();
    }

    /**
     * Gets the current game mode.
     *
     * @return the current GameMode (CLASSIC or TIME_ATTACK)
     */
    public HighScoreManager.GameMode getCurrentGameMode() {
        return currentGameMode;
    }

    /**
     * Gets the top scores for a specific game mode.
     *
     * @param mode the GameMode to get scores for
     * @param count the maximum number of scores to return
     * @return a list of top scores in descending order
     */
    public List<Integer> getLeaderboardScores(GameMode mode, int count) {
        return highScoreManager.getTopScores(mode, count);
    }

    /**
     * Handles the down movement event for the current piece.
     * Moves the piece down, handles collisions, clears completed rows, and spawns new pieces.
     *
     * @param event the MoveEvent containing movement details
     * @return DownData containing cleared row information and updated view data
     */
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

            canHold = true;
            guiController.setHoldEnabled(true);

            if (gameBoard.createNewBrick()) {
                isGameOver.set(true);
                handleGameOver();
                guiController.gameOver();
            }

            guiController.refreshGameBackground(gameBoard.getBoardMatrix());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                javafx.application.Platform.runLater(() -> {
                    gameBoard.getScore().add(1);
                });
            }
        }
        return new DownData(clearRow, gameBoard.getViewData());
    }

    /**
     * Handles left movement event for the current piece.
     *
     * @param event the MoveEvent containing movement details
     * @return updated ViewData after the movement
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        gameBoard.moveBrickLeft();
        return gameBoard.getViewData();
    }

    /**
     * Handles right movement event for the current piece.
     *
     * @param event the MoveEvent containing movement details
     * @return updated ViewData after the movement
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        gameBoard.moveBrickRight();
        return gameBoard.getViewData();
    }

    /**
     * Handles rotation event for the current piece.
     *
     * @param event the MoveEvent containing movement details
     * @return updated ViewData after the rotation
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        gameBoard.rotateLeftBrick();
        return gameBoard.getViewData();
    }

    /**
     * Creates a new game with fresh state.
     * Resets the board, score, held piece, and timer based on game mode.
     */
    @Override
    public void createNewGame() {
        if (timeAttackTimer != null) {
            timeAttackTimer.stop();
        }

        gameBoard.newGame();
        int highScore = highScoreManager.loadHighScore(currentGameMode);
        guiController.updateHighScoreLabel(highScore);
        isGameOver.set(false);

        heldBrick = null;
        canHold = true;
        guiController.setHoldEnabled(true);
        guiController.updateHoldPieceDisplay(getHoldPiece());

        guiController.bindScore(gameBoard.getScore().scoreProperty());
        guiController.refreshGameBackground(gameBoard.getBoardMatrix());

        if (currentGameMode == GameMode.TIME_ATTACK) {
            timeRemaining = 120;
            isTimeAttackMode = true;

            guiController.showTimer();
            guiController.updateTimer(timeRemaining);
            if (timeAttackTimer != null) {
                timeAttackTimer.stop();
                setupTimeAttackTimer();
                timeAttackTimer.play();
            }
        } else {
            isTimeAttackMode = false;
            guiController.hideTimer();
        }
    }

    /**
     * Checks if a piece position would result in a collision.
     * Used for ghost piece calculation.
     *
     * @param brick the ViewData containing piece position and shape
     * @return true if the position would cause a collision, false otherwise
     */
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

    /**
     * Handles hard drop event.
     * Instantly drops the piece to its landing position and locks it.
     *
     * @param brick the ViewData of the piece to drop
     * @return DownData containing cleared row information and updated view data
     */
    @Override
    public DownData onHardDrop(ViewData brick) {
        ClearRow clearRow = gameBoard.hardDropBrick();

        int scoreBonus = clearRow.getScoreBonus();
        if (scoreBonus > 0) {
            javafx.application.Platform.runLater(() -> {
                gameBoard.getScore().add(scoreBonus);
            });
        }

        canHold = true;
        guiController.setHoldEnabled(true);

        boolean collision = gameBoard.createNewBrick();

        if (collision) {
            isGameOver.set(true);
            handleGameOver();
            guiController.gameOver();
        }
        guiController.refreshGameBackground(gameBoard.getBoardMatrix());
        return new DownData(clearRow, gameBoard.getViewData());
    }

    /**
     * Gets the current piece view data.
     *
     * @return ViewData for the current piece
     */
    @Override
    public ViewData getCurrentBrick() {
        return gameBoard.getViewData();
    }

    /**
     * Gets the next pieces for preview display.
     *
     * @param count the number of upcoming pieces to retrieve
     * @return list of 2D arrays representing piece shapes
     */
    @Override
    public List<int[][]> getNextPieces(int count) {
        List<Brick> nextBricks = gameBoard.getBrickGenerator().getNextBricks(count);
        List<int[][]> nextPiecesData = new ArrayList<>();

        for (Brick brick : nextBricks) {
            nextPiecesData.add(brick.getShapeMatrix().get(0));

        }
        return nextPiecesData;
    }

    /**
     * Handles the hold piece action.
     * Swaps the current piece with the held piece, or stores it if none is held.
     *
     * @return true if the hold was successful, false if hold is not available
     */
    @Override
    public boolean onHoldEvent() {
        if (!canHold || isGameOver.getValue()) {
            return false;
        }

        Brick boardCurrent = gameBoard.getCurrentBrick();

        if (heldBrick == null) {
            heldBrick = boardCurrent;
            Brick nextBrick = gameBoard.getBrickGenerator().getBrick();
            gameBoard.spawnBrick(nextBrick);
            if (gameBoard.checkCollision()) {
                isGameOver.set(true);
                handleGameOver();
                guiController.gameOver();
                return false;
            }
        } else {
            Brick temp = heldBrick;
            heldBrick = boardCurrent;
            gameBoard.spawnBrick(temp);

            if (gameBoard.checkCollision()) {
                gameBoard.spawnBrick(boardCurrent);
                heldBrick = temp;
                return false;
            }
        }
        canHold = false;
        guiController.setHoldEnabled(false);
        guiController.updateHoldPieceDisplay(getHoldPiece());
        guiController.refreshBrick(gameBoard.getViewData());
        return true;
    }

    /**
     * Gets the currently held piece data.
     *
     * @return 2D array representing the held piece shape, or null if no piece is held
     */
    @Override
    public int[][] getHoldPiece() {
        return heldBrick == null ? null : heldBrick.getShapeMatrix().get(0);
    }

    /**
     * Pauses the game.
     * Pauses the Time Attack timer if in that mode.
     */
    public void pauseGame() {
        if (timeAttackTimer != null && isTimeAttackMode) {
            timeAttackTimer.pause();
        }
    }

    /**
     * Resumes the game.
     * Resumes the Time Attack timer if in that mode.
     */
    public void resumeGame() {
        if (timeAttackTimer != null && isTimeAttackMode) {
            timeAttackTimer.play();
        }
    }
}

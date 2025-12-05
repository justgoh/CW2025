package com.comp2042.controller;

import com.comp2042.view.EventSource;
import com.comp2042.view.EventType;
import com.comp2042.view.InputEventListener;
import com.comp2042.view.MoveEvent;
import javafx.beans.property.BooleanProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Handles keyboard input for the Tetris game.
 * This class processes key press events and translates them into game actions by delegating to the appropriate event listener methods.
 * <p>Supported controls:
 * <ul>
 *   <li>LEFT/A - Move piece left</li>
 *   <li>RIGHT/D - Move piece right</li>
 *   <li>UP/W - Rotate piece</li>
 *   <li>DOWN/S - Soft drop (move down faster)</li>
 *   <li>SPACE - Hard drop (instant drop)</li>
 *   <li>P - Pause/unpause game</li>
 *   <li>N - Start new game</li>
 *   <li>C/SHIFT - Hold piece</li>
 * </ul>
 *
 * <p>The handler respects game state, preventing input when the game is paused or over (except for pause and new game commands).
 */
public class GameInputHandler {

    /**
     * The event listener that processes game actions
     */
    private final InputEventListener eventListener;

    /**
     * Property indicating if the game is currently paused
     */
    private final BooleanProperty isPaused;

    /**
     * Property indicating if the game is over
     */
    private final BooleanProperty isGameOver;

    /**
     * Callback for pause action
     */
    private Runnable onPauseAction;

    /**
     * Callback for new game action
     */
    private Runnable onNewGameAction;

    /**
     * Callback for hard drop action
     */
    private Runnable onHardDropAction;

    /**
     * Callback for hold piece action
     */
    private Runnable onHoldAction;

    /**
     * Callback for move down action
     */
    private MoveDownCallback onMoveDownAction;

    /**
     * Callback for refresh brick action (after movement)
     */
    private RefreshBrickCallback onRefreshBrickAction;

    /**
     * Constructs a GameInputHandler with the specified dependencies.
     *
     * @param eventListener the InputEventListener to process game events
     * @param isPaused      the BooleanProperty tracking pause state
     * @param isGameOver    the BooleanProperty tracking game over state
     */
    public GameInputHandler(InputEventListener eventListener,
                            BooleanProperty isPaused,
                            BooleanProperty isGameOver) {
        this.eventListener = eventListener;
        this.isPaused = isPaused;
        this.isGameOver = isGameOver;
    }

    /**
     * Sets the callback for pause actions.
     *
     * @param onPauseAction the Runnable to execute when pause is triggered
     */
    public void setOnPauseAction(Runnable onPauseAction) {
        this.onPauseAction = onPauseAction;
    }

    /**
     * Sets the callback for new game actions.
     *
     * @param onNewGameAction the Runnable to execute when new game is triggered
     */
    public void setOnNewGameAction(Runnable onNewGameAction) {
        this.onNewGameAction = onNewGameAction;
    }

    /**
     * Sets the callback for hard drop actions.
     *
     * @param onHardDropAction the Runnable to execute when hard drop is triggered
     */
    public void setOnHardDropAction(Runnable onHardDropAction) {
        this.onHardDropAction = onHardDropAction;
    }

    /**
     * Sets the callback for hold piece actions.
     *
     * @param onHoldAction the Runnable to execute when hold is triggered
     */
    public void setOnHoldAction(Runnable onHoldAction) {
        this.onHoldAction = onHoldAction;
    }

    /**
     * Sets the callback for move down actions.
     *
     * @param onMoveDownAction the callback to execute when piece moves down
     */
    public void setOnMoveDownAction(MoveDownCallback onMoveDownAction) {
        this.onMoveDownAction = onMoveDownAction;
    }

    /**
     * Sets the callback for refresh brick actions.
     *
     * @param onRefreshBrickAction the callback to execute after piece movement
     */
    public void setOnRefreshBrickAction(RefreshBrickCallback onRefreshBrickAction) {
        this.onRefreshBrickAction = onRefreshBrickAction;
    }

    /**
     * Handles a key press event.
     * Routes the key press to the appropriate game action based on the key code
     * and current game state.
     *
     * @param event the KeyEvent to handle
     */
    public void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();

        if (code == KeyCode.P) {
            if (onPauseAction != null) {
                onPauseAction.run();
            }
            event.consume();
            return;
        }

        if (code == KeyCode.N) {
            if (onNewGameAction != null) {
                onNewGameAction.run();
            }
            event.consume();
            return;
        }

        if (isGameOver.getValue()) {
            return;
        }

        if (isPaused.getValue()) {
            return;
        }

        handleGameplayInput(event);
    }

    /**
     * Handles input during active gameplay.
     * Processes movement, rotation, drop, and hold actions.
     *
     * @param event the KeyEvent to handle
     */
    private void handleGameplayInput(KeyEvent event) {
        KeyCode code = event.getCode();

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            handleLeftMovement();
            event.consume();
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            handleRightMovement();
            event.consume();
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            handleRotation();
            event.consume();
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            handleSoftDrop();
            event.consume();
        } else if (code == KeyCode.SPACE) {
            if (onHardDropAction != null) {
                onHardDropAction.run();
            }
            event.consume();
        } else if (code == KeyCode.C || code == KeyCode.SHIFT) {
            if (onHoldAction != null) {
                onHoldAction.run();
            }
            event.consume();
        }
    }

    /**
     * Handles left movement input.
     * Moves the current piece one cell to the left if possible.
     */
    private void handleLeftMovement() {
        if (eventListener != null && onRefreshBrickAction != null) {
            onRefreshBrickAction.refresh(
                    eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER))
            );
        }
    }

    /**
     * Handles right movement input.
     * Moves the current piece one cell to the right if possible.
     */
    private void handleRightMovement() {
        if (eventListener != null && onRefreshBrickAction != null) {
            onRefreshBrickAction.refresh(
                    eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER))
            );
        }
    }

    /**
     * Handles rotation input.
     * Rotates the current piece clockwise if possible.
     */
    private void handleRotation() {
        if (eventListener != null && onRefreshBrickAction != null) {
            onRefreshBrickAction.refresh(
                    eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER))
            );
        }
    }

    /**
     * Handles soft drop input.
     * Moves the current piece down one cell faster than automatic dropping.
     */
    private void handleSoftDrop() {
        if (onMoveDownAction != null) {
            onMoveDownAction.moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
        }
    }

    /**
     * Functional interface for move down callbacks.
     * Used to handle the complex logic of moving a piece down.
     */
    @FunctionalInterface
    public interface MoveDownCallback {
        /**
         * Called when a piece should move down.
         *
         * @param event the MoveEvent containing movement details
         */
        void moveDown(MoveEvent event);
    }

    /**
     * Functional interface for refresh brick callbacks.
     * Used to update the visual representation after piece movement.
     */
    @FunctionalInterface
    public interface RefreshBrickCallback {
        /**
         * Called when the brick display needs to be refreshed.
         *
         * @param viewData the ViewData containing updated piece information
         */
        void refresh(com.comp2042.view.ViewData viewData);
    }
}
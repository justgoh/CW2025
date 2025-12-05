package com.comp2042.view;

import java.util.List;

/**
 * Defines callbacks for game input events and state queries.
 * <p>
 * This interface serves as the communication contract between the GUI controller
 * and the game logic controller. It handles user input events, piece movements,
 * game state changes, and provides access to game data for rendering.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Processes piece movement and rotation events</li>
 *   <li>Handles game state changes (new game, hold piece)</li>
 *   <li>Provides game data for UI rendering</li>
 *   <li>Supports collision detection and preview features</li>
 * </ul>
 */
public interface InputEventListener {

    /**
     * Handles a downward movement event for the current piece.
     *
     * @param event the MoveEvent containing movement details
     * @return a DownData object with cleared row information and updated view data
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles a leftward movement event for the current piece.
     *
     * @param event the MoveEvent containing movement details
     * @return updated ViewData after the movement
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles a rightward movement event for the current piece.
     *
     * @param event the MoveEvent containing movement details
     * @return updated ViewData after the movement
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles a rotation event for the current piece.
     *
     * @param event the MoveEvent containing movement details
     * @return updated ViewData after the rotation
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Creates and initializes a new game.
     * <p>
     * Resets the board, score, and spawns the first piece.
     */
    void createNewGame();

    /**
     * Checks if a piece would collide at a given position.
     * <p>
     * Used for ghost piece calculation to show where the piece would land.
     *
     * @param brick the ViewData containing piece position to check
     * @return true if collision would occur, false otherwise
     */
    boolean onGhostCheck(ViewData brick);

    /**
     * Performs a hard drop of the specified piece.
     * <p>
     * Instantly drops the piece to its lowest position and locks it.
     *
     * @param brick the ViewData of the piece to hard drop
     * @return a DownData object with cleared row information and score bonus
     */
    DownData onHardDrop(ViewData brick);

    /**
     * Gets the current piece's view data.
     *
     * @return ViewData containing the current piece information for rendering
     */
    ViewData getCurrentBrick();

    /**
     * Gets multiple upcoming pieces for preview display.
     *
     * @param count the number of upcoming pieces to retrieve
     * @return a list of 2D arrays representing upcoming piece shapes
     */
    List<int[][]> getNextPieces(int count);

    /**
     * Handles the hold piece action.
     * <p>
     * Swaps the current piece with the held piece, or stores it if none is held.
     *
     * @return true if the hold was successful, false if hold is not available
     */
    boolean onHoldEvent();

    /**
     * Gets the currently held piece data.
     *
     * @return a 2D array representing the held piece shape, or null if no piece is held
     */
    int[][] getHoldPiece();
}

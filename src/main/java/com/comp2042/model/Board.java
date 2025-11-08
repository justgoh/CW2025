package com.comp2042.model;

import com.comp2042.view.ViewData;

<<<<<<< HEAD
public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

=======
/**
 * Main game board  interface for managing the state of the game
 * Methods to move, rotate, create bricks, clear rows, merge bricks and retrieve game state data
 */
public interface Board {

    /**
     * Moves brick down by one row
     *
     * @return true if the brick is moved successfully, false if it cannot move further downwards
     */
    boolean moveBrickDown();

    /**
     * Moves brick left by one column
     *
     * @return true if the brick is moved successfully, false if it cannot move further leftwards
     */
    boolean moveBrickLeft();

    /**
     * Moves brick right by one column
     *
     * @return true if the brick is moved successfully, false if it cannot move further rightwards
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick counterclockwise
     *
     * @return true if the brick is rotated successfully
     */
    boolean rotateLeftBrick();

    /**
     * Creates and places a new brick on the board
     *
     * @return true if the new brick cannot be placed (end game), false otherwise
     */
    boolean createNewBrick();

    /**
     * Returns the 2D matrix representing the board state
     *
     * @return a 2D int array of the game board
     */
    int[][] getBoardMatrix();

    /**
     * Returns the view data for the current brick
     *
     * @return a {@link ViewData} object representing the current brick
     */
    ViewData getViewData();

    /**
     * Merges the current brick into the board background when it cannot move down furthermore
     */
    void mergeBrickToBackground();

    /**
     * Clears any full rows and returns the result
     *
     * @return {@link ClearRow}object with info about cleared rows and score bonus
     */
    ClearRow clearRows();

    /**
     * Returns the current score
     *
     * @return the {@link Score} instance for the current game
     */
    Score getScore();

    /**
     * Resets the game board for a current game
     */
>>>>>>> 2maintanence
    void newGame();
}

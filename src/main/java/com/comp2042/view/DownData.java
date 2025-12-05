package com.comp2042.view;

import com.comp2042.model.ClearRow;

/**
 * Encapsulates the result of a downward piece movement operation.
 * <p>
 * This immutable class packages together the line clearing results and updated
 * view data after a piece moves down. It is used to communicate both the game
 * state changes and visual updates needed when a piece descends or locks.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Stores cleared row information from the movement</li>
 *   <li>Provides updated view data for rendering</li>
 *   <li>Combines game logic and view updates in one object</li>
 * </ul>
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    /**
     * Constructs a DownData with the specified clearing and view information.
     *
     * @param clearRow the information about cleared rows, or null if no rows were cleared
     * @param viewData the updated view data for the current piece position
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    /**
     * Gets the row clearing information from this movement.
     *
     * @return the ClearRow object with cleared row data, or null if no rows were cleared
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets the updated view data after the movement.
     *
     * @return the ViewData object containing current piece information for rendering
     */
    public ViewData getViewData() {
        return viewData;
    }
}

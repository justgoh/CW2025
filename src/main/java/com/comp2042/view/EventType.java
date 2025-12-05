package com.comp2042.view;

/**
 * Represents the type of movement or action for a Tetris piece.
 * <p>
 * This enum defines all possible piece movements in the game, including
 * directional movements and rotation. Each event type corresponds to a
 * specific transformation of the current piece's position or orientation.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Defines available piece movement types</li>
 *   <li>Maps user input to game actions</li>
 *   <li>Supports event handling and piece manipulation logic</li>
 * </ul>
 */
public enum EventType {
    /**
     * Move piece down by one row
     */
    DOWN,
    /**
     * Move piece left by one column
     */
    LEFT,
    /**
     * Move piece right by one column
     */
    RIGHT,
    /**
     * Rotate piece counterclockwise
     */
    ROTATE
}

package com.comp2042.constants;

/**
 * Contains game-related constants used throughout the Tetris application.
 * <p>
 * This class centralizes magic numbers related to game mechanics, board dimensions,
 * and gameplay configuration to improve maintainability and readability. All
 * constants are immutable and accessible through static final fields.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Defines board dimensions and display settings</li>
 *   <li>Configures piece preview panels</li>
 *   <li>Specifies brick and cell sizing parameters</li>
 *   <li>Sets time attack mode thresholds</li>
 *   <li>Configures leaderboard display limits</li>
 * </ul>
 */
public final class GameConstants {
    /**
     * Private constructor to prevent instantiation.
     */
    private GameConstants() {
        throw new AssertionError("Cannot instantiate GameConstants");
    }

    // Board Dimensions

    public static final int BRICK_SIZE = 20;

    public static final int INVISIBLE_ROWS = 2;

    // Preview Panel Setting

    public static final int NEXT_PIECE_PREVIEW_COUNT = 3;

    public static final int PREVIEW_GRID_SIZE = 4;

    public static final int HOLD_PANEL_GRID_SIZE = 4;

    // Visual Settings

    public static final int BRICK_CORNER_RADIUS = 9;

    public static final double BRICK_STROKE_WIDTH = 0.3;

    public static final int PREVIEW_BRICK_SIZE = BRICK_SIZE - 2;

    // Ghost Piece Setting

    public static final double GHOST_OPACITY = 0.4;

    public static final double GHOST_STROKE_WIDTH = 1.5;

    // Time Attack Mode

    public static final int CRITICAL_TIME_THRESHOLD_SECONDS = 30;

    public static final int WARNING_TIME_THRESHOLD_SECONDS = 60;

    // Leaderboard

    public static final int MAX_LEADERBOARD_ENTRIES = 10;
}

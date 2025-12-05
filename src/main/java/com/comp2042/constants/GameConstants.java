package com.comp2042.constants;

/**
 * Contains game-related constants used throughout the Tetris application.
 * This class centralizes magic numbers related to game mechanics, board dimensions, and gameplay configuration to improve maintainability and readability.
 * <p>Constants include:
 * <ul>
 *   <li>Board dimensions and display settings</li>
 *   <li>Piece preview configurations</li>
 *   <li>Brick and cell sizing</li>
 * </ul>
 */

public final class GameConstants {
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static constants.
     */
    private GameConstants() {
        throw new AssertionError("Cannot instantiate GameConstants");
    }

    // Board Dimensions

    /**
     * The size of each brick/cell in pixels
     */
    public static final int BRICK_SIZE = 20;

    /**
     * Number of rows at the top of the board that are invisible (spawn area)
     */
    public static final int INVISIBLE_ROWS = 2;

    // Preview Panel Setting

    /**
     * Number of next pieces to preview
     */
    public static final int NEXT_PIECE_PREVIEW_COUNT = 3;

    /**
     * Grid size for next piece preview panels (4x4 grid)
     */
    public static final int PREVIEW_GRID_SIZE = 4;

    /**
     * Grid size for hold piece panel (4x4 grid)
     */
    public static final int HOLD_PANEL_GRID_SIZE = 4;

    // Visual Settings

    /**
     * Corner radius for rounded brick corners (arc width)
     */
    public static final int BRICK_CORNER_RADIUS = 9;

    /**
     * Stroke width for brick borders in pixels
     */
    public static final double BRICK_STROKE_WIDTH = 0.3;

    /**
     * Reduced brick size for preview panels (actual size minus spacing)
     */
    public static final int PREVIEW_BRICK_SIZE = BRICK_SIZE - 2;

    // Ghost Piece Setting

    /**
     * Opacity level for ghost piece (0.0 - 1.0)
     */
    public static final double GHOST_OPACITY = 0.4;

    /**
     * Stroke width for ghost piece outline
     */
    public static final double GHOST_STROKE_WIDTH = 1.5;

    // Time Attack Mode

    /**
     * Time threshold in seconds when timer turns red (critical)
     */
    public static final int CRITICAL_TIME_THRESHOLD_SECONDS = 30;

    /**
     * Time threshold in seconds when timer turns orange (warning)
     */
    public static final int WARNING_TIME_THRESHOLD_SECONDS = 60;

    // Leaderboard

    /**
     * Maximum number of scores to display in leaderboard
     */
    public static final int MAX_LEADERBOARD_ENTRIES = 10;
}

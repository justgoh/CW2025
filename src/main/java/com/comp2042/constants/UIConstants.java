package com.comp2042.constants;

/**
 * Contains UI layout and spacing constants for the Tetris application.
 * This class defines padding, gaps, sizes, and other layout-related values used in the user interface to maintain consistent spacing and sizing.
 * <p>Constants include:
 * <ul>
 *   <li>Panel padding and spacing</li>
 *   <li>Grid gaps and layout measurements</li>
 *   <li>UI component dimensions</li>
 * </ul>
 */

public final class UIConstants {
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static constants.
     */
    private UIConstants() {
        throw new AssertionError("Cannot instantiate UIConstants");
    }

    // Preview Panel Layout

    /**
     * Padding around preview panels in pixels
     */
    public static final int PREVIEW_PANEL_PADDING = 5;

    /**
     * Horizontal gap between cells in preview grids
     */
    public static final int PREVIEW_PANEL_HGAP = 1;

    /**
     * Vertical gap between cells in preview grids
     */
    public static final int PREVIEW_PANEL_VGAP = 1;

    // Leaderboard Layout

    /**
     * Spacing between leaderboard entries in HBox
     */
    public static final int LEADERBOARD_ENTRY_SPACING = 10;

    /**
     * Preferred width for leaderboard score entries
     */
    public static final int LEADERBOARD_ENTRY_WIDTH = 300;

    /**
     * Minimum width for rank label in leaderboard
     */
    public static final int LEADERBOARD_RANK_WIDTH = 30;

    // Game Board Layout

    /**
     * Horizontal gap between game board cells (should be 0 for seamless grid)
     */
    public static final int GAME_BOARD_HGAP = 0;

    /**
     * Vertical gap between game board cells (should be 0 for seamless grid)
     */
    public static final int GAME_BOARD_VGAP = 0;
}

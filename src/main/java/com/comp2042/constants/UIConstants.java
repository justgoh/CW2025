package com.comp2042.constants;

/**
 * Defines UI-related layout, spacing, and sizing constants used across the
 * Tetris application's graphical interface.
 * <p>
 * This class centralizes magic numbers related to padding, gaps, and component
 * dimensions to ensure consistent UI presentation and easier maintainability.
 * All constants are immutable and accessed through static final fields.
 * </p>
 *
 * <b>Functionality:</b>
 * <ul>
 *     <li>Configures padding and spacing for preview panels</li>
 *     <li>Defines leaderboard item layout measurements</li>
 *     <li>Specifies game board layout gaps</li>
 * </ul>
 */
public final class UIConstants {

    private UIConstants() {
        throw new AssertionError("Cannot instantiate UIConstants");
    }

    // Preview Panel Layout

    public static final int PREVIEW_PANEL_PADDING = 5;

    // Leaderboard Layout

    public static final int LEADERBOARD_ENTRY_SPACING = 10;

    public static final int LEADERBOARD_ENTRY_WIDTH = 300;

    public static final int LEADERBOARD_RANK_WIDTH = 30;

    // Game Board Layout

    public static final int GAME_BOARD_HGAP = 0;

    public static final int GAME_BOARD_VGAP = 0;
}

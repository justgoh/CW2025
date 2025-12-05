package com.comp2042.model;

/**
 * Enum representing the different UI states and screens in the Tetris application.
 * This enum is used to manage the visibility of different menus and game screens, ensuring only the appropriate UI elements are shown at any given time.
 *
 * <p>UI States:
 * <ul>
 *   <li>HOME - Main menu screen</li>
 *   <li>HOW_TO_PLAY - Instructions and tutorial screen</li>
 *   <li>THEMES - Theme selection menu</li>
 *   <li>GAME - Active gameplay screen</li>
 *   <li>PAUSED - Paused game overlay</li>
 *   <li>GAME_OVER - Game over screen with final score</li>
 *   <li>LEADERBOARD - High scores display</li>
 * </ul>
 */

public enum UIState {
    /**
     * Main menu screen state
     */
    HOME,

    /**
     * How to play instructions screen state
     */
    HOW_TO_PLAY,

    /**
     * Theme selection menu state
     */
    THEMES,

    /**
     * Active gameplay screen state
     */
    GAME,

    /**
     * Paused game overlay state
     */
    PAUSED,

    /**
     * Game over screen state
     */
    GAME_OVER,

    /**
     * Leaderboard display state
     */
    LEADERBOARD;

    /**
     * Determines if this state represents a menu screen.
     * Menu states include HOME, HOW_TO_PLAY, THEMES, and LEADERBOARD.
     *
     * @return true if this is a menu state, false otherwise
     */
    public boolean isMenuState() {
        return this == HOME || this == HOW_TO_PLAY ||
                this == THEMES || this == LEADERBOARD;
    }

    /**
     * Determines if this state represents a gameplay screen.
     * Gameplay states include GAME, PAUSED, and GAME_OVER.
     *
     * @return true if this is a gameplay state, false otherwise
     */
    public boolean isGameplayState() {
        return this == GAME || this == PAUSED || this == GAME_OVER;
    }
}

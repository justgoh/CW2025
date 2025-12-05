package com.comp2042.constants;

/**
 * Contains animation and timing-related constants for the Tetris application.
 * This class centralizes all time-based values including game tick rates, animation durations, and visual effect settings.
 * <p>Constants include:
 * <ul>
 *   <li>Game loop timing</li>
 *   <li>Animation durations</li>
 *   <li>Visual effect parameters</li>
 * </ul>
 */

public final class AnimationConfig {
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static constants.
     */
    private AnimationConfig() {
        throw new AssertionError("Cannot instantiate AnimationConfig");
    }

    // Game Loop Timing

    /**
     * Default delay between automatic piece drops in milliseconds
     */
    public static final int DEFAULT_GAME_TICK_DELAY_MS = 400;

    // Reflection Effect

    /**
     * Fraction of the reflection effect (0.0 - 1.0)
     */
    public static final double REFLECTION_FRACTION = 0.8;

    /**
     * Top opacity for reflection effect (0.0 - 1.0)
     */
    public static final double REFLECTION_TOP_OPACITY = 0.9;

    /**
     * Top offset for reflection effect in pixels
     */
    public static final int REFLECTION_TOP_OFFSET = -12;

    // Score Notification

    /**
     * Duration for score notification display (if animated)
     */
    public static final int SCORE_NOTIFICATION_DURATION_MS = 1000;
}
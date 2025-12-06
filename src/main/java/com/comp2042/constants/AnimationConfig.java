package com.comp2042.constants;

/**
 * Contains animation and timing-related constants for the Tetris application.
 * <p>
 * This class centralizes all time-based values including game tick rates,
 * animation durations, and visual effect settings. All constants are immutable
 * and accessible through static final fields.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Defines game loop timing parameters</li>
 *   <li>Configures animation durations</li>
 *   <li>Specifies visual effect parameters</li>
 * </ul>
 */
public final class AnimationConfig {

    private AnimationConfig() {
        throw new AssertionError("Cannot instantiate AnimationConfig");
    }

    // Game Loop Timing
    public static final int DEFAULT_GAME_TICK_DELAY_MS = 400;

    // Reflection Effect
    public static final double REFLECTION_FRACTION = 0.8;

    public static final double REFLECTION_TOP_OPACITY = 0.9;

    public static final int REFLECTION_TOP_OFFSET = -12;
}
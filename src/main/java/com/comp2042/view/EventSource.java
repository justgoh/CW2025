package com.comp2042.view;

/**
 * Indicates the source of a game input event.
 * <p>
 * This enum distinguishes between user-initiated actions (keyboard input)
 * and automatic actions (game timer). This distinction is important for
 * scoring and event handling logic.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Identifies whether an action was user-triggered or automatic</li>
 *   <li>Enables different scoring for manual vs automatic movements</li>
 *   <li>Supports event filtering and handling logic</li>
 * </ul>
 */
public enum EventSource {
    /**
     * Event triggered by user keyboard input
     */
    USER,
    /**
     * Event triggered automatically by the game timer/thread
     */
    THREAD
}

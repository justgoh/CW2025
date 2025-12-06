package com.comp2042.view;

/**
 * Represents the source of a piece movement event in the game.
 * <p>
 * This immutable class encapsulates whether a game event was triggered by
 * user input or the automatic game timer. This distinction enables different
 * handling and scoring logic for user-initiated versus automatic movements.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Tracks whether the event was user-triggered or automatic</li>
 *   <li>Enables event-specific scoring (e.g., points for manual down movement)</li>
 *   <li>Distinguishes keyboard input from timer-based actions</li>
 * </ul>
 */
public final class MoveEvent {
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent with the specified type and source.
     *
     * @param eventSource the source of the event (USER or THREAD)
     */
    public MoveEvent(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    /**
     * Gets the source of this event.
     *
     * @return the EventSource indicating whether this was user-triggered or automatic
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}

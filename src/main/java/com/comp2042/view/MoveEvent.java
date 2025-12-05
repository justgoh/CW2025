package com.comp2042.view;

/**
 * Represents a piece movement or action event in the game.
 * <p>
 * This immutable class encapsulates information about a game event, including
 * the type of movement (down, left, right, rotate) and the source of the event
 * (user input or automatic timer). This distinction enables different handling
 * and scoring logic for user-initiated versus automatic movements.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Stores the type of movement or action</li>
 *   <li>Tracks whether the event was user-triggered or automatic</li>
 *   <li>Enables event-specific handling and scoring logic</li>
 * </ul>
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent with the specified type and source.
     *
     * @param eventType   the type of movement (DOWN, LEFT, RIGHT, ROTATE)
     * @param eventSource the source of the event (USER or THREAD)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of movement for this event.
     *
     * @return the EventType indicating the movement direction or action
     */
    public EventType getEventType() {
        return eventType;
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

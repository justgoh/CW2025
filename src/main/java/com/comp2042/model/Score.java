package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages the player's score during gameplay.
 * <p>
 * This class provides an observable score property that can be bound to UI
 * components for automatic updates. It supports score increments, resets,
 * and direct access to the current score value.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Maintains current score as an observable property</li>
 *   <li>Supports score increments for game events</li>
 *   <li>Allows score reset for new games</li>
 *   <li>Enables UI binding through JavaFX properties</li>
 * </ul>
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Gets the observable score property for UI binding.
     * <p>
     * This property can be bound to JavaFX UI components to automatically
     * update the displayed score when it changes.
     *
     * @return the IntegerProperty representing the score, never null
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Adds points to the current score.
     *
     * @param i the number of points to add, can be negative to subtract
     */
    public void add(int i) {
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets the score to zero.
     * <p>
     * Typically called when starting a new game.
     */
    public void reset() {
        score.setValue(0);
    }

    /**
     * Gets the current score value.
     *
     * @return the current score
     */
    public int getScore() {
        return score.get();
    }

}

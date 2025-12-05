package com.comp2042.view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Displays the game over message when gameplay ends.
 * <p>
 * This JavaFX component creates a centered panel with a "GAME OVER" label
 * that is shown when the player loses. The label uses a custom style class
 * for visual formatting.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Displays game over notification to the player</li>
 *   <li>Centers the message on screen</li>
 *   <li>Applies custom styling through CSS</li>
 * </ul>
 */
public class GameOverPanel extends BorderPane {

    /**
     * Constructs a GameOverPanel with a centered "GAME OVER" label.
     * <p>
     * The label is styled using the "gameOverStyle" CSS class.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}

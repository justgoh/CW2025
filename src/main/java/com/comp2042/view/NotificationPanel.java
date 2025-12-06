package com.comp2042.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Displays animated score notifications during gameplay.
 * <p>
 * This JavaFX component creates a temporary notification that appears on screen
 * to show score bonuses (such as line clear points). The notification fades out
 * and floats upward before automatically removing itself from the scene.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Displays score bonus text with glow effect</li>
 *   <li>Animates with fade and upward translation</li>
 *   <li>Automatically removes itself after animation completes</li>
 * </ul>
 */
public class NotificationPanel extends BorderPane {

    /**
     * Constructs a NotificationPanel with the specified text.
     * <p>
     * Creates a centered label with glow effect and white text color,
     * styled using the "bonusStyle" CSS class.
     *
     * @param text the text to display (typically score bonus like "+100")
     */
    public NotificationPanel(String text) {
        setMinHeight(200);
        setMinWidth(220);
        final Label score = new Label(text);
        score.getStyleClass().add("bonusStyle");
        final Effect glow = new Glow(0.6);
        score.setEffect(glow);
        score.setTextFill(Color.WHITE);
        setCenter(score);

    }

    /**
     * Plays the notification animation and removes the panel when complete.
     * <p>
     * The animation consists of a fade out (2 seconds) and upward translation
     * (2.5 seconds) that run simultaneously. The panel is automatically removed
     * from the provided list when the animation finishes.
     *
     * @param list the observable list containing this node, used for removal after animation
     */
    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(event -> list.remove(NotificationPanel.this));
        transition.play();
    }
}

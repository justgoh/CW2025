package com.comp2042.view;

import com.comp2042.model.UIState;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Manages the visibility and state transitions of different UI screens and menus.
 * <p>
 * This class serves as a centralized controller for showing and hiding various
 * UI components, ensuring that only the appropriate screens are visible at any time.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Controls visibility of main menu, game screens, and overlays</li>
 *   <li>Manages transitions between different UI states</li>
 *   <li>Handles pause menu, game over screen, and leaderboard display</li>
 *   <li>Provides null-safe visibility toggling for all components</li>
 * </ul>
 */
public class ViewManager {
    // Main menu components
    private final VBox homeMenu;
    private final VBox howToPlayMenu;
    private final VBox themesMenu;

    // Game components
    private final StackPane gameBoard;
    private final GridPane brickPanel;
    private final VBox leftSidebar;
    private final VBox rightSidebar;

    // Overlay components
    private final VBox pauseMenu;
    private final VBox gameOverPanel;
    private final Group groupNotification;
    private final VBox leaderMenu;

    // Timer label
    private final Label timerLabel;

    /**
     * Constructs a ViewManager with references to all managed UI components.
     *
     * @param homeMenu          the main menu VBox
     * @param howToPlayMenu     the how-to-play instructions VBox
     * @param themesMenu        the theme selection menu VBox
     * @param gameBoard         the main game board StackPane
     * @param brickPanel        the GridPane containing game bricks
     * @param leftSidebar       the left sidebar VBox
     * @param rightSidebar      the right sidebar VBox
     * @param pauseMenu         the pause menu overlay VBox
     * @param gameOverPanel     the game over screen VBox
     * @param groupNotification the notification group for score popups
     * @param leaderMenu        the leaderboard display VBox
     * @param timerLabel        the timer label for time attack mode
     */
    public ViewManager(VBox homeMenu, VBox howToPlayMenu, VBox themesMenu,
                       StackPane gameBoard, GridPane brickPanel,
                       VBox leftSidebar, VBox rightSidebar,
                       VBox pauseMenu, VBox gameOverPanel,
                       Group groupNotification, VBox leaderMenu,
                       Label timerLabel) {
        this.homeMenu = homeMenu;
        this.howToPlayMenu = howToPlayMenu;
        this.themesMenu = themesMenu;
        this.gameBoard = gameBoard;
        this.brickPanel = brickPanel;
        this.leftSidebar = leftSidebar;
        this.rightSidebar = rightSidebar;
        this.pauseMenu = pauseMenu;
        this.gameOverPanel = gameOverPanel;
        this.groupNotification = groupNotification;
        this.leaderMenu = leaderMenu;
        this.timerLabel = timerLabel;
    }

    /**
     * Transitions to a new UI state.
     * <p>
     * Automatically handles showing and hiding appropriate UI components
     * based on the target state.
     *
     * @param newState the UIState to transition to
     */
    public void setState(UIState newState) {

        switch (newState) {
            case HOME:
                showHome();
                break;
            case HOW_TO_PLAY:
                showHowToPlay();
                break;
            case THEMES:
                showThemes();
                break;
            case GAME:
                showGame();
                break;
            case PAUSED:
                showPause();
                break;
            case GAME_OVER:
                showGameOver();
                break;
            case LEADERBOARD:
                showLeaderboard();
                break;
        }
    }

    /**
     * Shows the home menu and hides all other components.
     * <p>
     * This is the default starting state of the application.
     */
    public void showHome() {
        setVisible(homeMenu, true);
        setVisible(howToPlayMenu, false);
        setVisible(themesMenu, false);
        setVisible(gameBoard, false);
        setVisible(brickPanel, false);
        setVisible(leftSidebar, false);
        setVisible(rightSidebar, false);
        setVisible(pauseMenu, false);
        setVisible(gameOverPanel, false);
        setVisible(groupNotification, false);
        setVisible(leaderMenu, false);
        setVisible(timerLabel, false);
    }

    /**
     * Shows the how-to-play instructions screen.
     * <p>
     * Hides the home menu and other non-relevant components.
     */
    public void showHowToPlay() {
        setVisible(homeMenu, false);
        setVisible(howToPlayMenu, true);
        setVisible(themesMenu, false);
        setVisible(leaderMenu, false);

    }

    /**
     * Shows the theme selection menu.
     * <p>
     * Hides the home menu and other non-relevant components.
     */
    public void showThemes() {
        setVisible(homeMenu, false);
        setVisible(howToPlayMenu, false);
        setVisible(themesMenu, true);
        setVisible(leaderMenu, false);

    }

    /**
     * Shows the main game interface with all gameplay components.
     * <p>
     * Hides all menu screens and overlays.
     */
    public void showGame() {
        setVisible(homeMenu, false);
        setVisible(howToPlayMenu, false);
        setVisible(themesMenu, false);
        setVisible(gameBoard, true);
        setVisible(brickPanel, true);
        setVisible(leftSidebar, true);
        setVisible(rightSidebar, true);
        setVisible(pauseMenu, false);
        setVisible(gameOverPanel, false);
        setVisible(groupNotification, false);
        setVisible(leaderMenu, false);

    }

    /**
     * Shows the pause menu overlay.
     * <p>
     * Game components remain visible but dimmed behind the pause menu.
     */
    public void showPause() {
        setVisible(pauseMenu, true);
    }

    /**
     * Shows the game over screen.
     * <p>
     * Displays the final score and game over options.
     */
    public void showGameOver() {
        setVisible(groupNotification, true);
        setVisible(gameOverPanel, true);
    }

    /**
     * Shows the leaderboard display.
     * <p>
     * Can be shown from either menu or game states.
     */
    public void showLeaderboard() {
        setVisible(leaderMenu, true);
    }

    /**
     * Hides the leaderboard display.
     * <p>
     * Returns to the previous state.
     */
    public void hideLeaderboard() {
        setVisible(leaderMenu, false);
    }

    /**
     * Sets the visibility of a JavaFX Node safely.
     * <p>
     * Checks for null before setting visibility to prevent NullPointerExceptions.
     *
     * @param node    the Node to show or hide
     * @param visible true to show, false to hide
     */
    private void setVisible(javafx.scene.Node node, boolean visible) {
        if (node != null) {
            node.setVisible(visible);
        }
    }

}

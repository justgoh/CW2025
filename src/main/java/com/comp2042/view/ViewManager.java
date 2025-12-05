package com.comp2042.view;

import com.comp2042.model.UIState;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Manages the visibility and state transitions of different UI screens and menus.
 * This class serves as a centralized controller for showing and hiding various UI components, ensuring that only the appropriate screens are visible at any time.
 * <p>Managed UI components include:
 * <ul>
 *   <li>Main menu (home screen)</li>
 *   <li>How to play instructions</li>
 *   <li>Theme selection menu</li>
 *   <li>Game board and gameplay elements</li>
 *   <li>Pause menu overlay</li>
 *   <li>Game over screen</li>
 *   <li>Leaderboard display</li>
 * </ul>
 * <p>The ViewManager implements a state-based approach where transitioning to a new UI state automatically handles hiding irrelevant components and showing the appropriate ones.
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
     * The current UI state
     */
    private UIState currentState;

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
        this.currentState = UIState.HOME;
    }

    /**
     * Gets the current UI state.
     *
     * @return the current UIState enum value
     */
    public UIState getCurrentState() {
        return currentState;
    }

    /**
     * Transitions to a new UI state.
     * Automatically handles showing and hiding appropriate UI components.
     *
     * @param newState the UIState to transition to
     */
    public void setState(UIState newState) {
        this.currentState = newState;

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

        currentState = UIState.HOME;
    }

    /**
     * Shows the how-to-play instructions screen.
     * Hides the home menu and other non-relevant components.
     */
    public void showHowToPlay() {
        setVisible(homeMenu, false);
        setVisible(howToPlayMenu, true);
        setVisible(themesMenu, false);
        setVisible(leaderMenu, false);

        currentState = UIState.HOW_TO_PLAY;
    }

    /**
     * Shows the theme selection menu.
     * Hides the home menu and other non-relevant components.
     */
    public void showThemes() {
        setVisible(homeMenu, false);
        setVisible(howToPlayMenu, false);
        setVisible(themesMenu, true);
        setVisible(leaderMenu, false);

        currentState = UIState.THEMES;
    }

    /**
     * Shows the main game interface with all gameplay components.
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

        currentState = UIState.GAME;
    }

    /**
     * Shows the pause menu overlay.
     * Game components remain visible but dimmed behind the pause menu.
     */
    public void showPause() {
        setVisible(pauseMenu, true);
        currentState = UIState.PAUSED;
    }

    /**
     * Hides the pause menu overlay.
     * Returns to the active game state.
     */
    public void hidePause() {
        setVisible(pauseMenu, false);
        currentState = UIState.GAME;
    }

    /**
     * Shows the game over screen.
     * Displays the final score and game over options.
     */
    public void showGameOver() {
        setVisible(groupNotification, true);
        setVisible(gameOverPanel, true);
        currentState = UIState.GAME_OVER;
    }

    /**
     * Shows the leaderboard display.
     * Can be shown from either menu or game states.
     */
    public void showLeaderboard() {
        setVisible(leaderMenu, true);
        currentState = UIState.LEADERBOARD;
    }

    /**
     * Hides the leaderboard display.
     * Returns to the previous state.
     */
    public void hideLeaderboard() {
        setVisible(leaderMenu, false);
    }

    /**
     * Shows the timer label.
     * Used in time attack mode to display remaining time.
     */
    public void showTimer() {
        setVisible(timerLabel, true);
    }

    /**
     * Hides the timer label.
     * Used when not in time attack mode.
     */
    public void hideTimer() {
        setVisible(timerLabel, false);
    }

    /**
     * Hides all menus and overlays.
     * Useful for transitioning between different states.
     */
    public void hideAllMenus() {
        setVisible(homeMenu, false);
        setVisible(howToPlayMenu, false);
        setVisible(themesMenu, false);
        setVisible(pauseMenu, false);
        setVisible(gameOverPanel, false);
        setVisible(leaderMenu, false);
    }

    /**
     * Hides all game-related components.
     * Used when returning to menu states.
     */
    public void hideGameComponents() {
        setVisible(gameBoard, false);
        setVisible(brickPanel, false);
        setVisible(leftSidebar, false);
        setVisible(rightSidebar, false);
        setVisible(groupNotification, false);
        setVisible(timerLabel, false);
    }

    /**
     * Sets the visibility of a JavaFX Node safely.
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

    /**
     * Checks if the game is currently in an active gameplay state.
     *
     * @return true if in GAME, PAUSED, or GAME_OVER state
     */
    public boolean isInGameState() {
        return currentState.isGameplayState();
    }

    /**
     * Checks if the game is currently in a menu state.
     *
     * @return true if in HOME, HOW_TO_PLAY, THEMES, or LEADERBOARD state
     */
    public boolean isInMenuState() {
        return currentState.isMenuState();
    }
}

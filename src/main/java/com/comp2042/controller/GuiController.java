package com.comp2042.controller;

import com.comp2042.constants.AnimationConfig;
import com.comp2042.constants.GameConstants;
import com.comp2042.constants.UIConstants;
import com.comp2042.model.HighScoreManager;
import com.comp2042.model.HighScoreManager.GameMode;
import com.comp2042.model.Theme;
import com.comp2042.view.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main controller for the Tetris game GUI.
 * This class manages the user interface, handles user input, and coordinates between the view components and game logic. It serves as the central hub for all UI-related operations including menus, gameplay display, and theme management.
 * <p>Key responsibilities:
 * <ul>
 *   <li>Initialize and manage UI components</li>
 *   <li>Handle user input and button actions</li>
 *   <li>Coordinate with helper classes for rendering and state management</li>
 *   <li>Update displays based on game events</li>
 *   <li>Manage game modes (Classic and Time Attack)</li>
 * </ul>
 */

public class GuiController implements Initializable {

    // FXML Components
    @FXML
    private StackPane gameBoard;
    @FXML
    private GridPane gamePanel;
    @FXML
    private Group groupNotification;
    @FXML
    private GridPane brickPanel;
    @FXML
    private Label highScoreLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private VBox pauseMenu;
    @FXML
    private Button restartButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button pauseButton;
    @FXML
    private VBox leaderMenu;
    @FXML
    private VBox leaderboardList;
    @FXML
    private Button closeLeaderboardButton;
    @FXML
    private GridPane nextPiecePanel1;
    @FXML
    private GridPane nextPiecePanel2;
    @FXML
    private GridPane nextPiecePanel3;
    @FXML
    private GridPane holdPiecePanel;
    @FXML
    private Button holdButton;
    @FXML
    private VBox homeMenu;
    @FXML
    private Button startButton;
    @FXML
    private Button homeLeaderboardButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button restartFromGameOver;
    @FXML
    private Button mainMenuFromGameOver;
    @FXML
    private Button backToMenuButton;
    @FXML
    private Button backToMenuFromPause;
    @FXML
    private VBox leftSidebar;
    @FXML
    private VBox rightSidebar;
    @FXML
    private VBox howToPlayMenu;
    @FXML
    private Button startGameButton;
    @FXML
    private Button backToMenuFromHowToPlay;
    @FXML
    private VBox themesMenu;
    @FXML
    private Button themesButton;
    @FXML
    private Button defaultThemeButton;
    @FXML
    private Button countrysideButton;
    @FXML
    private Button beachButton;
    @FXML
    private Button tronButton;
    @FXML
    private Button backFromThemesButton;
    @FXML
    private VBox gameOverPanel;
    @FXML
    private Button timeAttackButton;
    @FXML
    private Label timerLabel;
    @FXML
    private Button resumeButton;
    @FXML
    private Label modeIndicatorLabel;
    @FXML
    private Button classicLeaderboardButton;
    @FXML
    private Button timeAttackLeaderboardButton;
    @FXML
    public Button leaderboardButton;

    // Helper Classes

    /**
     * Manages UI component visibility and state transitions
     */
    private ViewManager viewManager;

    /**
     * Handles rendering of game pieces
     */
    private PieceRenderer pieceRenderer;

    /**
     * Manages application themes
     */
    private ThemeManager themeManager;

    /**
     * Handles leaderboard display
     */
    private LeaderboardView leaderboardView;

    /**
     * Manages game timeline and automatic piece dropping
     */
    private GameTimeline gameTimeline;

    /**
     * Handles keyboard input
     */
    private GameInputHandler inputHandler;

    // Game State

    /**
     * Event listener for game logic
     */
    private InputEventListener eventListener;

    /**
     * Property tracking pause state
     */
    private final BooleanProperty isPause = new SimpleBooleanProperty();

    /**
     * Property tracking game over state
     */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /**
     * Reference to the game controller
     */
    private GameController gameController;

    /**
     * The game mode to start (Classic or Time Attack)
     */
    private GameMode modeToStart = GameMode.CLASSIC;

    // Display Components

    /**
     * 2D array of rectangles representing the game board
     */
    private Rectangle[][] displayMatrix;

    /**
     * 2D array of rectangles for the current piece
     */
    private Rectangle[][] rectangles;

    /**
     * 2D array of rectangles for the ghost piece
     */
    private Rectangle[][] ghostRectangles;

    /**
     * 3D array of rectangles for next piece previews
     */
    private Rectangle[][][] nextPieceRectangles;

    /**
     * 2D array of rectangles for the hold piece
     */
    private Rectangle[][] holdPieceRectangles;

    // Initialization

    /**
     * Initializes the controller after FXML components are loaded.
     * Sets up the initial UI state, creates helper objects, loads high scores,
     * and configures input handlers.
     *
     * @param location  the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize view manager
        viewManager = new ViewManager(
                homeMenu, howToPlayMenu, themesMenu,
                gameBoard, brickPanel, leftSidebar, rightSidebar,
                pauseMenu, gameOverPanel, groupNotification, leaderMenu,
                timerLabel
        );

        // Initialize other helpers
        pieceRenderer = new PieceRenderer();
        themeManager = new ThemeManager();
        leaderboardView = new LeaderboardView(leaderboardList);

        // Set initial UI state
        viewManager.showHome();

        if (modeIndicatorLabel != null) {
            updateModeIndicator("Classic");
        }

        // Load digital font for score display
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        // Configure game panel for input
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        // Load and display high score
        HighScoreManager hsm = new HighScoreManager();
        int highscore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + highscore);

        // Setup reflection effect
        final Reflection reflection = new Reflection();
        reflection.setFraction(AnimationConfig.REFLECTION_FRACTION);
        reflection.setTopOpacity(AnimationConfig.REFLECTION_TOP_OPACITY);
        reflection.setTopOffset(AnimationConfig.REFLECTION_TOP_OFFSET);

        // Initialize preview panels
        initializeNextPiecePreview();
        initializeHoldPiecePanel();
        setupHoldKeyBinding();
        initializeHowToPlayBackground();
    }

    // Dependency Injection

    /**
     * Sets the PieceRenderer for this controller.
     *
     * @param pieceRenderer the PieceRenderer to use
     */
    public void setPieceRenderer(PieceRenderer pieceRenderer) {
        this.pieceRenderer = pieceRenderer;
    }

    /**
     * Sets the ThemeManager for this controller.
     *
     * @param themeManager the ThemeManager to use
     */
    public void setThemeManager(ThemeManager themeManager) {
        this.themeManager = themeManager;
    }

    /**
     * Sets the ViewManager for this controller.
     *
     * @param viewManager the ViewManager to use
     */
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    /**
     * Sets the LeaderboardView for this controller.
     *
     * @param leaderboardView the LeaderboardView to use
     */
    public void setLeaderboardView(LeaderboardView leaderboardView) {
        this.leaderboardView = leaderboardView;
    }

    /**
     * Sets the GameController for this controller.
     *
     * @param gameController the GameController to use
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        if (gameController != null) {
            GameMode currentMode = gameController.getCurrentGameMode();
            updateModeIndicator(currentMode == GameMode.CLASSIC ? "Classic" : "Time Attack");
        }
    }

    /**
     * Sets the InputEventListener for game logic callbacks.
     *
     * @param eventListener the InputEventListener to use
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    // Game Initialization

    /**
     * Initializes the game view with the board matrix and initial piece.
     * Sets up the display grid, game panel dimensions, and creates initial rectangles for pieces and ghost pieces.
     *
     * @param boardMatrix the 2D array representing the game board
     * @param brick       the initial piece data
     */

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        gamePanel.getChildren().clear();
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        // Create display matrix (skip invisible rows)
        for (int row = GameConstants.INVISIBLE_ROWS; row < boardMatrix.length; row++) {
            for (int column = 0; column < boardMatrix[row].length; column++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[row][column] = rectangle;
                gamePanel.add(rectangle, column, row - GameConstants.INVISIBLE_ROWS);
            }
        }
        brickPanel.getChildren().clear();

        // Set game panel dimensions
        int panelWidth = boardMatrix[0].length * GameConstants.BRICK_SIZE;
        int panelHeight = (boardMatrix.length - GameConstants.INVISIBLE_ROWS) * GameConstants.BRICK_SIZE;

        gamePanel.setMinSize(panelWidth, panelHeight);
        gamePanel.setMaxSize(panelWidth, panelHeight);
        gamePanel.setPrefSize(panelWidth, panelHeight);

        // Set clipping rectangle
        Rectangle clip = new Rectangle();
        clip.setWidth(panelWidth);
        clip.setHeight(panelHeight);
        gamePanel.setClip(clip);

        // Create rectangles for current piece
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {

                // Create ghost piece rectangle
                Rectangle ghost = pieceRenderer.createGhostBrick();
                ghostRectangles[row][column] = ghost;
                gamePanel.getChildren().add(ghost);

                // Create current piece rectangle
                Rectangle rectangle = pieceRenderer.createStyledBrick(brick.getBrickData()[row][column]);
                rectangle.setManaged(false);
                rectangles[row][column] = rectangle;
                gamePanel.getChildren().add(rectangle);
            }
        }

        // Configure grid spacing
        gamePanel.setPadding(Insets.EMPTY);
        gamePanel.setHgap(UIConstants.GAME_BOARD_HGAP);
        gamePanel.setVgap(UIConstants.GAME_BOARD_VGAP);

        brickPanel.setPadding(Insets.EMPTY);
        brickPanel.setHgap(UIConstants.GAME_BOARD_HGAP);
        brickPanel.setVgap(UIConstants.GAME_BOARD_VGAP);
        brickPanel.setVisible(false);
        brickPanel.toFront();

        updateBrickPosition(brick);
        updateGhost(brick);

        // Create game Timeline
        gameTimeline = new GameTimeline(() -> {
            if (!isGameOver.getValue() && !isPause.getValue()) {
                moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
            }
        });
        gameTimeline.start();
    }

    // Piece Rendering

    /**
     * Gets the fill color for a given color code.
     * Delegates to PieceRenderer.
     *
     * @param colorCode the color code (0-7)
     * @return the Paint color
     */
    private Paint getFillColor(int colorCode) {
        return pieceRenderer.getFillColor(colorCode);
    }

    /**
     * Refreshes the brick display after a move.
     * Updates the piece rectangles if the piece shape has changed, and updates positions and ghost piece.
     *
     * @param brick the updated ViewData for the current piece
     */
    public void refreshBrick(ViewData brick) {
            if (rectangles.length != brick.getBrickData().length ||
                    rectangles[0].length != brick.getBrickData()[0].length) {
                recreatePieceRectangles(brick);
            }
            updateBrickPosition(brick);
            updateGhost(brick);
            updateNextPiecesFromGenerator();

    }

    /**
     * Recreates piece rectangles when the piece shape changes (e.g., after rotation).
     * Removes old rectangles and creates new ones with the correct dimensions.
     *
     * @param brick the ViewData containing the new piece shape
     */
    private void recreatePieceRectangles(ViewData brick) {
        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                gamePanel.getChildren().remove(rectangles[row][column]);
                gamePanel.getChildren().remove(ghostRectangles[row][column]);
            }
        }

        // Create new arrays
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        // Create new rectangles
        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                Rectangle ghost = pieceRenderer.createGhostBrick();
                ghost.setVisible(false);
                ghost.setManaged(false);
                ghostRectangles[row][column] = ghost;

                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setArcHeight(GameConstants.BRICK_CORNER_RADIUS);
                rectangle.setArcWidth(GameConstants.BRICK_CORNER_RADIUS);
                rectangles[row][column] = rectangle;
                rectangle.setManaged(false);

                gamePanel.getChildren().add(rectangle);
            }
        }

        // Add ghost rectangles
        for (Rectangle[] ghostRow : ghostRectangles) {
            for (Rectangle ghost : ghostRow) {
                gamePanel.getChildren().add(ghost);
            }
        }

        // Add piece rectangles on top
        for (Rectangle[] rectRow : rectangles) {
            for (Rectangle rectangle : rectRow) {
                gamePanel.getChildren().add(rectangle);
            }
        }
    }

    /**
     * Updates the position of the current piece rectangles.
     * Sets visibility, color, and translation for each rectangle based on the piece data and position.
     *
     * @param brick the ViewData containing piece position and data
     */
    private void updateBrickPosition(ViewData brick) {
        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                Rectangle rectangle = rectangles[row][column];
                int displayRow = brick.getyPosition() + row - GameConstants.INVISIBLE_ROWS;

                if (brick.getBrickData()[row][column] != 0 && (displayRow >= 0)) {
                    rectangle.setVisible(true);
                    pieceRenderer.applyBrickStyling(rectangle, brick.getBrickData()[row][column]);
                    rectangle.setTranslateX((brick.getxPosition() + column) * GameConstants.BRICK_SIZE);
                    rectangle.setTranslateY(displayRow * GameConstants.BRICK_SIZE);
                } else {
                    rectangle.setVisible(false);
                }
            }
        }
    }

    /**
     * Updates the ghost piece position.
     * Calculates where the current piece would land if hard dropped, and positions the ghost piece rectangles accordingly.
     *
     * @param brick the ViewData for the current piece
     */
    private void updateGhost(ViewData brick) {
        int ghostY = brick.getyPosition();
        while (true) {
            ViewData temp = brick.copyWithPosition(brick.getxPosition(), ghostY + 1);
            if (eventListener.onGhostCheck(temp)) {
                break;
            }
            ghostY++;
        }

        for (int row = 0; row < ghostRectangles.length; row++) {
            for (int column = 0; column < ghostRectangles[row].length; column++) {
                Rectangle ghost = ghostRectangles[row][column];
                int displayRow = ghostY + row - GameConstants.INVISIBLE_ROWS;

                if (brick.getBrickData()[row][column] != 0 && displayRow >= 0) {
                    ghost.setVisible(true);
                    ghost.setTranslateX((brick.getxPosition() + column) * GameConstants.BRICK_SIZE);
                    ghost.setTranslateY(displayRow * GameConstants.BRICK_SIZE);
                    ghost.setFill(Color.TRANSPARENT);
                    ghost.setStroke(Color.GRAY);
                    ghost.setStrokeWidth(GameConstants.GHOST_STROKE_WIDTH);
                    ghost.setOpacity(GameConstants.GHOST_OPACITY);
                } else {
                    ghost.setVisible(false);
                }
            }
        }
    }

    /**
     * Refreshes the game background based on the board matrix.
     * Updates the display matrix to show locked pieces.
     *
     * @param board the 2D array representing the game board
     */
    public void refreshGameBackground(int[][] board) {
        for (int row = GameConstants.INVISIBLE_ROWS; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                pieceRenderer.applyBrickStyling(
                        displayMatrix[row][column],
                        board[row][column]
                );
            }
        }
    }

    // Input Handling

    /**
     * Handles key press events.
     * Creates an input handler if needed and delegates to it.
     *
     * @param keyEvent the KeyEvent to handle
     */
    private void handleKeyPress(KeyEvent keyEvent) {
        if (inputHandler == null) {
            createInputHandler();
        }
        inputHandler.handleKeyPress(keyEvent);
    }

    /**
     * Creates and configures the input handler with all necessary callbacks.
     */
    private void createInputHandler() {
        inputHandler = new GameInputHandler(eventListener, isPause, isGameOver);

        inputHandler.setOnPauseAction(() -> pauseGame(null));
        inputHandler.setOnNewGameAction(() -> newGame(null));
        inputHandler.setOnHardDropAction(this::hardDrop);
        inputHandler.setOnHoldAction(() -> holdPiece(null));
        inputHandler.setOnMoveDownAction(this::moveDown);
        inputHandler.setOnRefreshBrickAction(this::refreshBrick);
    }

    /**
     * Moves the current piece down by one row.
     * Handles line clearing, score notifications, and piece locking.
     *
     * @param event the MoveEvent containing movement details
     */
    private void moveDown(MoveEvent event) {
        if (gameTimeline == null) {
            return;
        }
        if (isGameOver.getValue() || isPause.getValue()) {
            return;
        }
        DownData downData = eventListener.onDownEvent(event);

        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
            groupNotification.setVisible(true);

        }
        refreshBrick(downData.getViewData());
        gamePanel.requestFocus();
    }

    /**
     * Performs a hard drop of the current piece.
     * Instantly drops the piece to its landing position and locks it.
     */
    public void hardDrop() {
        DownData downData = eventListener.onHardDrop(eventListener.getCurrentBrick());
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore((groupNotification.getChildren()));
            groupNotification.setVisible(true);
        }

        refreshBrick((downData.getViewData()));
        gamePanel.requestFocus();
    }

    // Next Piece Preview

    /**
     * Initializes the next piece preview panels.
     * Creates a 4x4 grid of rectangles in each preview panel.
     */
    private void initializeNextPiecePreview() {
        nextPieceRectangles = new Rectangle[GameConstants.NEXT_PIECE_PREVIEW_COUNT][][];
        GridPane[] previewPanels = {nextPiecePanel1, nextPiecePanel2, nextPiecePanel3};

        for (int panelIndex = 0; panelIndex < previewPanels.length; panelIndex++) {
            GridPane panel = previewPanels[panelIndex];
            panel.getChildren().clear();
            nextPieceRectangles[panelIndex] = pieceRenderer.initializePreviewPanel(panel);
        }
    }

    /**
     * Updates all next piece preview panels with new piece data.
     *
     * @param nextBrickDataList the list of piece data arrays for upcoming pieces
     */
    public void updateNextPieces(List<int[][]> nextBrickDataList) {
        for (int i = 0; i < GameConstants.NEXT_PIECE_PREVIEW_COUNT; i++) {
            if (i < nextBrickDataList.size()) {
                pieceRenderer.renderPieceOnPreview(
                        getPreviewPanelByIndex(i),
                        nextBrickDataList.get(i),
                        nextPieceRectangles[i]
                );
            } else {
                pieceRenderer.clearPreviewPanel(nextPieceRectangles[i]);
            }
        }
    }

    /**
     * Gets a preview panel by its index.
     *
     * @param index the panel index (0, 1, or 2)
     * @return the corresponding GridPane
     */
    private GridPane getPreviewPanelByIndex(int index) {
        switch (index) {
            case 0:
                return nextPiecePanel1;
            case 1:
                return nextPiecePanel2;
            case 2:
                return nextPiecePanel3;
            default:
                return nextPiecePanel1;
        }
    }

    /**
     * Updates the next piece previews from the piece generator.
     */
    private void updateNextPiecesFromGenerator() {
        if (eventListener != null) {
            List<int[][]> nextPieces = eventListener.getNextPieces(GameConstants.NEXT_PIECE_PREVIEW_COUNT);
            updateNextPieces(nextPieces);
        }
    }

    // Hold Piece

    /**
     * Initializes the hold piece panel.
     * Creates a 4x4 grid of rectangles for displaying the held piece.
     */
    private void initializeHoldPiecePanel() {
        holdPieceRectangles = pieceRenderer.initializePreviewPanel(holdPiecePanel);
    }

    /**
     * Sets up key binding for the hold piece feature.
     */
    private void setupHoldKeyBinding() {
        gamePanel.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (isGameOver.getValue() || isPause.getValue()) return;
            if (keyEvent.getCode() == KeyCode.C || keyEvent.getCode() == KeyCode.SHIFT) {
                holdPiece(null);
                keyEvent.consume();
            }
        });
    }

    /**
     * Handles the hold piece action.
     * Swaps the current piece with the held piece, or stores the current piece if none is held.
     *
     * @param actionEvent the ActionEvent (can be null if triggered by keyboard)
     */
    @FXML
    public void holdPiece(ActionEvent actionEvent) {
        if (isGameOver.getValue() || isPause.getValue()) return;

        if (eventListener != null) {
            boolean success = eventListener.onHoldEvent();
            if (success) {
                int[][] holdPieceData = eventListener.getHoldPiece();
                updateHoldPieceDisplay(holdPieceData);
                refreshBrick(eventListener.getCurrentBrick());
            }
        }
        gamePanel.requestFocus();
    }

    /**
     * Updates the hold piece display panel.
     *
     * @param holdPieceData the 2D array representing the held piece, or null if no piece is held
     */
    public void updateHoldPieceDisplay(int[][] holdPieceData) {
        pieceRenderer.renderPieceOnPreview(holdPiecePanel, holdPieceData, holdPieceRectangles);
    }

    /**
     * Sets whether the hold feature is enabled or disabled.
     * Updates the button styling to reflect the state.
     *
     * @param enabled true if hold is available, false if already used
     */
    public void setHoldEnabled(boolean enabled) {
        if (enabled) {
            holdButton.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white;");
            holdButton.setText("Hold (C)");
        } else {
            holdButton.setStyle("-fx-background-color: #666666; -fx-text-fill: #999999;");
            holdButton.setText("Hold Used");
        }
    }

    // Game Control

    /**
     * Binds the score label to a score property.
     * The label will automatically update when the score changes.
     *
     * @param scoreProperty the IntegerProperty to bind to
     */
    public void bindScore(IntegerProperty scoreProperty) {
        scoreLabel.textProperty().unbind();
        scoreLabel.setText(null);
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    /**
     * Handles the game over state.
     * Stops the game timeline and displays the game over screen.
     */
    public void gameOver() {
        if (gameTimeline != null) {
            gameTimeline.stop();
        }

        if (groupNotification != null && gameOverPanel != null) {
            groupNotification.setVisible(true);
            gameOverPanel.setVisible(true);
        }
        isGameOver.setValue(Boolean.TRUE);

        if (gameController != null) {
            GameMode currentMode = gameController.getCurrentGameMode();
            int finalScore = Integer.parseInt(scoreLabel.getText().replace("Score: ", ""));
            int highScore = gameController.getLeaderboardScores(currentMode, 1).isEmpty() ?
                    0 : gameController.getLeaderboardScores(currentMode, 1).get(0);
            highScoreLabel.setText("High Score: " + highScore);
        }
    }

    /**
     * Starts a new game.
     * Resets game state, hides menus, and initializes a fresh game.
     *
     * @param actionEvent the ActionEvent (can be null)
     */
    public void newGame(ActionEvent actionEvent) {
        if (gameTimeline != null) {
            gameTimeline.stop();
            gameTimeline = null;
        }

        isGameOver.setValue(Boolean.FALSE);
        isPause.setValue(Boolean.FALSE);

        if (groupNotification != null) {
            groupNotification.setVisible(false);
        }
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
        if (pauseMenu != null) {
            pauseMenu.setVisible(false);
        }

        if (gameController != null) {
            GameMode currentMode = gameController.getCurrentGameMode();
            int highScore = gameController.getLeaderboardScores(currentMode, 1).isEmpty() ?
                    0 : gameController.getLeaderboardScores(currentMode, 1).get(0);
            highScoreLabel.setText("High Score: " + highScore);
        }

        eventListener.createNewGame();

        int speed = 500;
        if (gameController != null && gameController.getCurrentGameMode() == GameMode.TIME_ATTACK) {
            speed = 300;
        }
        gameTimeline = new GameTimeline(() -> {
            if (!isGameOver.getValue() && !isPause.getValue()) {
                moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
            }
        }, speed
        );
        gameTimeline.start();

        updateNextPiecesFromGenerator();
        gamePanel.requestFocus();
    }

    /**
     * Updates the high score label.
     *
     * @param highscore the new high score value
     */
    public void updateHighScoreLabel(int highscore) {
        if (highScoreLabel != null) {
            highScoreLabel.setText("High Score: " + highscore);
        }
    }

    /**
     * Toggles the pause state of the game.
     *
     * @param actionEvent the ActionEvent (can be null)
     */
    public void pauseGame(ActionEvent actionEvent) {
        if (isGameOver.getValue()) {
            return;
        }
        if (isPause.get()) {
            isPause.set(false);
            pauseMenu.setVisible(false);

            if (gameController != null) {
                gameController.resumeGame();
            }

            if (gameTimeline != null) {
                gameTimeline.resume();
            }
        } else {
            isPause.set(true);
            pauseMenu.setVisible(true);

            if (gameController != null) {
                gameController.pauseGame();
            }
            if (gameTimeline != null) {
                gameTimeline.pause();
            }
        }

        if (gamePanel != null) {
            gamePanel.requestFocus();
        }
    }

    /**
     * Pauses the game (called externally).
     */
    public void pauseGame() {
        if (gameTimeline != null) {
            gameTimeline.pause();
        }
        if (gameController != null) {
            gameController.pauseGame();
        }
    }

    /**
     * Resumes the game (called externally).
     */
    public void resumeGame() {
        if (gameTimeline != null) {
            gameTimeline.resume();
            }

        if (gameController != null) {
            gameController.resumeGame();
        }
    }

    /**
     * Restarts the current game.
     *
     * @param event the ActionEvent
     */
    public void restartGame(ActionEvent event) {
        newGame(event);
    }

    /**
     * Quits the application.
     *
     * @param actionEvent the ActionEvent
     */
    public void quitGame(ActionEvent actionEvent) {
        System.exit(0);
    }

    // Menu Navigation

    /**
     * Starts the game from the main menu.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void startGame(ActionEvent actionEvent) {
        if (gameTimeline != null) {
            gameTimeline.stop();
            gameTimeline = null;
        }

        viewManager.showGame();
        newGame(actionEvent);
        gamePanel.requestFocus();
    }

    /**
     * Returns to the main menu from any screen.
     * Stops the game and cleans up resources.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void backToMainMenu(ActionEvent actionEvent) {
        if (gameTimeline != null) {
            gameTimeline.stop();
            gameTimeline = null;
        }

        if (gameController != null) {
            try {
                gameController.pauseGame();
            } catch (Exception e) {
                System.out.println("DEBUG: Error stopping game controller timers: " + e.getMessage());
            }
        }

        isPause.set(false);
        isGameOver.set(false);

        if (groupNotification != null) {
            groupNotification.setVisible(false);
        }
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
        if (pauseMenu != null) {
            pauseMenu.setVisible(false);
        }

        viewManager.showHome();
    }

    /**
     * Shows the How to Play screen.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void showHowToPlay(ActionEvent actionEvent) {
        showHowToPlayInternal();
    }

    /**
     * Internal method to show How to Play screen.
     * Updates the start button based on the selected game mode.
     */
    private void showHowToPlayInternal() {

        if (startGameButton != null) {
            if (modeToStart == GameMode.TIME_ATTACK) {
                startGameButton.setText("START TIME ATTACK");
                startGameButton.setStyle("-fx-background-color: #ff6b35; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 12 25; -fx-font-weight: bold;");
            } else {
                startGameButton.setText("START CLASSIC");
                startGameButton.setStyle("-fx-background-color: #23c42a; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 12 25; -fx-font-weight: bold;");
            }
        }
    }

    /**
     * Starts the game from the How to Play screen.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void startGameFromHowToPlay(ActionEvent actionEvent) {
        if (gameTimeline != null) {
            gameTimeline.stop();
            gameTimeline = null;
        }

        if (howToPlayMenu != null) howToPlayMenu.setVisible(false);

        if (homeMenu != null) homeMenu.setVisible(false);
        if (themesMenu != null) themesMenu.setVisible(false);
        if (gameBoard != null) gameBoard.setVisible(true);
        if (brickPanel != null) brickPanel.setVisible(true);
        if (leftSidebar != null) leftSidebar.setVisible(true);
        if (rightSidebar != null) rightSidebar.setVisible(true);

        if (modeToStart == GameMode.TIME_ATTACK) {
            if (gameController != null) {
                gameController.startTimeAttackMode();
                updateModeIndicator("Time Attack");
            }
        } else {
            if (gameController != null) {
                gameController.startClassicMode();
                updateModeIndicator("Classic");
            }
        }

        int speed = 500;
        if (gameController != null && gameController.getCurrentGameMode() == GameMode.TIME_ATTACK) {
            speed = 300;
        }

        gameTimeline = new GameTimeline(() -> {
            if (!isGameOver.getValue() && !isPause.getValue()) {
                moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
            }
        }, speed);

        gameTimeline.start();

        isGameOver.setValue(Boolean.FALSE);
        isPause.setValue(Boolean.FALSE);

        if (gamePanel != null) {
            gamePanel.requestFocus();
        }
    }

    /**
     * Returns to main menu from How to Play screen.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void backToMainMenuFromHowToPlay(ActionEvent actionEvent) {
        backToMainMenu(actionEvent);
    }

    // Theme Management

    /**
     * Shows the theme selection menu.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void showThemes(ActionEvent actionEvent) {
        viewManager.showThemes();
    }

    /**
     * Returns to main menu from themes screen.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void backToMainMenuFromThemes(ActionEvent actionEvent) {
        backToMainMenu(actionEvent);
    }

    /**
     * Applies the default theme.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void setDefaultTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.DEFAULT, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Applies the countryside theme.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void setCountrysideTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.COUNTRYSIDE, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Applies the beach theme.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void setBeachTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.BEACH, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Applies the tron theme.
     *
     * @param actionEvent the ActionEvent
     */
    @FXML
    public void setTronTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.TRON, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Initializes the How to Play background image.
     */
    private void initializeHowToPlayBackground() {
        try {
            javafx.scene.image.Image howToPlayBg = new javafx.scene.image.Image(
                    getClass().getResourceAsStream("/howtoplay.png")
            );
            BackgroundImage backgroundImage = new BackgroundImage(
                    howToPlayBg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            howToPlayMenu.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.out.println("Error loading How To Play background: " + e.getMessage());
        }
    }

    // Game Mode

    /**
     * Prepares to start Classic mode.
     */
    @FXML
    public void showHowToPlayForClassic() {
        modeToStart = GameMode.CLASSIC;
        showHowToPlayInternal();
        viewManager.showHowToPlay();
    }

    /**
     * Prepares to start Time Attack mode.
     */
    @FXML
    public void showHowToPlayForTimeAttack() {
        modeToStart = GameMode.TIME_ATTACK;
        showHowToPlayInternal();
        viewManager.showHowToPlay();
    }


    /**
     * Starts Classic mode.
     */
    @FXML
    public void startClassicMode() {
        viewManager.showGame();

        if (gameController != null) {
            gameController.startClassicMode();
            updateModeIndicator("Classic");
        }
        newGame(null);
    }

    /**
     * Starts Time Attack mode.
     */
    @FXML
    public void startTimeAttackMode() {
        viewManager.showGame();

        if (gameController != null) {
            gameController.startTimeAttackMode();
            updateModeIndicator("Time Attack");
        }
        newGame(null);
    }

    /**
     * Updates the mode indicator label.
     *
     * @param mode the mode name ("Classic" or "Time Attack")
     */
    private void updateModeIndicator(String mode) {
        if (modeIndicatorLabel != null) {
            modeIndicatorLabel.setText("Mode: " + mode);

            if (mode.equals("Time Attack")) {
                modeIndicatorLabel.setTextFill(javafx.scene.paint.Color.web("#ff6b35"));
            } else {
                modeIndicatorLabel.setTextFill(javafx.scene.paint.Color.web("#52b49b"));
            }
        }
    }

    // Timer for Time Attack

    /**
     * Updates the timer display.
     *
     * @param seconds the remaining seconds
     */
    public void updateTimer(int seconds) {
        if (timerLabel != null) {
            int minutes = seconds / 60;
            int secs = seconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, secs));

            if (seconds <= GameConstants.CRITICAL_TIME_THRESHOLD_SECONDS) {
                timerLabel.setTextFill(javafx.scene.paint.Color.RED);
            } else if (seconds <= GameConstants.WARNING_TIME_THRESHOLD_SECONDS) {
                timerLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
            } else {
                timerLabel.setTextFill(javafx.scene.paint.Color.web("#ff6b35"));
            }
        }
    }

    /**
     * Shows the timer label.
     */
    public void showTimer() {
        if (timerLabel != null) {
            timerLabel.setVisible(true);
        }
    }

    /**
     * Hides the timer label.
     */
    public void hideTimer() {
        if (timerLabel != null) {
            timerLabel.setVisible(false);
        }
    }

    // Leaderboard

    /**
     * Shows the leaderboard (defaults to Classic mode).
     */
    public void showLeaderboard() {
        showClassicLeaderboard();
    }

    /**
     * Closes the leaderboard display.
     */
    public void closeLeaderboard() {
        viewManager.hideLeaderboard();
        gamePanel.requestFocus();
    }

    /**
     * Shows the Classic mode leaderboard.
     */
    @FXML
    public void showClassicLeaderboard() {
        if (gameController != null) {
            List<Integer> scores = gameController.getLeaderboardScores(
                    GameMode.CLASSIC, GameConstants.MAX_LEADERBOARD_ENTRIES);
            leaderboardView.displayLeaderboard("CLASSIC LEADERBOARD", scores);
        }
        viewManager.showLeaderboard();
    }

    /**
     * Shows the Time Attack mode leaderboard.
     */
    @FXML
    public void showTimeAttackLeaderboard() {
        if (gameController != null) {
            List<Integer> scores = gameController.getLeaderboardScores(
                    GameMode.TIME_ATTACK, GameConstants.MAX_LEADERBOARD_ENTRIES);
            leaderboardView.displayLeaderboard("TIME ATTACK LEADERBOARD", scores);
        }
        viewManager.showLeaderboard();
    }

    /**
     * Shows the game over screen with final score.
     *
     * @param score the final score
     * @param mode  the game mode name
     */
    public void showGameOver(int score, String mode) {
        String gameOverText = mode + " Mode\nFinal Score: " + score;
        groupNotification.setVisible(true);
    }
}


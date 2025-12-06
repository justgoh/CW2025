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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main controller for the Tetris game GUI.
 * <p>
 * This class manages the user interface, handles user input, and coordinates
 * between the view components and game logic. It serves as the central hub for
 * all UI-related operations including menus, gameplay display, and theme management.
 * The controller implements JavaFX's Initializable interface to set up the UI
 * after FXML components are loaded.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Initialize and manage UI components</li>
 *   <li>Handle user input and button actions</li>
 *   <li>Coordinate with helper classes for rendering and state management</li>
 *   <li>Update displays based on game events</li>
 *   <li>Manage game modes (Classic and Time Attack)</li>
 * </ul>
 */
public class GuiController implements Initializable {

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
    private VBox leaderMenu;
    @FXML
    private VBox leaderboardList;
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
    private VBox leftSidebar;
    @FXML
    private VBox rightSidebar;
    @FXML
    private VBox howToPlayMenu;
    @FXML
    private Button startGameButton;
    @FXML
    private VBox themesMenu;
    @FXML
    private VBox gameOverPanel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label modeIndicatorLabel;
    @FXML
    public Button leaderboardButton;

    private ViewManager viewManager;

    private PieceRenderer pieceRenderer;

    private ThemeManager themeManager;

    private LeaderboardView leaderboardView;

    private GameTimeline gameTimeline;

    private GameInputHandler inputHandler;

    private InputEventListener eventListener;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private GameController gameController;

    private GameMode modeToStart = GameMode.CLASSIC;

    private Rectangle[][] displayMatrix;

    private Rectangle[][] rectangles;

    private Rectangle[][] ghostRectangles;

    private Rectangle[][][] nextPieceRectangles;

    private Rectangle[][] holdPieceRectangles;

    /**
     * Initializes the controller after FXML components are loaded.
     * <p>
     * Sets up the initial UI state, creates helper objects (ViewManager, PieceRenderer,
     * ThemeManager, LeaderboardView), loads high scores, configures input handlers,
     * and initializes all preview panels. This method is automatically called by
     * JavaFX after the FXML file has been loaded.
     *
     * @param location  the location used to resolve relative paths for the root object, or null if unknown
     * @param resources the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        viewManager = new ViewManager(
                homeMenu, howToPlayMenu, themesMenu,
                gameBoard, brickPanel, leftSidebar, rightSidebar,
                pauseMenu, gameOverPanel, groupNotification, leaderMenu,
                timerLabel
        );

        pieceRenderer = new PieceRenderer();
        themeManager = new ThemeManager();
        leaderboardView = new LeaderboardView(leaderboardList);

        viewManager.showHome();

        if (modeIndicatorLabel != null) {
            updateModeIndicator("Classic");
        }

        URL fontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (fontUrl != null) {
            Font.loadFont(fontUrl.toExternalForm(), 38);
        } else {
            System.err.println("Warning: digital.ttf font file not found, using default font");
        }

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::handleKeyPress);

        HighScoreManager hsm = new HighScoreManager();
        int highscore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + highscore);

        final Reflection reflection = new Reflection();
        reflection.setFraction(AnimationConfig.REFLECTION_FRACTION);
        reflection.setTopOpacity(AnimationConfig.REFLECTION_TOP_OPACITY);
        reflection.setTopOffset(AnimationConfig.REFLECTION_TOP_OFFSET);

        initializeNextPiecePreview();
        initializeHoldPiecePanel();
        setupHoldKeyBinding();
        initializeHowToPlayBackground();
    }

    /**
     * Sets the GameController for this controller.
     * <p>
     * Establishes the connection to the game logic controller and updates
     * the mode indicator to reflect the current game mode.
     *
     * @param gameController the GameController instance managing game logic and state
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
     * <p>
     * Establishes the connection between UI input events and game logic
     * by providing the listener that will process move, rotate, and other game events.
     *
     * @param eventListener the InputEventListener to handle game logic callbacks
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Initializes the game view with the board matrix and initial piece.
     * <p>
     * Sets up the display grid by creating Rectangle objects for each board cell,
     * configures game panel dimensions, and creates initial rectangles for the current
     * piece and its ghost preview. Also initializes and starts the game timeline for
     * automatic piece dropping.
     *
     * @param boardMatrix the 2D array representing the game board state
     * @param brick       the ViewData containing the initial piece information
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {

        gamePanel.getChildren().clear();
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];

        for (int row = GameConstants.INVISIBLE_ROWS; row < boardMatrix.length; row++) {
            for (int column = 0; column < boardMatrix[row].length; column++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[row][column] = rectangle;
                gamePanel.add(rectangle, column, row - GameConstants.INVISIBLE_ROWS);
            }
        }
        brickPanel.getChildren().clear();

        int panelWidth = boardMatrix[0].length * GameConstants.BRICK_SIZE;
        int panelHeight = (boardMatrix.length - GameConstants.INVISIBLE_ROWS) * GameConstants.BRICK_SIZE;

        gamePanel.setMinSize(panelWidth, panelHeight);
        gamePanel.setMaxSize(panelWidth, panelHeight);
        gamePanel.setPrefSize(panelWidth, panelHeight);

        Rectangle clip = new Rectangle();
        clip.setWidth(panelWidth);
        clip.setHeight(panelHeight);
        gamePanel.setClip(clip);

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {

                Rectangle ghost = pieceRenderer.createGhostBrick();
                ghostRectangles[row][column] = ghost;
                gamePanel.getChildren().add(ghost);

                Rectangle rectangle = pieceRenderer.createStyledBrick(brick.getBrickData()[row][column]);
                rectangle.setManaged(false);
                rectangles[row][column] = rectangle;
                gamePanel.getChildren().add(rectangle);
            }
        }

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

        gameTimeline = new GameTimeline(() -> {
            if (!isGameOver.getValue() && !isPause.getValue()) {
                moveDown(new MoveEvent(EventSource.THREAD));
            }
        });
        gameTimeline.start();
    }

    /**
     * Refreshes the brick display after a move.
     * <p>
     * Updates the piece rectangles if the piece shape has changed (such as after
     * rotation), updates the position of all piece blocks, updates the ghost piece
     * preview, and refreshes the next piece previews from the generator.
     *
     * @param brick the ViewData containing updated information for the current piece
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
     * Recreates piece rectangles when the piece shape changes.
     * <p>
     * Removes old Rectangle objects and creates new ones with the correct dimensions
     * to match the new piece shape. This is necessary after rotation when the
     * bounding box dimensions change (e.g., I-piece rotating from 1x4 to 4x1).
     *
     * @param brick the ViewData containing the new piece shape and dimensions
     */
    private void recreatePieceRectangles(ViewData brick) {
        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                gamePanel.getChildren().remove(rectangles[row][column]);
                gamePanel.getChildren().remove(ghostRectangles[row][column]);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

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

        for (Rectangle[] ghostRow : ghostRectangles) {
            for (Rectangle ghost : ghostRow) {
                gamePanel.getChildren().add(ghost);
            }
        }

        for (Rectangle[] rectRow : rectangles) {
            for (Rectangle rectangle : rectRow) {
                gamePanel.getChildren().add(rectangle);
            }
        }
    }

    /**
     * Updates the position of the current piece rectangles.
     * <p>
     * Sets visibility, color, and translation for each rectangle based on the piece
     * data and position. Hides blocks that are in the invisible rows at the top of
     * the board or that are empty (0 value) in the piece data.
     *
     * @param brick the ViewData containing piece position and block data
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
     * <p>
     * Calculates where the current piece would land if hard dropped by testing
     * positions downward until a collision is detected. Positions the semi-transparent
     * ghost piece rectangles at this landing position to provide visual guidance.
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
     * <p>
     * Updates the display matrix rectangles to show locked pieces that have
     * been placed on the board. Called after pieces are locked in place.
     *
     * @param board the 2D array representing the current game board state
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

    /**
     * Handles key press events.
     * <p>
     * Creates an input handler if one doesn't exist and delegates the key
     * event to it for processing. The input handler routes keys to appropriate
     * game actions.
     *
     * @param keyEvent the KeyEvent triggered by user keyboard input
     */
    private void handleKeyPress(KeyEvent keyEvent) {
        if (inputHandler == null) {
            createInputHandler();
        }
        inputHandler.handleKeyPress(keyEvent);
    }

    /**
     * Creates and configures the input handler with all necessary callbacks.
     * <p>
     * Sets up the GameInputHandler with references to the event listener and
     * game state properties, then configures all action callbacks for pause,
     * new game, hard drop, hold, move down, and refresh operations.
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
     * <p>
     * Handles the logic for moving a piece down, including checking for line clears,
     * displaying score notifications when lines are cleared, and refreshing the display.
     * Returns early if the game is over, paused, or no timeline exists.
     *
     * @param event the MoveEvent containing movement details and source information
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
     * <p>
     * Instantly drops the piece to its landing position and locks it in place.
     * Displays score notifications if any lines are cleared as a result, and
     * refreshes the display with the new board state.
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


    /**
     * Initializes the next piece preview panels.
     * <p>
     * Creates 4x4 grids of Rectangle objects in each of the three preview panels
     * to display upcoming pieces. Uses PieceRenderer to set up the panels.
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
     * <p>
     * Renders each upcoming piece in its corresponding preview panel. If fewer
     * pieces are available than preview slots, remaining panels are cleared.
     *
     * @param nextBrickDataList the list of 2D arrays representing upcoming pieces
     */
    public void updateNextPieces(List<int[][]> nextBrickDataList) {
        for (int i = 0; i < GameConstants.NEXT_PIECE_PREVIEW_COUNT; i++) {
            if (i < nextBrickDataList.size()) {
                pieceRenderer.renderPieceOnPreview(
                        nextBrickDataList.get(i),
                        nextPieceRectangles[i]
                );
            } else {
                pieceRenderer.clearPreviewPanel(nextPieceRectangles[i]);
            }
        }
    }

    /**
     * Updates the next piece previews from the piece generator.
     * <p>
     * Queries the event listener for the next pieces in the queue and updates
     * the preview panels accordingly. Called after piece movements to keep
     * previews current.
     */
    private void updateNextPiecesFromGenerator() {
        if (eventListener != null) {
            List<int[][]> nextPieces = eventListener.getNextPieces(GameConstants.NEXT_PIECE_PREVIEW_COUNT);
            updateNextPieces(nextPieces);
        }
    }

    /**
     * Initializes the hold piece panel.
     * <p>
     * Creates a 4x4 grid of Rectangle objects for displaying the currently
     * held piece. Uses PieceRenderer to set up the panel structure.
     */
    private void initializeHoldPiecePanel() {
        holdPieceRectangles = pieceRenderer.initializePreviewPanel(holdPiecePanel);
    }

    /**
     * Sets up key binding for the hold piece feature.
     * <p>
     * Configures keyboard event handlers to trigger the hold action when
     * C or SHIFT keys are pressed during active gameplay.
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
     * <p>
     * Swaps the current piece with the held piece, or stores the current piece
     * if no piece is currently held. Updates the hold display panel and refreshes
     * the current piece display. Returns early if the game is over or paused.
     *
     * @param actionEvent the ActionEvent from button click, or null if triggered by keyboard
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
     * <p>
     * Renders the specified piece data in the hold panel, or clears the panel
     * if null is provided (indicating no piece is currently held).
     *
     * @param holdPieceData the 2D array representing the held piece, or null if empty
     */
    public void updateHoldPieceDisplay(int[][] holdPieceData) {
        pieceRenderer.renderPieceOnPreview(holdPieceData, holdPieceRectangles);
    }

    /**
     * Sets whether the hold feature is enabled or disabled.
     * <p>
     * Updates the hold button styling to visually indicate whether the hold
     * action is currently available or has already been used for this piece.
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

    /**
     * Binds the score label to a score property.
     * <p>
     * Establishes a binding so the score label automatically updates whenever
     * the score property changes. Unbinds any previous binding first.
     *
     * @param scoreProperty the IntegerProperty to bind to the score label
     */
    public void bindScore(IntegerProperty scoreProperty) {
        scoreLabel.textProperty().unbind();
        scoreLabel.setText(null);
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    /**
     * Handles the game over state.
     * <p>
     * Stops the game timeline, displays the game over screen, sets the game over
     * flag, and updates the high score label if a new high score was achieved.
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
            int highScore = gameController.getLeaderboardScores(currentMode, 1).isEmpty() ?
                    0 : gameController.getLeaderboardScores(currentMode, 1).getFirst();
            highScoreLabel.setText("High Score: " + highScore);
        }
    }

    /**
     * Starts a new game.
     * <p>
     * Resets game state flags, hides game over and pause menus, updates the high
     * score label, creates a fresh game through the event listener, initializes
     * a new game timeline with appropriate speed for the current mode, and starts
     * automatic piece dropping.
     *
     * @param actionEvent the ActionEvent from button click, or null if triggered programmatically
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
                    0 : gameController.getLeaderboardScores(currentMode, 1).getFirst();
            highScoreLabel.setText("High Score: " + highScore);
        }

        eventListener.createNewGame();

        int speed = 500;
        if (gameController != null && gameController.getCurrentGameMode() == GameMode.TIME_ATTACK) {
            speed = 300;
        }
        gameTimeline = new GameTimeline(() -> {
            if (!isGameOver.getValue() && !isPause.getValue()) {
                moveDown(new MoveEvent(EventSource.THREAD));
            }
        }, speed
        );
        gameTimeline.start();

        updateNextPiecesFromGenerator();
        gamePanel.requestFocus();
    }

    /**
     * Updates the high score label.
     * <p>
     * Sets the text of the high score label to display the provided value.
     *
     * @param highscore the new high score value to display
     */
    public void updateHighScoreLabel(int highscore) {
        if (highScoreLabel != null) {
            highScoreLabel.setText("High Score: " + highscore);
        }
    }

    /**
     * Toggles the pause state of the game.
     * <p>
     * If the game is currently paused, resumes gameplay by hiding the pause menu,
     * resuming the game controller, and restarting the timeline. If not paused,
     * pauses the game by showing the pause menu and stopping the timeline.
     * Has no effect if the game is over.
     *
     * @param actionEvent the ActionEvent from button click, or null if triggered by keyboard
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
     * Pauses the game.
     * <p>
     * External method to pause the game by stopping the timeline and pausing
     * the game controller. Used when pause needs to be triggered programmatically
     * rather than through user input.
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
     * Restarts the current game.
     * <p>
     * Delegates to newGame to reset and start a fresh game session.
     *
     * @param event the ActionEvent from the restart button
     */
    public void restartGame(ActionEvent event) {
        newGame(event);
    }

    /**
     * Quits the application.
     * <p>
     * Terminates the application immediately by calling System.exit.
     *
     * @param actionEvent the ActionEvent from the quit button
     */
    public void quitGame(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Returns to the main menu from any screen.
     * <p>
     * Stops the game timeline, pauses the game controller, resets game state flags,
     * hides all game screens and menus, and shows the home menu.
     *
     * @param actionEvent the ActionEvent from the back to menu button
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
     * Internal method to show How to Play screen.
     * <p>
     * Updates the start button text and styling based on the selected game mode
     * (Classic or Time Attack) to provide clear indication of which mode will start.
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
     * <p>
     * Stops any existing timeline, hides the how to play menu, shows the game board,
     * starts the selected game mode (Classic or Time Attack), creates a new game
     * timeline with appropriate speed, and resets game state flags.
     *
     * @param actionEvent the ActionEvent from the start game button
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
                moveDown(new MoveEvent(EventSource.THREAD));
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
     * <p>
     * Delegates to the main backToMainMenu method.
     *
     * @param actionEvent the ActionEvent from the back button
     */
    @FXML
    public void backToMainMenuFromHowToPlay(ActionEvent actionEvent) {
        backToMainMenu(actionEvent);
    }

    /**
     * Shows the theme selection menu.
     * <p>
     * Uses the ViewManager to display the themes screen where users can
     * choose different visual themes for the application.
     *
     * @param actionEvent the ActionEvent from the themes button
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
     * <p>
     * Uses the ThemeManager to apply the default visual theme to the root pane
     * and menus, then returns to the main menu.
     *
     * @param actionEvent the ActionEvent from the theme button
     */
    @FXML
    public void setDefaultTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.DEFAULT, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Applies the countryside theme.
     * <p>
     * Uses the ThemeManager to apply the countryside visual theme to the root pane
     * and menus, then returns to the main menu.
     *
     * @param actionEvent the ActionEvent from the theme button
     */
    @FXML
    public void setCountrysideTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.COUNTRYSIDE, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Applies the beach theme.
     * <p>
     * Uses the ThemeManager to apply the beach visual theme to the root pane
     * and menus, then returns to the main menu.
     *
     * @param actionEvent the ActionEvent from the theme button
     */
    @FXML
    public void setBeachTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.BEACH, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Applies the tron theme.
     * <p>
     * Uses the ThemeManager to apply the tron visual theme to the root pane
     * and menus, then returns to the main menu.
     *
     * @param actionEvent the ActionEvent from the theme button
     */
    @FXML
    public void setTronTheme(ActionEvent actionEvent) {
        Pane rootPane = (Pane) homeMenu.getParent();
        themeManager.applyTheme(Theme.TRON, rootPane, homeMenu, themesMenu);
        backToMainMenuFromThemes(null);
    }

    /**
     * Initializes the How to Play background image.
     * <p>
     * Loads the howtoplay.png image and sets it as the background for the
     * how to play menu with appropriate sizing and positioning. Logs an
     * error if the image fails to load.
     */
    private void initializeHowToPlayBackground() {
        try {
            var imageStream = getClass().getResourceAsStream("/howtoplay.png");
            if (imageStream != null) {
                javafx.scene.image.Image howToPlayBg = new javafx.scene.image.Image(imageStream);
                BackgroundImage backgroundImage = new BackgroundImage(
                        howToPlayBg,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
                );
                howToPlayMenu.setBackground(new Background(backgroundImage));
            } else {
                System.err.println("Warning: howtoplay.png image file not found");
            }
        } catch (Exception e) {
            System.out.println("Error loading How To Play background: " + e.getMessage());
        }
    }

    /**
     * Prepares to start Classic mode.
     * <p>
     * Sets the internal mode flag to Classic and displays the How to Play screen
     * with Classic mode styling and instructions.
     */
    @FXML
    public void showHowToPlayForClassic() {
        modeToStart = GameMode.CLASSIC;
        showHowToPlayInternal();
        viewManager.showHowToPlay();
    }

    /**
     * Prepares to start Time Attack mode.
     * <p>
     * Sets the internal mode flag to Time Attack and displays the How to Play screen
     * with Time Attack mode styling and instructions.
     */
    @FXML
    public void showHowToPlayForTimeAttack() {
        modeToStart = GameMode.TIME_ATTACK;
        showHowToPlayInternal();
        viewManager.showHowToPlay();
    }

    /**
     * Updates the mode indicator label.
     * <p>
     * Sets the text and color of the mode indicator based on the current game mode.
     * Time Attack mode displays in orange, Classic mode in teal.
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


    /**
     * Updates the timer display.
     * <p>
     * Formats and displays the remaining time in MM:SS format. Changes the color
     * to red if time is critical, orange if low, or orange if normal.
     *
     * @param seconds the remaining seconds in Time Attack mode
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
     * <p>
     * Makes the timer label visible for Time Attack mode.
     */
    public void showTimer() {
        if (timerLabel != null) {
            timerLabel.setVisible(true);
        }
    }

    /**
     * Hides the timer label.
     * <p>
     * Hides the timer label for Classic mode or when not needed.
     */
    public void hideTimer() {
        if (timerLabel != null) {
            timerLabel.setVisible(false);
        }
    }

    /**
     * Shows the leaderboard.
     * <p>
     * Defaults to displaying the Classic mode leaderboard.
     */
    public void showLeaderboard() {
        showClassicLeaderboard();
    }

    /**
     * Closes the leaderboard display.
     * <p>
     * Hides the leaderboard view and returns focus to the game panel.
     */
    public void closeLeaderboard() {
        viewManager.hideLeaderboard();
        gamePanel.requestFocus();
    }

    /**
     * Shows the Classic mode leaderboard.
     * <p>
     * Retrieves top scores for Classic mode from the game controller and
     * displays them in the leaderboard view.
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
     * <p>
     * Retrieves top scores for Time Attack mode from the game controller and
     * displays them in the leaderboard view.
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
     * <p>
     * Displays the game mode and final score in the notification area.
     *
     * @param score the final score achieved
     * @param mode  the game mode name ("Classic" or "Time Attack")
     */
    public void showGameOver(int score, String mode) {
        String gameOverText = mode + " Mode\nFinal Score: " + score;
        groupNotification.setVisible(true);
    }
}
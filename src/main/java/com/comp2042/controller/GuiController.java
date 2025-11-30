package com.comp2042.controller;

import com.comp2042.model.HighScoreManager;
import com.comp2042.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    public Button leaderboardButton;

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

    private InputEventListener eventListener;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private Rectangle[][] displayMatrix;

    private Rectangle[][] rectangles;

    private Rectangle[][] ghostRectangles;

    private Rectangle[][][] nextPieceRectangles;

    private Rectangle[][] holdPieceRectangles;

    private String currentTheme = "default";

    private Timeline countdownTimeline;

    private int remainingSeconds = 180;

    private boolean isTimeAttackMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (homeMenu != null) homeMenu.setVisible(true);
        if (howToPlayMenu != null) howToPlayMenu.setVisible(false);
        if (themesMenu != null) themesMenu.setVisible(false);
        if (gameBoard != null) gameBoard.setVisible(false);
        if (leftSidebar != null) leftSidebar.setVisible(false);
        if (rightSidebar != null) rightSidebar.setVisible(false);
        if (brickPanel != null) brickPanel.setVisible(false);
        if (pauseMenu != null) pauseMenu.setVisible(false);
        if (leaderMenu != null) leaderMenu.setVisible(false);
        if (groupNotification != null) groupNotification.setVisible(false);
        if (gameOverPanel != null) gameOverPanel.setVisible(false);

        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isGameOver.getValue()) return;
                if (keyEvent.getCode() == KeyCode.P) {
                    isPause.setValue(!isPause.getValue());
                    pauseMenu.setVisible(isPause.getValue());
                    keyEvent.consume();
                    return;
                }
                if (!isPause.getValue()) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop();
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                    keyEvent.consume();
                }
            }
        });

        HighScoreManager hsm = new HighScoreManager();
        int highscore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + highscore);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
        initializeNextPiecePreview();
        initializeHoldPiecePanel();
        setupHoldKeyBinding();
        initializeHowToPlayBackground();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        gamePanel.getChildren().clear();

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int row = 2; row < boardMatrix.length; row++) {
            for (int column = 0; column < boardMatrix[row].length; column++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[row][column] = rectangle;
                gamePanel.add(rectangle, column, row - 2);
            }
        }
        brickPanel.getChildren().clear();

        gamePanel.setMinSize(boardMatrix[0].length * BRICK_SIZE, (boardMatrix.length - 2) * BRICK_SIZE);
        gamePanel.setMaxSize(boardMatrix[0].length * BRICK_SIZE, (boardMatrix.length - 2) * BRICK_SIZE);
        gamePanel.setPrefSize(boardMatrix[0].length * BRICK_SIZE, (boardMatrix.length - 2) * BRICK_SIZE);

        Rectangle clip = new Rectangle();
        clip.setWidth(boardMatrix[0].length * BRICK_SIZE);
        clip.setHeight((boardMatrix.length - 2) * BRICK_SIZE);
        gamePanel.setClip(clip);
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {

                Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghost.setFill(Color.LIGHTGRAY);
                ghost.setStroke(Color.LIGHTGRAY);
                ghost.setStrokeWidth(1.5);
                ghost.setOpacity(0.5);
                ghostRectangles[row][column] = ghost;
                gamePanel.getChildren().add(ghost);

                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[row][column]));
                rectangles[row][column] = rectangle;
                gamePanel.getChildren().add(rectangle);
            }
        }

        gamePanel.setPadding(Insets.EMPTY);
        gamePanel.setHgap(0);
        gamePanel.setVgap(0);

        brickPanel.setPadding(Insets.EMPTY);
        brickPanel.setHgap(0);
        brickPanel.setVgap(0);
        brickPanel.setVisible(false);
        brickPanel.toFront();

        updateBrickPosition(brick);
        updateGhost(brick);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int colorCode) {
        Paint fillColor;
        switch (colorCode) {
            case 0:
                fillColor = Color.TRANSPARENT;
                break;
            case 1:
                fillColor = Color.AQUA;
                break;
            case 2:
                fillColor = Color.BLUEVIOLET;
                break;
            case 3:
                fillColor = Color.DARKGREEN;
                break;
            case 4:
                fillColor = Color.YELLOW;
                break;
            case 5:
                fillColor = Color.RED;
                break;
            case 6:
                fillColor = Color.BEIGE;
                break;
            case 7:
                fillColor = Color.BURLYWOOD;
                break;
            default:
                fillColor = Color.WHITE;
                break;
        }
        return fillColor;
    }


    public void refreshBrick(ViewData brick) {
        if (!isPause.getValue()) {
            if (rectangles.length != brick.getBrickData().length ||
                    rectangles[0].length != brick.getBrickData()[0].length) {
                for (int row = 0; row < rectangles.length; row++) {
                    for (int column = 0; column < rectangles[row].length; column++) {
                        gamePanel.getChildren().remove(rectangles[row][column]);
                        gamePanel.getChildren().remove(ghostRectangles[row][column]);
                    }
                }
                rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
                ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

                for (int row = 0; row < brick.getBrickData().length; row++) {
                    for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                        Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        ghost.setFill(Color.TRANSPARENT);
                        ghost.setStroke(Color.GRAY);
                        ghost.setStrokeWidth(1.0);
                        ghost.setOpacity(0.5);
                        ghost.setArcHeight(9);
                        ghost.setArcWidth(9);
                        ghost.setVisible(false);
                        ghost.setManaged(false);
                        ghostRectangles[row][column] = ghost;

                        Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        rectangle.setArcHeight(9);
                        rectangle.setArcWidth(9);
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
                    for (Rectangle rect : rectRow) {
                        gamePanel.getChildren().add(rect);
                    }
                }
            }
            updateBrickPosition(brick);
            updateGhost(brick);
            updateNextPiecesFromGenerator();
        }
    }

    private void updateBrickPosition(ViewData brick) {
        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                Rectangle rectangle = rectangles[row][column];
                int displayRow = brick.getyPosition() + row - 2;

                if (brick.getBrickData()[row][column] != 0 && displayRow >= 0) {
                    rectangle.setVisible(true);
                    rectangle.setFill(getFillColor(brick.getBrickData()[row][column]));
                    rectangle.setTranslateX((brick.getxPosition() + column) * BRICK_SIZE);
                    rectangle.setTranslateY(displayRow * BRICK_SIZE);

                    rectangle.setStroke(Color.BLACK);
                    rectangle.setStrokeWidth(0.3);
                    rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
                } else {
                    rectangle.setVisible(false);
                }
            }
        }
    }

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
                int displayRow = ghostY + row - 2;

                if (brick.getBrickData()[row][column] != 0 && displayRow >= 0) {
                    ghost.setVisible(true);
                    ghost.setTranslateX((brick.getxPosition() + column) * BRICK_SIZE);
                    ghost.setTranslateY(displayRow * BRICK_SIZE);
                    ghost.setFill(Color.TRANSPARENT);
                    ghost.setStroke(Color.GRAY);
                    ghost.setStrokeWidth(1.5);
                    ghost.setOpacity(0.4);
                } else {
                    ghost.setVisible(false);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int row = 2; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                setRectangleData(board[row][column], displayMatrix[row][column]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        if (color != 0) {
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(0.3);
            rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        } else {
            rectangle.setStroke(Color.TRANSPARENT);
            rectangle.setStrokeWidth(0);
        }
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
                groupNotification.setVisible(true);

            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        scoreLabel.textProperty().unbind();
        scoreLabel.setText(null);
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    public void gameOver() {
        timeLine.stop();
        if (groupNotification != null && gameOverPanel != null) {
            groupNotification.setVisible(true);
            gameOverPanel.setVisible(true);
        }
        isGameOver.setValue(Boolean.TRUE);

        int finalScore = Integer.parseInt(scoreLabel.getText().replace("Score: ", ""));
        HighScoreManager hsm = new HighScoreManager();
        int prevHighScore = hsm.loadHighScore();

        hsm.addScore(finalScore);

        int currentHighScore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + currentHighScore);
    }

    public void newGame(ActionEvent actionEvent) {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        if (!isTimeAttackMode && timerLabel != null) {
            timerLabel.setVisible(false);
        }

        if (homeMenu != null) {
            homeMenu.setVisible(false);
        }
        if (groupNotification != null) {
            groupNotification.setVisible(false);
        }
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
        timeLine.stop();
        HighScoreManager hsm = new HighScoreManager();
        int highscore = hsm.loadHighScore();
        highScoreLabel.setText(("High Score: " + highscore));
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        pauseMenu.setVisible(false);
        updateNextPiecesFromGenerator();
    }

    public void updateHighScoreLabel(int highscore) {
        highScoreLabel.setText("High Score: " + highscore);
    }

    public void pauseGame(ActionEvent actionEvent) {
        isPause.setValue(!isPause.getValue());
        pauseMenu.setVisible(isPause.getValue());

        if (isTimeAttackMode && countdownTimeline != null) {
            if (isPause.getValue()) {
                countdownTimeline.pause();
            } else {
                countdownTimeline.play();
            }
        }

        gamePanel.requestFocus();
    }

    public void restartGame(ActionEvent actionEvent) {
        newGame(null);
    }

    public void quitGame(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void showLeaderboard() {
        leaderboardList.getChildren().clear();

        HighScoreManager hsm = new HighScoreManager();
        List<Integer> topScores = hsm.loadScores();

        for (int i = 0; i < topScores.size(); i++) {
            Label s = new Label((i + 1) + "." + topScores.get(i));
            s.setTextFill(Color.WHITE);
            s.setStyle("-fx-font-size: 18px;");
            leaderboardList.getChildren().add(s);
        }
        leaderMenu.toFront();
        leaderMenu.setVisible(true);
    }

    public void closeLeaderboard() {
        leaderMenu.setVisible(false);
        gamePanel.requestFocus();
    }

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

    private void initializeNextPiecePreview() {
        nextPieceRectangles = new Rectangle[3][][];

        GridPane[] previewPanels = {nextPiecePanel1, nextPiecePanel2, nextPiecePanel3};
        for (int panelIndex = 0; panelIndex < previewPanels.length; panelIndex++) {
            GridPane panel = previewPanels[panelIndex];
            panel.getChildren().clear();

            Rectangle[][] panelRectangles = new Rectangle[4][4];
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setArcWidth(5);
                    rectangle.setArcHeight(5);
                    panelRectangles[row][column] = rectangle;
                    panel.add(rectangle, column, row);
                }
            }
            nextPieceRectangles[panelIndex] = panelRectangles;
            panel.setHgap(1);
            panel.setVgap(1);
            panel.setPadding(new Insets(5));
        }
    }

    public void updateNextPieces(List<int[][]> nextBrickDataList) {
        for (int i = 0; i < 3; i++) {
            if (i < nextBrickDataList.size()) {
                updateNextPiecePreview(i, nextBrickDataList.get(i));
            } else {
                clearNextPiecePreview(i);
            }
        }
    }

    private void updateNextPiecePreview(int previewIndex, int[][] brickData) {
        Rectangle[][] previewRectangles = nextPieceRectangles[previewIndex];

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                previewRectangles[row][column].setFill(Color.TRANSPARENT);
            }
        }
        int startRow = (4 - brickData.length) / 2;
        int startColumn = (4 - brickData[0].length) / 2;

        for (int row = 0; row < brickData.length; row++) {
            for (int column = 0; column < brickData[row].length; column++) {
                if (brickData[row][column] != 0) {
                    int previewRow = startRow + row;
                    int previewColumn = startColumn + column;
                    if (previewRow >= 0 && previewRow < 4 && previewColumn >= 0 && previewColumn < 4) {
                        previewRectangles[previewRow][previewColumn].setFill(getFillColor(brickData[row][column]));
                    }
                }
            }
        }
    }

    private void clearNextPiecePreview(int previewIndex) {
        Rectangle[][] previewRectangles = nextPieceRectangles[previewIndex];
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                previewRectangles[row][column].setFill(Color.TRANSPARENT);
            }
        }
    }

    private void updateNextPiecesFromGenerator() {
        if (eventListener != null) {
            List<int[][]> nextPieces = eventListener.getNextPieces(3);
            updateNextPieces(nextPieces);
        }
    }

    private void initializeHoldPiecePanel() {
        holdPiecePanel.getChildren().clear();
        holdPieceRectangles = new Rectangle[4][4];

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcWidth(5);
                rectangle.setArcHeight(5);
                holdPieceRectangles[row][column] = rectangle;
                holdPiecePanel.add(rectangle, column, row);
            }
        }
        holdPiecePanel.setHgap(1);
        holdPiecePanel.setVgap(1);
        holdPiecePanel.setPadding(new Insets(5));
    }

    private void setupHoldKeyBinding() {
        gamePanel.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (isGameOver.getValue() || isPause.getValue()) return;
            if (keyEvent.getCode() == KeyCode.C || keyEvent.getCode() == KeyCode.SHIFT) {
                holdPiece(null);
                keyEvent.consume();
            }
        });
    }

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

    public void updateHoldPieceDisplay(int[][] holdPieceData) {
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                holdPieceRectangles[row][column].setFill(Color.TRANSPARENT);
            }
        }
        if (holdPieceData != null) {
            int startRow = (4 - holdPieceData.length) / 2;
            int startColumn = (4 - holdPieceData[0].length) / 2;

            for (int row = 0; row < holdPieceData.length; row++) {
                for (int column = 0; column < holdPieceData[row].length; column++) {
                    if (holdPieceData[row][column] != 0) {
                        int displayRow = startRow + row;
                        int displayColumn = startColumn + column;
                        if (displayRow >= 0 && displayRow < 4 && displayColumn >= 0 && displayColumn < 4) {
                            holdPieceRectangles[displayRow][displayColumn].setFill(getFillColor(holdPieceData[row][column]));
                        }
                    }
                }
            }
        }
    }

    public void setHoldEnabled(boolean enabled) {
        if (enabled) {
            holdButton.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white;");
            holdButton.setText("Hold (C)");
        } else {
            holdButton.setStyle("-fx-background-color: #666666; -fx-text-fill: #999999;");
            holdButton.setText("Hold Used");
        }
    }

    @FXML
    public void startGame(ActionEvent actionEvent) {
        if (homeMenu != null) homeMenu.setVisible(false);
        if (gameBoard != null) gameBoard.setVisible(true);
        if (leftSidebar != null) leftSidebar.setVisible(true);
        if (rightSidebar != null) rightSidebar.setVisible(true);
        if (brickPanel != null) brickPanel.setVisible(true);
        newGame(actionEvent);
        gamePanel.requestFocus();
    }

    @FXML
    public void backToMainMenu(ActionEvent actionEvent) {
        if (timeLine != null) {
            timeLine.stop();
        }
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }
        isTimeAttackMode = false;
        remainingSeconds = 180;

        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        if (gameBoard != null) gameBoard.setVisible(false);
        if (leftSidebar != null) leftSidebar.setVisible(false);
        if (rightSidebar != null) rightSidebar.setVisible(false);
        if (brickPanel != null) brickPanel.setVisible(false);
        if (pauseMenu != null) pauseMenu.setVisible(false);
        if (leaderMenu != null) leaderMenu.setVisible(false);
        if (groupNotification != null) groupNotification.setVisible(false);
        if (homeMenu != null) homeMenu.setVisible(true);

        HighScoreManager hsm = new HighScoreManager();
        int highscore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + highscore);
        if (timerLabel != null) timerLabel.setVisible(false);
    }

    @FXML
    public void showHowToPlay(ActionEvent actionEvent) {
        if (homeMenu != null) homeMenu.setVisible(false);
        if (howToPlayMenu != null) howToPlayMenu.setVisible(true);
    }

    @FXML
    public void startGameFromHowToPlay(ActionEvent actionEvent) {
        if (howToPlayMenu != null) howToPlayMenu.setVisible(false);
        startGame(actionEvent);
    }

    @FXML
    public void backToMainMenuFromHowToPlay(ActionEvent actionEvent) {
        if (howToPlayMenu != null) howToPlayMenu.setVisible(false);
        if (homeMenu != null) homeMenu.setVisible(true);
    }

    @FXML
    public void showThemes(ActionEvent actionEvent) {
        if (homeMenu != null) homeMenu.setVisible(false);
        if (themesMenu != null) themesMenu.setVisible(true);
    }

    @FXML
    public void backToMainMenuFromThemes(ActionEvent actionEvent) {
        if (themesMenu != null) themesMenu.setVisible(false);
        if (homeMenu != null) homeMenu.setVisible(true);
    }

    @FXML
    public void setDefaultTheme(ActionEvent actionEvent) {
        applyGradientTheme("-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);");
        currentTheme = "default";
    }

    @FXML
    public void setCountrysideTheme(ActionEvent actionEvent) {
        applyImageTheme("countryside_bg.png");
        currentTheme = "countryside";
    }

    @FXML
    public void setBeachTheme(ActionEvent actionEvent) {
        applyImageTheme("beach_bg.png");
        currentTheme = "beach";
    }

    @FXML
    public void setTronTheme(ActionEvent actionEvent) {
        applyImageTheme("tron_bg.png");
        currentTheme = "tron";
    }

    private void applyImageTheme(String imagePath) {
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/" + imagePath));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );

            Background background = new Background(backgroundImage);

            Pane rootPane = (Pane) homeMenu.getParent();
            if (rootPane != null) {
                rootPane.setBackground(background);
            }

            if (homeMenu != null) homeMenu.setBackground(background);
            if (themesMenu != null) themesMenu.setBackground(background);

        } catch (Exception e) {
            System.out.println("Error loading theme image: " + e.getMessage());

            setDefaultTheme(null);
        }

        backToMainMenuFromThemes(null);
    }

    private void applyGradientTheme(String gradientStyle) {

        Pane rootPane = (Pane) homeMenu.getParent();

        if (rootPane != null) {
            rootPane.setStyle(gradientStyle);
            rootPane.setBackground(null);
        }
        if (homeMenu != null) {
            homeMenu.setStyle(gradientStyle);
            homeMenu.setBackground(null);
        }
        if (themesMenu != null) {
            themesMenu.setStyle(gradientStyle);
            themesMenu.setBackground(null);
        }
    }

    private void initializeHowToPlayBackground() {
        try {
            Image howToPlayBg = new Image(getClass().getResourceAsStream("/howtoplay.png"));
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


    @FXML
    public void startTimeAttackMode(ActionEvent actionEvent) {
        isTimeAttackMode = true;
        remainingSeconds = 180;

        if (homeMenu != null) homeMenu.setVisible(false);
        if (gameBoard != null) gameBoard.setVisible(true);
        if (leftSidebar != null) leftSidebar.setVisible(true);
        if (rightSidebar != null) rightSidebar.setVisible(true);
        if (brickPanel != null) brickPanel.setVisible(true);
        if (timerLabel != null) {
            timerLabel.setVisible(true);
            updateTimerDisplay();
        }

        newGame(actionEvent);
        startCountdown();
        gamePanel.requestFocus();
    }

    private void startCountdown() {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        countdownTimeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    if (!isPause.getValue()) {
                        remainingSeconds--;
                        updateTimerDisplay();

                        if (remainingSeconds <= 0) {
                            timeUp();
                        }
                    }
                }
        ));
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);
        countdownTimeline.play();
    }

    private void updateTimerDisplay() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("TIME: %d:%02d", minutes, seconds));

        if (remainingSeconds <= 30) {
            timerLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ff0000;");
        } else if (remainingSeconds <= 60) {
            timerLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ffaa00;");
        } else {
            timerLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #ff6b35;");
        }
    }

    private void timeUp() {
        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }
        timeLine.stop();

        if (groupNotification != null && gameOverPanel != null) {
            groupNotification.setVisible(true);
            gameOverPanel.setVisible(true);
        }
        isGameOver.setValue(Boolean.TRUE);

        int finalScore = Integer.parseInt(scoreLabel.getText().replace("Score: ", ""));
        HighScoreManager hsm = new HighScoreManager();
        hsm.addScore(finalScore);

        int currentHighScore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + currentHighScore);
    }
}


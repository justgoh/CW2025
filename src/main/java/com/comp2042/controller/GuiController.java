package com.comp2042.controller;

import com.comp2042.model.HighScoreManager;
import com.comp2042.model.Score;
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
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

    @FXML
    private BorderPane gameBoard;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

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

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    private InputEventListener eventListener;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private Score score = new Score();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                    keyEvent.consume();
                }
            }
        });
        gameOverPanel.setVisible(false);

        HighScoreManager hsm = new HighScoreManager();
        int highscore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + highscore);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int row = 2; row < boardMatrix.length; row++) {
            for (int column = 0; column < boardMatrix[row].length; column++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[row][column] = rectangle;
                gamePanel.add(rectangle, column, row - 2);
            }
        }
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[row][column]));
                rectangles[row][column] = rectangle;
                brickPanel.add(rectangle, column, row);

                Rectangle ghost = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghost.setFill(Color.LIGHTGRAY);
                ghost.setStroke(Color.LIGHTGRAY);
                ghost.setStrokeWidth(1.5);
                ghost.setOpacity(0.5);
                ghostRectangles[row][column] = ghost;
                gamePanel.getChildren().add(ghost);
                GridPane.setColumnIndex(ghost, 0);
                GridPane.setRowIndex(ghost, 0);
            }
        }

        gamePanel.setPadding(Insets.EMPTY);
        brickPanel.setPadding(Insets.EMPTY);
        brickPanel.setHgap(0);
        brickPanel.setVgap(0);

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


    private void refreshBrick(ViewData brick) {
        if (!isPause.getValue()) {
            updateBrickPosition(brick);
            updateGhost(brick);
        }
    }

    private void updateBrickPosition(ViewData brick) {
        Point2D gamePanelPos = gamePanel.localToScene(0, 0);
        brickPanel.setLayoutX(gamePanelPos.getX() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(gamePanelPos.getY() + (brick.getyPosition() - 2) * BRICK_SIZE);
        for (int row = 0; row < brick.getBrickData().length; row++) {
            for (int column = 0; column < brick.getBrickData()[row].length; column++) {
                setRectangleData(brick.getBrickData()[row][column], rectangles[row][column]);
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
                if (brick.getBrickData()[row][column] != 0) {
                    ghostRectangles[row][column].setVisible(true);
                    GridPane.setColumnIndex(ghost, brick.getxPosition() + column);
                    GridPane.setRowIndex(ghost, ghostY + row - 2);
                    ghostRectangles[row][column].setFill(Color.LIGHTGRAY);
                    ghostRectangles[row][column].setStroke(Color.GRAY);
                    ghostRectangles[row][column].setStrokeWidth(1.0);
                    ghostRectangles[row][column].setOpacity(0.5);
                } else {
                    ghostRectangles[row][column].setVisible(false);
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
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());

                if (score != null) {
                    score.add(downData.getClearRow().getScoreBonus());
                }
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty scoreProperty) {
        scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);

        int finalScore = score.getScore();
        HighScoreManager hsm = new HighScoreManager();
        int prevHighScore = hsm.loadHighScore();

        hsm.addScore(finalScore);

        int currentHighScore = hsm.loadHighScore();
        highScoreLabel.setText("High Score: " + currentHighScore);
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        score = new Score();
        bindScore(score.scoreProperty());
        HighScoreManager hsm = new HighScoreManager();
        int highscore = hsm.loadHighScore();
        highScoreLabel.setText(("High Score: " + highscore));
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        pauseMenu.setVisible(false);
    }

    public void updateHighScoreLabel(int highscore) {
        highScoreLabel.setText("High Score: " + highscore);
    }

    public void pauseGame(ActionEvent actionEvent) {
        isPause.setValue(!isPause.getValue());
        pauseMenu.setVisible(isPause.getValue());
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


}

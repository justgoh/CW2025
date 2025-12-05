package com.comp2042.view;

import com.comp2042.controller.GameController;
import com.comp2042.controller.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main entry point for the Tetris JavaFX application.
 * <p>
 * This class initializes the JavaFX application, loads the FXML layout,
 * sets up the controllers, and displays the main game window. It establishes
 * the connection between the GUI controller and game logic controller.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Loads the FXML UI layout</li>
 *   <li>Initializes GUI and game controllers</li>
 *   <li>Configures the application window</li>
 *   <li>Launches the JavaFX application</li>
 * </ul>
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application and initializes the game.
     * <p>
     * Loads the FXML layout, creates controller instances, and displays
     * the main window with a fixed size of 1080x720 pixels.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController guiController = fxmlLoader.getController();
        GameController gameController = new GameController(guiController);
        guiController.setGameController(gameController);

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 1080, 720);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}

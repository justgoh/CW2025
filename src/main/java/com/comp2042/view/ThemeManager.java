package com.comp2042.view;

import com.comp2042.model.Theme;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.InputStream;

/**
 * Manages visual themes for the Tetris application.
 * <p>
 * This class handles the application of different visual themes including
 * gradient-based themes and image-based background themes. It ensures consistent
 * theme application across all relevant UI components.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Applies gradient-based CSS themes</li>
 *   <li>Applies image-based background themes</li>
 *   <li>Manages theme switching with fallback handling</li>
 * </ul>
 */
public class ThemeManager {

    /**
     * Constructs a new ThemeManager with the default theme.
     */
    public ThemeManager() {
    }

    /**
     * Applies a theme to the specified pane and related UI components.
     * <p>
     * Automatically determines whether to apply a gradient or image theme
     * based on the theme type.
     *
     * @param theme           the Theme to apply
     * @param rootPane        the main Pane to apply the theme to
     * @param additionalPanes optional additional Panes that should also receive the theme
     */
    public void applyTheme(Theme theme, Pane rootPane, Pane... additionalPanes) {

        if (theme.isImageTheme()) {
            applyImageTheme(theme, rootPane, additionalPanes);
        } else {
            applyGradientTheme(theme, rootPane, additionalPanes);
        }
    }

    /**
     * Applies an image-based theme to the specified panes.
     * <p>
     * Loads the theme's background image and sets it as a BackgroundImage with
     * appropriate sizing and positioning. Falls back to default theme if the
     * image cannot be loaded.
     *
     * @param theme           the Theme containing the image resource
     * @param rootPane        the main Pane to apply the theme to
     * @param additionalPanes optional additional Panes that should also receive the theme
     */
    private void applyImageTheme(Theme theme, Pane rootPane, Pane... additionalPanes) {
        try {
            var imageStream = getClass().getResourceAsStream("/" + theme.getResource());

            if (imageStream == null) {
                System.err.println("Error: Could not find theme image: " + theme.getResource());
                System.err.println("Falling back to default theme");
                applyTheme(Theme.DEFAULT, rootPane, additionalPanes);
                return;
            }
            Background background = createImageBackground(imageStream);

            if (rootPane != null) {
                rootPane.setBackground(background);
                rootPane.setStyle("");
            }

            for (Pane pane : additionalPanes) {
                if (pane != null) {
                    pane.setBackground(background);
                    pane.setStyle("");
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading theme image: " + theme.getResource());
            System.err.println("Falling back to default theme");
            applyTheme(Theme.DEFAULT, rootPane, additionalPanes);
        }
    }

    /**
     * Creates a Background object from an image stream.
     * <p>
     * Configures the background image to cover the entire area while maintaining
     * aspect ratio, with no repetition and centered positioning.
     *
     * @param imageStream the input stream containing the image data
     * @return a Background configured with the image
     */
    private Background createImageBackground(InputStream imageStream) {
        Image bgImage = new Image(
                imageStream);

        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        true
                )
        );

        return new Background(backgroundImage);
    }

    /**
     * Applies a gradient-based theme to the specified panes.
     * <p>
     * Uses CSS styling to create color gradient backgrounds.
     *
     * @param theme           the Theme containing the gradient CSS
     * @param rootPane        the main Pane to apply the theme to
     * @param additionalPanes optional additional Panes that should also receive the theme
     */
    private void applyGradientTheme(Theme theme, Pane rootPane, Pane... additionalPanes) {
        String gradientStyle = theme.getResource();

        if (rootPane != null) {
            rootPane.setStyle(gradientStyle);
            rootPane.setBackground(null);
        }

        for (Pane pane : additionalPanes) {
            if (pane != null) {
                pane.setStyle(gradientStyle);
                pane.setBackground(null);
            }
        }
    }

}
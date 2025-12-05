package com.comp2042.view;

import com.comp2042.model.Theme;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

/**
 * Manages visual themes for the Tetris application.
 * This class handles the application of different visual themes including gradient-based themes and image-based background themes.
 * <p>Supported theme types:
 * <ul>
 *   <li>Gradient themes - CSS-based color gradients</li>
 *   <li>Image themes - Background image overlays</li>
 * </ul>
 *
 * <p>The ThemeManager ensures consistent theme application across all
 * relevant UI components including menus and game screens.
 */

public class ThemeManager {
    /**
     * The currently active theme
     */
    private Theme currentTheme;

    /**
     * Constructs a new ThemeManager with the default theme.
     */
    public ThemeManager() {
        this.currentTheme = Theme.DEFAULT;
    }

    /**
     * Gets the currently active theme.
     *
     * @return the current Theme enum value
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Applies a theme to the specified pane and related UI components.
     * Automatically determines whether to apply a gradient or image theme based on the theme type.
     *
     * @param theme           the Theme to apply
     * @param rootPane        the main Pane to apply the theme to
     * @param additionalPanes optional additional Panes that should also receive the theme
     */
    public void applyTheme(Theme theme, Pane rootPane, Pane... additionalPanes) {
        this.currentTheme = theme;

        if (theme.isImageTheme()) {
            applyImageTheme(theme, rootPane, additionalPanes);
        } else {
            applyGradientTheme(theme, rootPane, additionalPanes);
        }
    }

    /**
     * Applies an image-based theme to the specified panes.
     * Loads the theme's background image and sets it as a BackgroundImage with appropriate sizing and positioning.
     *
     * @param theme           the Theme containing the image resource
     * @param rootPane        the main Pane to apply the theme to
     * @param additionalPanes optional additional Panes that should also receive the theme
     */
    private void applyImageTheme(Theme theme, Pane rootPane, Pane... additionalPanes) {
        try {
            Image bgImage = new Image(
                    getClass().getResourceAsStream("/" + theme.getResource())
            );

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

            Background background = new Background(backgroundImage);

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
     * Applies a gradient-based theme to the specified panes.
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

    /**
     * Applies the default theme to the specified panes.
     * Convenience method for quickly reverting to the default appearance.
     *
     * @param rootPane        the main Pane to apply the theme to
     * @param additionalPanes optional additional Panes that should also receive the theme
     */
    public void applyDefaultTheme(Pane rootPane, Pane... additionalPanes) {
        applyTheme(Theme.DEFAULT, rootPane, additionalPanes);
    }

    /**
     * Gets the name of the currently active theme.
     *
     * @return the theme name as a string
     */
    public String getCurrentThemeName() {
        return currentTheme.getName();
    }
}
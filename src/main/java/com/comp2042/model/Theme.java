package com.comp2042.model;

/**
 * Enum representing available UI themes for the Tetris game.
 * Each theme defines the visual appearance of the game interface through either CSS gradient styles or background images.
 * <p>Available themes:
 * <ul>
 *   <li>DEFAULT - Dark gradient background</li>
 *   <li>COUNTRYSIDE</li>
 *   <li>BEACH</li>
 *   <li>TRON</li>
 * </ul>
 * <p>
 */

public enum Theme {
    /**
     * Default dark gradient theme
     */

    DEFAULT("default",
            "-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);",
            ThemeType.GRADIENT),

    /**
     * Countryside landscape theme with background image
     */
    COUNTRYSIDE("countryside",
            "countryside_bg.png",
            ThemeType.IMAGE),

    /**
     * Beach theme with background image
     */
    BEACH("beach",
            "beach_bg.png",
            ThemeType.IMAGE),

    /**
     * Tron-style theme with background image
     */
    TRON("tron",
            "tron_bg.png",
            ThemeType.IMAGE);

    private final String name;

    private final String resource;

    private final ThemeType type;

    /**
     * Constructs a Theme with the specified properties.
     *
     * @param name     the internal name identifier for this theme
     * @param resource the CSS style string or image filename
     * @param type     the type of theme (GRADIENT or IMAGE)
     */
    Theme(String name, String resource, ThemeType type) {
        this.name = name;
        this.resource = resource;
        this.type = type;
    }

    /**
     * Gets the internal name of this theme.
     *
     * @return the theme name as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the resource associated with this theme.
     * For gradient themes, this is a CSS style string.
     * For image themes, this is the image filename.
     *
     * @return the resource string (CSS or filename)
     */
    public String getResource() {
        return resource;
    }

    /**
     * Checks if this theme uses a background image.
     *
     * @return true if this is an image-based theme, false otherwise
     */
    public boolean isImageTheme() {
        return type == ThemeType.IMAGE;
    }

    /**
     * Enum distinguishing between gradient and image-based themes.
     * Used to determine how the theme should be applied to UI components.
     */
    public enum ThemeType {
        /**
         * CSS gradient-based theme
         */
        GRADIENT,

        /**
         * Image file-based theme
         */
        IMAGE
    }
}
package com.comp2042.model;

/**
 * Enum representing available UI themes for the Tetris game.
 * Each theme defines the visual appearance of the game interface through either CSS gradient styles or background images.
 * <p>Available themes:
 * <ul>
 *   <li>DEFAULT - Dark gradient background</li>
 *   <li>COUNTRYSIDE</li>
 *   <li>BEACH</li>
 *   <li>TRONe</li>
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

    /**
     * The internal name identifier for this theme
     */
    private final String name;

    /**
     * The resource path (CSS style or image filename)
     */
    private final String resource;

    /**
     * The type of theme (gradient or image-based)
     */
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
     * Gets the type of this theme.
     *
     * @return the ThemeType (GRADIENT or IMAGE)
     */
    public ThemeType getType() {
        return type;
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
     * Checks if this theme uses a CSS gradient.
     *
     * @return true if this is a gradient-based theme, false otherwise
     */
    public boolean isGradientTheme() {
        return type == ThemeType.GRADIENT;
    }

    /**
     * Retrieves a Theme enum by its name string.
     * The search is case-insensitive.
     *
     * @param name the theme name to search for
     * @return the corresponding Theme enum, or DEFAULT if not found
     */
    public static Theme fromName(String name) {
        for (Theme theme : values()) {
            if (theme.name.equalsIgnoreCase(name)) {
                return theme;
            }
        }
        return DEFAULT;
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
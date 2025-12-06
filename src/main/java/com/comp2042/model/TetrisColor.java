package com.comp2042.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Enum representing Tetris piece colors with their corresponding color codes.
 * Each Tetris piece type has a unique color identified by a numeric code (0-7).
 * <p>
 * <b>Color codes mapping:</b>
 * <ul>
 *   <li>0 - EMPTY (Transparent)</li>
 *   <li>1 - CYAN (I-piece)</li>
 *   <li>2 - PURPLE (T-piece)</li>
 *   <li>3 - GREEN (S-piece)</li>
 *   <li>4 - YELLOW (O-piece)</li>
 *   <li>5 - RED (Z-piece)</li>
 *   <li>6 - BEIGE (L-piece)</li>
 *   <li>7 - BROWN (J-piece)</li>
 * </ul>
 */
public enum TetrisColor {
    /**
     * Empty cell with transparent color
     */
    EMPTY(0, Color.TRANSPARENT),

    /**
     * Cyan color for I-piece
     */
    CYAN(1, Color.AQUA),

    /**
     * Purple color for T-piece
     */
    PURPLE(2, Color.BLUEVIOLET),

    /**
     * Green color for S-piece
     */
    GREEN(3, Color.DARKGREEN),

    /**
     * Yellow color for O-piece
     */
    YELLOW(4, Color.YELLOW),

    /**
     * Red color for Z-piece
     */
    RED(5, Color.RED),

    /**
     * Beige color for L-piece
     */
    BEIGE(6, Color.BEIGE),

    /**
     * Brown color for J-piece
     */
    BROWN(7, Color.BURLYWOOD);

    /**
     * The numeric code representing this color
     */
    private final int code;

    /**
     * The JavaFX Paint object for this color
     */
    private final Paint color;

    /**
     * Constructs a TetrisColor with the specified code and color.
     *
     * @param code  the numeric identifier for this color (0-7)
     * @param color the JavaFX Paint object representing this color
     */
    TetrisColor(int code, Paint color) {
        this.code = code;
        this.color = color;
    }

    /**
     * Retrieves the Paint color corresponding to a given numeric code.
     * This method is useful for converting color codes stored in the game board
     * matrix into actual colors for rendering.
     *
     * @param code the color code (0-7)
     * @return the corresponding Paint color, or WHITE if the code is invalid
     */
    public static Paint getColorByCode(int code) {
        for (TetrisColor tetrisColor : values()) {
            if (tetrisColor.code == code) {
                return tetrisColor.color;
            }
        }
        return Color.WHITE; // default for unknown codes
    }

}

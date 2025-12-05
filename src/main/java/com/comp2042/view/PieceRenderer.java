package com.comp2042.view;

import com.comp2042.constants.GameConstants;
import com.comp2042.constants.UIConstants;
import com.comp2042.model.TetrisColor;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Handles the rendering of Tetris pieces on various display panels.
 * This class is responsible for creating and updating the visual representation of game pieces, including current pieces, ghost pieces, next piece previews, and the hold piece display.
 * <p>Main responsibilities:
 * <ul>
 *   <li>Converting piece data arrays into visual Rectangle objects</li>
 *   <li>Managing piece colors and styling</li>
 *   <li>Rendering pieces on preview panels</li>
 *   <li>Creating ghost piece visualizations</li>
 * </ul>
 */

public class PieceRenderer {
    /**
     * Constructs a new PieceRenderer.
     */
    public PieceRenderer() {
    }

    /**
     * Gets the Paint color corresponding to a color code.
     * Uses the TetrisColor enum to map numeric codes to actual colors.
     *
     * @param colorCode the numeric color code (0-7)
     * @return the Paint object representing the color
     */
    public Paint getFillColor(int colorCode) {
        return TetrisColor.getColorByCode(colorCode);
    }

    /**
     * Creates a Rectangle with standard brick styling.
     * Applies the game's standard brick size, corner radius, and stroke properties.
     *
     * @param colorCode the color code for this brick
     * @return a styled Rectangle representing a game brick
     */
    public Rectangle createStyledBrick(int colorCode) {
        Rectangle rectangle = new Rectangle(
                GameConstants.BRICK_SIZE,
                GameConstants.BRICK_SIZE
        );
        rectangle.setFill(getFillColor(colorCode));
        rectangle.setArcHeight(GameConstants.BRICK_CORNER_RADIUS);
        rectangle.setArcWidth(GameConstants.BRICK_CORNER_RADIUS);

        if (colorCode != 0) {
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(GameConstants.BRICK_STROKE_WIDTH);
            rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        } else {
            rectangle.setStroke(Color.TRANSPARENT);
            rectangle.setStrokeWidth(0);
        }

        return rectangle;
    }

    /**
     * Creates a Rectangle for preview panels with appropriate sizing.
     * Preview bricks are slightly smaller than game bricks to allow for grid spacing.
     *
     * @return a Rectangle sized for preview panels
     */
    public Rectangle createPreviewBrick() {
        Rectangle rectangle = new Rectangle(
                GameConstants.PREVIEW_BRICK_SIZE,
                GameConstants.PREVIEW_BRICK_SIZE
        );
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);
        return rectangle;
    }

    /**
     * Creates a ghost piece Rectangle with semi-transparent styling.
     * Ghost pieces show where the current piece will land if dropped.
     *
     * @return a styled Rectangle for ghost piece display
     */
    public Rectangle createGhostBrick() {
        Rectangle ghost = new Rectangle(
                GameConstants.BRICK_SIZE,
                GameConstants.BRICK_SIZE
        );
        ghost.setFill(Color.TRANSPARENT);
        ghost.setStroke(Color.GRAY);
        ghost.setStrokeWidth(GameConstants.GHOST_STROKE_WIDTH);
        ghost.setOpacity(GameConstants.GHOST_OPACITY);
        ghost.setArcHeight(GameConstants.BRICK_CORNER_RADIUS);
        ghost.setArcWidth(GameConstants.BRICK_CORNER_RADIUS);
        ghost.setVisible(false);
        ghost.setManaged(false);
        return ghost;
    }

    /**
     * Renders a piece on a preview panel (next piece or hold piece).
     * Centers the piece within a 4x4 grid and applies appropriate colors.
     *
     * @param panel      the GridPane to render the piece on
     * @param pieceData  the 2D array representing the piece shape and colors
     * @param rectangles the 2D array of Rectangle objects in the panel
     */
    public void renderPieceOnPreview(GridPane panel, int[][] pieceData, Rectangle[][] rectangles) {
        for (int row = 0; row < GameConstants.PREVIEW_GRID_SIZE; row++) {
            for (int column = 0; column < GameConstants.PREVIEW_GRID_SIZE; column++) {
                rectangles[row][column].setFill(Color.TRANSPARENT);
            }
        }

        if (pieceData == null) {
            return;
        }

        int startRow = (GameConstants.PREVIEW_GRID_SIZE - pieceData.length) / 2;
        int startColumn = (GameConstants.PREVIEW_GRID_SIZE - pieceData[0].length) / 2;

        for (int row = 0; row < pieceData.length; row++) {
            for (int column = 0; column < pieceData[row].length; column++) {
                if (pieceData[row][column] != 0) {
                    int previewRow = startRow + row;
                    int previewColumn = startColumn + column;

                    if (previewRow >= 0 && previewRow < GameConstants.PREVIEW_GRID_SIZE &&
                            previewColumn >= 0 && previewColumn < GameConstants.PREVIEW_GRID_SIZE) {
                        rectangles[previewRow][previewColumn].setFill(
                                getFillColor(pieceData[row][column])
                        );
                    }
                }
            }
        }
    }

    /**
     * Initializes a preview panel with a grid of Rectangle objects.
     * Sets up the GridPane with appropriate spacing and padding.
     *
     * @param panel the GridPane to initialize
     * @return a 2D array of Rectangle objects representing the grid
     */
    public Rectangle[][] initializePreviewPanel(GridPane panel) {
        panel.getChildren().clear();
        Rectangle[][] rectangles = new Rectangle[GameConstants.PREVIEW_GRID_SIZE][GameConstants.PREVIEW_GRID_SIZE];

        for (int row = 0; row < GameConstants.PREVIEW_GRID_SIZE; row++) {
            for (int column = 0; column < GameConstants.PREVIEW_GRID_SIZE; column++) {
                Rectangle rectangle = createPreviewBrick();
                rectangles[row][column] = rectangle;
                panel.add(rectangle, column, row);
            }
        }

        panel.setHgap(1);
        panel.setVgap(1);
        panel.setPadding(new Insets(UIConstants.PREVIEW_PANEL_PADDING));

        return rectangles;
    }

    /**
     * Clears all rectangles in a preview panel to transparent.
     * Used when there is no piece to display.
     *
     * @param rectangles the 2D array of Rectangle objects to clear
     */
    public void clearPreviewPanel(Rectangle[][] rectangles) {
        for (int row = 0; row < rectangles.length; row++) {
            for (int column = 0; column < rectangles[row].length; column++) {
                rectangles[row][column].setFill(Color.TRANSPARENT);
            }
        }
    }

    /**
     * Applies styling to a Rectangle based on its color code.
     * Sets fill color, stroke, and corner radius according to game standards.
     *
     * @param rectangle the Rectangle to style
     * @param colorCode the color code to apply (0-7)
     */
    public void applyBrickStyling(Rectangle rectangle, int colorCode) {
        rectangle.setFill(getFillColor(colorCode));
        rectangle.setArcHeight(GameConstants.BRICK_CORNER_RADIUS);
        rectangle.setArcWidth(GameConstants.BRICK_CORNER_RADIUS);

        if (colorCode != 0) {
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(GameConstants.BRICK_STROKE_WIDTH);
            rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        } else {
            rectangle.setStroke(Color.TRANSPARENT);
            rectangle.setStrokeWidth(0);
        }
    }
}

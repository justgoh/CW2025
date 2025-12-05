package com.comp2042.model;

/**
 * Represents the result of a line clearing operation in Tetris.
 * <p>
 * This immutable class encapsulates all data related to clearing completed
 * rows from the game board, including the number of lines cleared, the
 * updated board state, and the score earned from the clear.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Stores the number of lines removed</li>
 *   <li>Provides the updated game board after line removal</li>
 *   <li>Calculates and stores the score bonus earned</li>
 * </ul>
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a ClearRow with the specified clearing results.
     *
     * @param linesRemoved the number of lines that were cleared
     * @param newMatrix    the updated board matrix after clearing lines
     * @param scoreBonus   the score points earned from this clear
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines removed in this clearing operation.
     *
     * @return the number of lines cleared, zero or positive
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets a copy of the updated board matrix after line clearing.
     * <p>
     * Returns a defensive copy to preserve immutability.
     *
     * @return a copy of the board matrix with cleared lines removed, never null
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus earned from this line clearing operation.
     *
     * @return the score points awarded for clearing lines
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}

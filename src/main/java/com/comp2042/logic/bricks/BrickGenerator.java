package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Generates and manages the sequence of Tetris pieces during gameplay.
 * <p>
 * This interface defines the contract for piece generation systems in the game.
 * Implementations are responsible for creating a sequence of Tetris pieces,
 * providing the current piece, and allowing preview of upcoming pieces.
 * The generator ensures a fair and unpredictable distribution of pieces
 * throughout the game.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Generates new Tetris pieces on demand</li>
 *   <li>Maintains a queue of upcoming pieces</li>
 *   <li>Provides preview capabilities for future pieces</li>
 *   <li>Ensures randomized but fair piece distribution</li>
 * </ul>
 *
 * @see Brick
 */
public interface BrickGenerator {

    /**
     * Retrieves and consumes the next piece from the generation queue.
     * <p>
     * This method removes the piece from the queue and generates a new one
     * to maintain the queue size.
     *
     * @return the next Brick in the sequence, never null
     */
    Brick getBrick();

    /**
     * Retrieves the next piece without consuming it from the queue.
     * <p>
     * Useful for displaying a preview of the upcoming piece to the player.
     *
     * @return the next Brick that will be returned by {@link #getBrick()}, never null
     */
    Brick getNextBrick();

    /**
     * Retrieves multiple upcoming pieces for preview purposes.
     * <p>
     * The generation queue remains unchanged after this call.
     *
     * @param count the number of upcoming pieces to retrieve, must be positive
     * @return a list of upcoming Bricks in spawn order, never null. May contain
     * fewer elements than count if the queue is smaller.
     */
    List<Brick> getNextBricks(int count);
}

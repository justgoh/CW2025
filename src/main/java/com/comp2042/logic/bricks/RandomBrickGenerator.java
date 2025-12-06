package com.comp2042.logic.bricks;

import java.util.*;

/**
 * Generates Tetris pieces using the "bag" randomization system.
 * <p>
 * This generator implements the modern Tetris randomization algorithm where all
 * seven piece types are shuffled into a "bag", then dealt out one by one. When
 * the bag is empty, a new shuffled bag is created. This ensures fair distribution
 * and prevents long droughts of specific pieces.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Generates pieces using the bag randomization algorithm</li>
 *   <li>Maintains a queue for piece previews</li>
 *   <li>Ensures all seven piece types appear equally often</li>
 *   <li>Provides look-ahead capability for multiple upcoming pieces</li>
 * </ul>
 */
public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    private final Deque<Brick> currentBag = new ArrayDeque<>();

    private static final int PREVIEW_COUNT = 3;

    /**
     * Constructs a RandomBrickGenerator with all seven Tetris piece types.
     * <p>
     * Initializes the brick list with I, J, L, O, S, T, and Z pieces, creates
     * the first shuffled bag, and fills the preview queue.
     */
    public RandomBrickGenerator() {
        brickList = List.of(
                new IBrick(),
                new JBrick(),
                new LBrick(),
                new OBrick(),
                new SBrick(),
                new TBrick(),
                new ZBrick()
        );
        refillBag();
        ensureQueueSize();
    }

    /**
     * Refills the bag with a new shuffled set of all seven piece types.
     * <p>
     * Creates a shuffled copy of all pieces and adds them to the current bag.
     */
    private void refillBag() {
        List<Brick> shuffled = new ArrayList<>(brickList);
        Collections.shuffle(shuffled);
        currentBag.addAll(shuffled);
    }

    /**
     * Ensures the preview queue maintains the required size.
     * <p>
     * Fills the queue to at least PREVIEW_COUNT + 1 pieces, refilling the
     * bag as needed when it becomes empty.
     */
    private void ensureQueueSize() {
        while (nextBricks.size() < PREVIEW_COUNT + 1) {
            if (currentBag.isEmpty()) {
                refillBag();
            }
            nextBricks.add(currentBag.poll());
        }
    }

    /**
     * Retrieves and consumes the next piece from the generation queue.
     * <p>
     * Removes the next piece from the queue and replenishes it with a new
     * piece from the bag, refilling the bag if necessary.
     *
     * @return the next Brick to be used in the game, never null
     */
    @Override
    public Brick getBrick() {
        ensureQueueSize();
        Brick nextBrick = nextBricks.poll();

        if (currentBag.isEmpty()) {
            refillBag();
        }
        nextBricks.add(currentBag.poll());
        return nextBrick;
    }

    /**
     * Retrieves the next piece without consuming it from the queue.
     *
     * @return the next Brick that will be returned by getBrick(), never null
     */
    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

    /**
     * Retrieves multiple upcoming pieces for preview purposes.
     *
     * @param count the number of upcoming pieces to retrieve
     * @return a list of upcoming Bricks in the order they will be spawned,
     * may contain fewer elements than count if fewer pieces are available
     */
    @Override
    public List<Brick> getNextBricks(int count) {
        List<Brick> preview = new ArrayList<>();
        Iterator<Brick> iterator = nextBricks.iterator();

        for (int i = 0; i < count && iterator.hasNext(); i++) {
            preview.add(iterator.next());
        }
        return preview;
    }
}

package com.comp2042.logic.bricks;

import java.util.*;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    private final Deque<Brick> currentBag = new ArrayDeque<>();

    private static final int PREVIEW_COUNT = 3;

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

    private void refillBag() {
        List<Brick> shuffled = new ArrayList<>(brickList);
        Collections.shuffle(shuffled);
        currentBag.addAll(shuffled);
    }

    private void ensureQueueSize() {
        while (nextBricks.size() < PREVIEW_COUNT + 1) {
            if (currentBag.isEmpty()) {
                refillBag();
            }
            nextBricks.add(currentBag.poll());
        }
    }

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

    @Override
    public Brick getNextBrick() {
        return nextBricks.peek();
    }

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

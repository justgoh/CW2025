package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    @Test
    void testConstructor_InitializesCorrectly() {
        Brick nextBrick = generator.getNextBrick();
        assertNotNull(nextBrick);
        assertNotNull(nextBrick.getShapeMatrix());
        assertFalse(nextBrick.getShapeMatrix().isEmpty());
    }

    @RepeatedTest(20)
    void testGetBrick_ReturnsValidBrick() {
        Brick brick = generator.getBrick();

        assertNotNull(brick, "Brick should not be null");
        assertNotNull(brick.getShapeMatrix(), "Shape matrix should not be null");
        assertFalse(brick.getShapeMatrix().isEmpty(), "Shape matrix should not be empty");

        for (int[][] shape : brick.getShapeMatrix()) {
            assertNotNull(shape, "Individual shape should not be null");
            assertTrue(shape.length > 0, "Shape should have rows");
            assertTrue(shape[0].length > 0, "Shape should have columns");
        }
    }

    @Test
    void testGetBrick_ReturnsDifferentBricksOverTime() {
        Set<Class<?>> brickTypes = new HashSet<>();

        for (int i = 0; i < 50; i++) {
            Brick brick = generator.getBrick();
            brickTypes.add(brick.getClass());
        }

        assertTrue(brickTypes.size() > 1,
                "Should return different brick types over time. Found: " + brickTypes.size());
    }

    @Test
    void testGetNextBrick_DoesNotConsumeBrick() {
        Brick preview1 = generator.getNextBrick();
        Brick preview2 = generator.getNextBrick();
        Brick actual = generator.getBrick();

        assertEquals(preview1, preview2,
                "getNextBrick should return the same brick without consuming it");

        assertEquals(preview1, actual,
                "getBrick should return the brick that was previewed");
    }

    @Test
    void testGetNextBrick_AfterGetBrick_ShowsNextBrick() {
        Brick firstPreview = generator.getNextBrick();
        Brick firstActual = generator.getBrick();
        Brick secondPreview = generator.getNextBrick();

        assertEquals(firstPreview, firstActual);

        assertNotEquals(firstPreview, secondPreview,
                "After getBrick(), getNextBrick should show the next brick");
    }

    @Test
    void testGetNextBricks_ReturnsCorrectNumberOfBricks() {
        List<Brick> preview = generator.getNextBricks(3);

        assertEquals(3, preview.size(), "Should return requested number of bricks");

        for (Brick brick : preview) {
            assertNotNull(brick);
            assertNotNull(brick.getShapeMatrix());
        }
    }

    @Test
    void testGetNextBricks_LargeCount_ReturnsAvailableBricks() {
        List<Brick> preview = generator.getNextBricks(10);

        assertTrue(preview.size() <= 4,
                "Should not return more than available preview bricks");
        assertTrue(preview.size() > 0, "Should return at least one brick");
    }

    @Test
    void testGetNextBricks_ZeroCount_ReturnsEmptyList() {
        List<Brick> preview = generator.getNextBricks(0);

        assertNotNull(preview);
        assertTrue(preview.isEmpty());
    }

    @Test
    void testGetNextBricks_NegativeCount_ReturnsEmptyList() {
        List<Brick> preview = generator.getNextBricks(-1);

        assertNotNull(preview);
        assertTrue(preview.isEmpty());
    }

    @Test
    void testBagMechanic_AllBricksAppear() {
        Set<Class<?>> seenBricks = new HashSet<>();

        for (int i = 0; i < 50; i++) {
            Brick brick = generator.getBrick();
            seenBricks.add(brick.getClass());

            if (seenBricks.size() == 7) {
                break;
            }
        }

        assertEquals(7, seenBricks.size(),
                "Should have seen all 7 brick types. Found: " + seenBricks.size());
    }

    @Test
    void testBagMechanic_NoImmediateRepeats() {

        Brick previous = null;
        int repeatCount = 0;

        for (int i = 0; i < 100; i++) {
            Brick current = generator.getBrick();

            if (previous != null && current.getClass().equals(previous.getClass())) {
                repeatCount++;
            }

            previous = current;
        }

        assertTrue(repeatCount < 10,
                "Should have very few immediate repeats with bag system. Found: " + repeatCount);
    }

    @Test
    void testFairDistribution_OverManyDraws() {
        int totalDraws = 1000;
        int[] brickCounts = new int[7];

        for (int i = 0; i < totalDraws; i++) {
            Brick brick = generator.getBrick();

            String className = brick.getClass().getSimpleName();
            switch (className) {
                case "IBrick": brickCounts[0]++; break;
                case "JBrick": brickCounts[1]++; break;
                case "LBrick": brickCounts[2]++; break;
                case "OBrick": brickCounts[3]++; break;
                case "SBrick": brickCounts[4]++; break;
                case "TBrick": brickCounts[5]++; break;
                case "ZBrick": brickCounts[6]++; break;
            }
        }

        double expected = totalDraws / 7.0;
        double tolerance = expected * 0.15;

        for (int count : brickCounts) {
            double diff = Math.abs(count - expected);
            assertTrue(diff < tolerance,
                    "Brick distribution should be fair. Expected ~" + expected +
                            ", got " + count + " (difference: " + diff + ")");
        }
    }

    @Test
    void testPreviewQueue_MaintainsCorrectSize() {
        List<Brick> initialPreview = generator.getNextBricks(10);
        int initialSize = initialPreview.size();

        for (int i = 0; i < 5; i++) {
            generator.getBrick();
            List<Brick> currentPreview = generator.getNextBricks(10);

            assertTrue(Math.abs(currentPreview.size() - initialSize) <= 1,
                    "Queue size should remain stable. Was: " + initialSize +
                            ", now: " + currentPreview.size());
        }
    }

    @Test
    void testGetNextBricks_ReturnsCorrectOrder() {
        List<Brick> preview = generator.getNextBricks(3);

        Brick first = generator.getBrick();
        Brick second = generator.getBrick();
        Brick third = generator.getBrick();

        assertEquals(preview.get(0), first, "First preview should match first brick");
        assertEquals(preview.get(1), second, "Second preview should match second brick");
        assertEquals(preview.get(2), third, "Third preview should match third brick");
    }

    @Test
    void testAllBrickTypes_HaveValidShapes() {
        for (int i = 0; i < 50; i++) {
            Brick brick = generator.getBrick();
            List<int[][]> shapes = brick.getShapeMatrix();

            assertNotNull(shapes);
            assertFalse(shapes.isEmpty());

            for (int[][] shape : shapes) {
                assertNotNull(shape);
                assertTrue(shape.length >= 2 && shape.length <= 4,
                        "Brick should have reasonable dimensions. Rows: " + shape.length);
                assertTrue(shape[0].length >= 2 && shape[0].length <= 4,
                        "Brick should have reasonable dimensions. Columns: " + shape[0].length);
            }
        }
    }
}
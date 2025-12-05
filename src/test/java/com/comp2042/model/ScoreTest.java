package com.comp2042.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialScoreIsZero() {
        assertEquals(0, score.getScore());
    }

    @Test
    void testAddPositivePoints() {
        score.add(100);
        assertEquals(100, score.getScore());

        score.add(50);
        assertEquals(150, score.getScore());
    }

    @Test
    void testAddNegativePoints() {
        score.add(200);
        score.add(-50);
        assertEquals(150, score.getScore());
    }

    @Test
    void testResetSetsScoreToZero() {
        score.add(500);
        assertEquals(500, score.getScore());

        score.reset();
        assertEquals(0, score.getScore());
    }

    @Test
    void testScorePropertyIsObservable() {
        assertNotNull(score.scoreProperty());
        assertEquals(0, score.scoreProperty().get());

        score.add(100);
        assertEquals(100, score.scoreProperty().get());
    }

    @Test
    void testMultipleOperations() {
        score.add(100);
        score.add(200);
        score.add(-50);
        assertEquals(250, score.getScore());

        score.reset();
        assertEquals(0, score.getScore());

        score.add(75);
        assertEquals(75, score.getScore());
    }
}

package com.comp2042.controller;

import com.comp2042.constants.AnimationConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the game's main timeline for automatic piece dropping.
 * <p>
 * This class encapsulates the JavaFX Timeline that controls the game loop,
 * providing a clean interface for starting, stopping, pausing, and resuming
 * the game's automatic progression. The timeline triggers a callback at
 * regular intervals, causing the current piece to move down automatically.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Start and stop the game loop</li>
 *   <li>Pause and resume without losing state</li>
 *   <li>Adjustable tick rate for difficulty levels</li>
 *   <li>Safe cleanup to prevent memory leaks</li>
 * </ul>
 */
public class GameTimeline {

    private Timeline timeline;

    private final Runnable onTick;

    private final int tickDelayMs;

    private boolean isRunning;

    /**
     * Constructs a GameTimeline with the specified tick callback.
     * Uses the default tick delay from AnimationConfig.
     *
     * @param onTick the Runnable to execute on each game tick
     */
    public GameTimeline(Runnable onTick) {
        this(onTick, AnimationConfig.DEFAULT_GAME_TICK_DELAY_MS);
    }

    /**
     * Constructs a GameTimeline with a custom tick delay.
     *
     * @param onTick      the Runnable to execute on each game tick
     * @param tickDelayMs the delay between ticks in milliseconds
     */
    public GameTimeline(Runnable onTick, int tickDelayMs) {
        this.onTick = onTick;
        this.tickDelayMs = tickDelayMs;
        this.isRunning = false;
    }

    /**
     * Starts the game timeline.
     * Creates a new Timeline and begins the game loop. If a timeline
     * is already running, it will be stopped first.
     */
    public void start() {
        stop();

        timeline = new Timeline(new KeyFrame(
                Duration.millis(tickDelayMs),
                event ->
                        onTick.run()
        ));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        isRunning = true;
    }

    /**
     * Stops the game timeline and cleans up resources.
     * After calling stop(), the timeline must be restarted with start()
     * to resume the game loop.
     */
    public void stop() {
        if (timeline != null) {
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline = null;
        }
        isRunning = false;
    }

    /**
     * Pauses the game timeline.
     * The timeline can be resumed from the same position using resume().
     * The timeline must be running for this method to have any effect.
     */
    public void pause() {
        if (timeline != null && isRunning) {
            timeline.pause();
            isRunning = false;
        }
    }

    /**
     * Resumes the game timeline from a paused state.
     * The timeline must have been paused for this method to have any effect.
     */
    public void resume() {

        if (timeline != null && !isRunning) {
            timeline.play();
            isRunning = true;
        }
    }
}
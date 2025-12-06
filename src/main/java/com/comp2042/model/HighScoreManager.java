package com.comp2042.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages high scores and leaderboards for different game modes.
 * <p>
 * This class handles persistent storage of player scores across game sessions,
 * maintaining separate leaderboards for Classic and Time Attack modes. Scores
 * are stored in text files and automatically sorted in descending order.
 * <p>
 * <b>Functionality:</b>
 * <ul>
 *   <li>Loads and saves scores from persistent storage</li>
 *   <li>Maintains top 10 scores for each game mode</li>
 *   <li>Provides quick access to high scores</li>
 *   <li>Automatically sorts scores in descending order</li>
 * </ul>
 */
public class HighScoreManager {

    private static final String CLASSIC_FILE = "classic_leaderboard.txt";
    private static final String TIME_ATTACK_FILE = "timeattack_leaderboard.txt";

    /**
     * Represents the available game modes for score tracking.
     */
    public enum GameMode {
        /**
         * Classic Tetris mode with standard rules
         */
        CLASSIC,
        /**
         * Time Attack mode with countdown timer
         */
        TIME_ATTACK
    }

    /**
     * Loads all scores for the specified game mode from persistent storage.
     *
     * @param mode the game mode to load scores for
     * @return a list of all saved scores, empty list if no scores exist or file cannot be read
     */
    public List<Integer> loadScores(GameMode mode) {
        List<Integer> scores = new ArrayList<>();
        String fileName = getFileName(mode);

        try (BufferedReader reader = new BufferedReader((new FileReader(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line.trim()));
            }
        } catch (Exception e) {
            // IOException: File not found or read error (expected on first run)
            // NumberFormatException: Corrupted score file
        }
        return scores;
    }

    /**
     * Saves the score list to persistent storage for the specified game mode.
     *
     * @param mode   the game mode to save scores for
     * @param scores the list of scores to save
     */
    public void saveScores(GameMode mode, List<Integer> scores) {
        String fileName = getFileName(mode);
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(fileName)))) {
            for (int score : scores) {
                writer.write(score + "");
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error saving scores to " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Adds a new score to the leaderboard for the specified game mode.
     * <p>
     * The score is automatically inserted into the sorted leaderboard. If the
     * leaderboard exceeds 10 entries, the lowest score is removed.
     *
     * @param mode  the game mode to add the score to
     * @param score the score value to add
     */
    public void addScore(GameMode mode, int score) {
        List<Integer> scores = loadScores(mode);
        scores.add(score);

        scores.sort(Collections.reverseOrder());

        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }
        saveScores(mode, scores);
    }

    /**
     * Retrieves the highest score for the specified game mode.
     *
     * @param mode the game mode to get the high score for
     * @return the highest score, or 0 if no scores exist
     */
    public int loadHighScore(GameMode mode) {
        List<Integer> scores = loadScores(mode);
        return scores.isEmpty() ? 0 : scores.getFirst();
    }

    /**
     * Retrieves the highest score for Classic mode.
     * <p>
     * This is a convenience method equivalent to calling {@link #loadHighScore(GameMode)}
     * with GameMode.CLASSIC.
     *
     * @return the highest score in Classic mode, or 0 if no scores exist
     */
    public int loadHighScore() {
        return loadHighScore(GameMode.CLASSIC);
    }

    /**
     * Retrieves the top scores for the specified game mode.
     *
     * @param mode  the game mode to get scores for
     * @param count the maximum number of scores to retrieve
     * @return a list of the top scores in descending order, may contain fewer
     * than count elements if fewer scores are available
     */
    public List<Integer> getTopScores(GameMode mode, int count) {
        List<Integer> scores = loadScores(mode);
        scores.sort(Collections.reverseOrder());
        return scores.subList(0, Math.min(scores.size(), count));
    }

    /**
     * Gets the filename for storing scores based on game mode.
     *
     * @param mode the game mode
     * @return the filename for the specified mode
     */
    private String getFileName(GameMode mode) {
        return switch (mode) {
            case CLASSIC -> CLASSIC_FILE;
            case TIME_ATTACK -> TIME_ATTACK_FILE;
        };
    }
}


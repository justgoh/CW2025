package com.comp2042.view;

import com.comp2042.constants.UIConstants;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Handles the display and formatting of leaderboard data.
 * This class is responsible for creating and populating the visual representation of high scores in a structured, readable format.
 *
 * <p>Features:
 * <ul>
 *   <li>Displays ranked list of scores</li>
 *   <li>Handles empty leaderboard states</li>
 *   <li>Applies consistent styling to score entries</li>
 *   <li>Supports customizable titles for different game modes</li>
 * </ul>
 */

public class LeaderboardView {
    /**
     * The VBox container for leaderboard entries
     */
    private final VBox leaderboardContainer;

    /**
     * Constructs a LeaderboardView with the specified container.
     *
     * @param leaderboardContainer the VBox that will contain the leaderboard entries
     */
    public LeaderboardView(VBox leaderboardContainer) {
        this.leaderboardContainer = leaderboardContainer;
    }

    /**
     * Displays a leaderboard with the specified title and scores.
     * Clears any existing content and populates the container with
     * formatted score entries or a "no scores" message.
     *
     * @param title  the title to display at the top of the leaderboard
     * @param scores the list of scores to display, ordered from highest to lowest
     */
    public void displayLeaderboard(String title, List<Integer> scores) {
        leaderboardContainer.getChildren().clear();
        addTitle(title);

        if (scores.isEmpty()) {
            addNoScoresMessage();
        } else {
            addScoreEntries(scores);
        }
    }

    /**
     * Adds a title label to the leaderboard container.
     * The title is styled with gold color and bold font.
     *
     * @param title the title text to display
     */
    private void addTitle(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-text-fill: GOLD; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold;"
        );
        leaderboardContainer.getChildren().add(titleLabel);
    }

    /**
     * Adds a "no scores yet" message to the leaderboard container.
     * This is displayed when the leaderboard is empty.
     */
    private void addNoScoresMessage() {
        Label noScoresLabel = new Label("No scores yet!");
        noScoresLabel.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-size: 16px;"
        );
        leaderboardContainer.getChildren().add(noScoresLabel);
    }

    /**
     * Adds formatted score entries to the leaderboard container.
     * Each entry displays a rank number and the corresponding score.
     *
     * @param scores the list of scores to display
     */
    private void addScoreEntries(List<Integer> scores) {
        for (int i = 0; i < scores.size(); i++) {
            HBox scoreEntry = createScoreEntry(i + 1, scores.get(i));
            leaderboardContainer.getChildren().add(scoreEntry);
        }
    }

    /**
     * Creates a single score entry HBox with rank and score labels.
     * The entry is formatted with consistent spacing and alignment.
     *
     * @param rank  the rank position (1, 2, 3, etc.)
     * @param score the score value
     * @return an HBox containing the formatted score entry
     */
    private HBox createScoreEntry(int rank, int score) {
        HBox scoreEntry = new HBox(UIConstants.LEADERBOARD_ENTRY_SPACING);
        scoreEntry.setAlignment(Pos.CENTER_LEFT);
        scoreEntry.setPrefWidth(UIConstants.LEADERBOARD_ENTRY_WIDTH);

        Label rankLabel = createRankLabel(rank);
        Label scoreLabel = createScoreLabel(score);

        scoreEntry.getChildren().addAll(rankLabel, scoreLabel);

        return scoreEntry;
    }

    /**
     * Creates a rank label with appropriate styling.
     * The rank is displayed with gold color and bold font.
     *
     * @param rank the rank number to display
     * @return a styled Label for the rank
     */
    private Label createRankLabel(int rank) {
        Label rankLabel = new Label(rank + ".");
        rankLabel.setStyle(
                "-fx-text-fill: #ffd700; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold;"
        );
        rankLabel.setMinWidth(UIConstants.LEADERBOARD_RANK_WIDTH);
        rankLabel.setAlignment(Pos.CENTER_RIGHT);

        return rankLabel;
    }

    /**
     * Creates a score label with appropriate styling and formatting.
     * The score is displayed with comma separators for readability.
     *
     * @param score the score value to display
     * @return a styled Label for the score
     */
    private Label createScoreLabel(int score) {
        Label scoreLabel = new Label(String.format("%,d", score));
        scoreLabel.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-size: 18px;"
        );

        return scoreLabel;
    }

    /**
     * Clears all content from the leaderboard container.
     * Useful for resetting the display before showing new data.
     */
    public void clear() {
        leaderboardContainer.getChildren().clear();
    }
}
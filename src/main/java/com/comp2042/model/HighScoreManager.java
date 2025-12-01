package com.comp2042.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {

    private static final String CLASSIC_FILE = "classic_leaderboard.txt";
    private static final String TIME_ATTACK_FILE = "timeattack_leaderboard.txt";

    public enum GameMode {
        CLASSIC,
        TIME_ATTACK
    }

    public List<Integer> loadScores(GameMode mode) {
        List<Integer> scores = new ArrayList<>();
        String fileName = getFileName(mode);

        try (BufferedReader reader = new BufferedReader((new FileReader(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line.trim()));
            }
        } catch (Exception e) {
        }
        return scores;
    }

    public void saveScores(GameMode mode, List<Integer> scores) {
        String fileName = getFileName(mode);
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(fileName)))) {
            for (int score : scores) {
                writer.write(score + "");
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addScore(GameMode mode, int score) {
        List<Integer> scores = loadScores(mode);
        scores.add(score);

        Collections.sort(scores, Collections.reverseOrder());

        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }
        saveScores(mode, scores);
    }

    public int loadHighScore(GameMode mode) {
        List<Integer> scores = loadScores(mode);
        return scores.isEmpty() ? 0 : scores.get(0);
    }

    public int loadHighScore() {
        return loadHighScore(GameMode.CLASSIC);
    }

    public List<Integer> getTopScores(GameMode mode, int count) {
        List<Integer> scores = loadScores(mode);
        Collections.sort(scores, Collections.reverseOrder());
        return scores.subList(0, Math.min(scores.size(), count));
    }

    private String getFileName(GameMode mode) {
        switch (mode) {
            case CLASSIC:
                return CLASSIC_FILE;
            case TIME_ATTACK:
                return TIME_ATTACK_FILE;
            default:
                return CLASSIC_FILE;
        }
    }
}


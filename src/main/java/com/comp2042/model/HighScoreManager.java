package com.comp2042.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {

    private static final String FILE_NAME = "leaderboard.txt";

    public List<Integer> loadScores() {
        List<Integer> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader((new FileReader(FILE_NAME)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(Integer.parseInt(line.trim()));
            }
        } catch (Exception e) {
        }
        return scores;
    }

    public void saveScores(List<Integer> scores) {
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(FILE_NAME)))) {
            for (int score : scores) {
                writer.write(score + "");
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addScore(int score) {
        List<Integer> scores = loadScores();
        scores.add(score);

        Collections.sort(scores, Collections.reverseOrder());

        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }
        saveScores(scores);
    }

    public int loadHighScore() {
        List<Integer> scores = loadScores();
        return scores.isEmpty() ? 0 : scores.get(0);
    }
}


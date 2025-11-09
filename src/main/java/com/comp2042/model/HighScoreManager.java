package com.comp2042.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class HighScoreManager {

    private static final String FILE_NAME = "highscore.txt";

    public int loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            return 0;
        }
    }

    public void saveHighScore(int score) {
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(FILE_NAME)))) {
            writer.write(String.valueOf(score));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


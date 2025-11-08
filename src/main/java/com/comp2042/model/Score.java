package com.comp2042.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

<<<<<<< HEAD
    public void add(int i){
        score.setValue(score.getValue() + i);
=======
    public int getScore() {
        return score.get();
    }

    public void add(int points) {
        score.setValue(score.getValue() + points);
>>>>>>> 2maintanence
    }

    public void reset() {
        score.setValue(0);
    }
}

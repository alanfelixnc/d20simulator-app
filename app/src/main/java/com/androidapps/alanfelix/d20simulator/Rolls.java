package com.androidapps.alanfelix.d20simulator;

public class Rolls {

    String rollId;
    int diceType;
    int rollValue;
    String rollDate;

    public Rolls() {

    }

    public Rolls(String rollId, int diceType, int rollValue, String rollDate) {
        this.rollId = rollId;
        this.diceType = diceType;
        this.rollValue = rollValue;
        this.rollDate = rollDate;
    }

    public String getRollId() {
        return rollId;
    }

    public int getDiceType() {
        return diceType;
    }

    public String getRollDate() {
        return rollDate;
    }

    public int getRollValue() {
        return rollValue;
    }
}

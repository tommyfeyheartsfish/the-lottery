package com.anwithayi.Client;

public class ClientContext {
    private int score;
    private String lastGuessedNum;
    private int lastCorrectlyGuessedNum;
    private String username;
    private boolean status; // Represents whether the client has guessed or not

    // Constructor
    public ClientContext() {
        this.username = null;
        this.score = 0;
        this.lastGuessedNum = null;
        this.lastCorrectlyGuessedNum = 0;
        this.status = false; // Initially not connected
    }

    // Getters and Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLastGuessedNum() {
        return lastGuessedNum;
    }

    public void setLastGuessedNum(String lastGuessedNum) {
        this.lastGuessedNum = lastGuessedNum;
    }

    public int getLastCorrectlyGuessedNum() {
        return lastCorrectlyGuessedNum;
    }

    public void setLastCorrectlyGuessedNum(int lastCorrectlyGuessedNum) {
        this.lastCorrectlyGuessedNum = lastCorrectlyGuessedNum;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username=username;
    }
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

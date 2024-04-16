package com.anwithayi.Server;

import java.io.PrintWriter;

public class Client {
    private int score;
    private String lastGuessedNum;
    private int lastCorrectlyGuessedNum;
    private String username; // Assuming you need a username field as well
    private PrintWriter out;
    private boolean hasGuessed;

    // Constructor
    public Client(String username) {
        this.username = username;
        this.score = 0; // Initialized to 0
        this.lastGuessedNum = null; // Initialized to null
        this.lastCorrectlyGuessedNum = 0; // Initialized to 0
        this.hasGuessed = false;

    }

    //constructor overload
    public Client(){
        this.username = null;
        this.score = 0; // Initialized to 0
        this.lastGuessedNum = null; // Initialized to null
        this.lastCorrectlyGuessedNum = 0; // Initialized to 0

    }

    // Getters and Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public boolean hasGuessed()
    {
       return hasGuessed;
    }
    public void setHasGuessed(boolean hasGuessed){
        this.hasGuessed=hasGuessed;
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
    public void setUsername(String name){
        this.username = name;

    }
    public void setWriter(PrintWriter out) {
        this.out = out;
    }

    public PrintWriter getWriter() {
        return this.out;
    }

    public void sendMessage(String message) {
        if(out != null) {
            out.println(message);
        }
    }
}


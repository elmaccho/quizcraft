package com.example.quizcraft.api;

public class ScoreResponse {
    public boolean success;
    public String message;
    public Integer winner_id;
    public ScoreEntry[] scores;

    public static class ScoreEntry {
        public int user_id;
        public int score;
    }
}
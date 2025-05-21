package com.example.quizcraft.api;

public class ScoreRequest {
    public int user_id;
    public int rozgrywka_id;
    public int score;

    public ScoreRequest(int user_id, int rozgrywka_id, int score) {
        this.user_id = user_id;
        this.rozgrywka_id = rozgrywka_id;
        this.score = score;
    }
}
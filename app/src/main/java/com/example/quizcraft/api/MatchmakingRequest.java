package com.example.quizcraft.api;

public class MatchmakingRequest {
    public int user_id;
    public int category_id;

    public MatchmakingRequest(int user_id, int category_id) {
        this.user_id = user_id;
        this.category_id = category_id;
    }
}
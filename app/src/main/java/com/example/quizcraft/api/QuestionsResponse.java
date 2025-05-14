package com.example.quizcraft.api;

import java.io.Serializable;
import java.util.List;

public class QuestionsResponse {
    public boolean success;
    public String message;
    public List<Question> questions;

    public class Question implements Serializable {
        public int id;
        public String tresc;
        public String photo;
        public int kategoria_id;
        public List<Answer> odpowiedzi;
    }

    public class Answer implements Serializable {
        public int id;
        public String nazwa;
        public int poprawna;

    }
}
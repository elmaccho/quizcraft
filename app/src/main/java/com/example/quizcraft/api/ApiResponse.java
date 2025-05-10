package com.example.quizcraft.api;

import com.example.quizcraft.Category;
import com.example.quizcraft.User;

import java.util.List;

public class ApiResponse {
    public boolean success;
    public String message;
    public User user;
    public List<Category> categories;
}
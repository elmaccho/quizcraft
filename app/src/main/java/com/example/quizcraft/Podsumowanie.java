package com.example.quizcraft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Podsumowanie extends AppCompatActivity {

    private TextView tvCategory, tvScore, tvSummary, tvUsername;
    private Button btnMainPage;
    private int score, totalQuestions;
    private String categoryName;
    private boolean is1v1;
    private int winnerId;
    private int opponentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.podsumowanie);

        tvCategory = findViewById(R.id.tv_podsumowanie);
        tvScore = findViewById(R.id.textView);
        tvUsername = findViewById(R.id.username_tv);
        btnMainPage = findViewById(R.id.bt_stronaglowna);
        tvSummary = findViewById(R.id.tv_summary); // Zakładam, że istnieje pole TextView o id tv_summary w layout

        score = getIntent().getIntExtra("SCORE", 0);
        totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 5);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        is1v1 = getIntent().getBooleanExtra("IS_1V1", false);
        winnerId = getIntent().getIntExtra("WINNER_ID", -1);
        opponentScore = getIntent().getIntExtra("OPPONENT_SCORE", -1);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = prefs.getString("username", "Nieznany użytkownik");
        int userId = prefs.getInt("userId", -1);

        tvCategory.setText("Kategoria: " + categoryName);
        tvScore.setText(score + "/" + totalQuestions);
        tvUsername.setText(username);

        if (is1v1 && opponentScore != -1) {
            String summaryText;
            if (winnerId == -1) {
                summaryText = "Remis! Twój wynik: " + score + ", Wynik przeciwnika: " + opponentScore;
            } else if (winnerId == userId) {
                summaryText = "Wygrałeś! Twój wynik: " + score + ", Wynik przeciwnika: " + opponentScore;
            } else {
                summaryText = "Przegrałeś! Twój wynik: " + score + ", Wynik przeciwnika: " + opponentScore;
            }
            tvSummary.setText(summaryText);
        } else {
            tvSummary.setText("");
        }

        btnMainPage.setOnClickListener(v -> {
            Intent intent = new Intent(Podsumowanie.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
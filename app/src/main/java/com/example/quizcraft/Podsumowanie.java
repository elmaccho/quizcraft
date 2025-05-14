package com.example.quizcraft;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Podsumowanie extends AppCompatActivity {

    private TextView tvCategory, tvScore, tvSummary, tvUsername;
    private Button btnMainPage, btnPlayAgain;
    private int score, totalQuestions;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.podsumowanie);

        tvCategory = findViewById(R.id.tv_podsumowanie);
        tvScore = findViewById(R.id.textView);
        tvUsername = findViewById(R.id.username_tv);
        btnMainPage = findViewById(R.id.bt_stronaglowna);
        btnPlayAgain = findViewById(R.id.bt_zagrajponownie);

        score = getIntent().getIntExtra("SCORE", 0);
        totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 5);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        tvCategory.setText("Kategoria: " + categoryName);
        tvScore.setText(score + "/" + totalQuestions);
        tvUsername.setText("");

        btnMainPage.setOnClickListener(v -> {
            Intent intent = new Intent(Podsumowanie.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(Podsumowanie.this, GameActivity.class);
            intent.putExtra("CATEGORY_ID", getIntent().getIntExtra("CATEGORY_ID", -1));
            intent.putExtra("CATEGORY_NAME", categoryName);
            startActivity(intent);
            finish();
        });
    }
}

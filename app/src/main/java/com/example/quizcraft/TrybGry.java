package com.example.quizcraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrybGry extends AppCompatActivity {

    Button soloBtn, oneVs1Btn;
    int categoryId;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tryby_gry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        if (categoryId == -1) {
            finish();
            return;
        }

        soloBtn = findViewById(R.id.button_solo);
        oneVs1Btn = findViewById(R.id.button_1v1);

        soloBtn.setOnClickListener(v -> {
            Intent intent = new Intent(TrybGry.this, GameActivity.class);
            intent.putExtra("CATEGORY_ID", categoryId);
            intent.putExtra("CATEGORY_NAME", categoryName);
            startActivity(intent);
            finish();
        });

        oneVs1Btn.setOnClickListener(v -> {
            Intent intent = new Intent(TrybGry.this, Ladowanie.class);
            intent.putExtra("CATEGORY_ID", categoryId);
            intent.putExtra("CATEGORY_NAME", categoryName);
            startActivity(intent);
        });
    }
}
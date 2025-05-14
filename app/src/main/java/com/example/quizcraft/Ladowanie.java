package com.example.quizcraft;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ladowanie extends AppCompatActivity {

    TextView tv_czas;
    Button btn_zagrajsam, btn_powrot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ladowanie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv_czas = findViewById(R.id.tv_czas);
        btn_zagrajsam = findViewById(R.id.btn_zagrajsam);
        btn_powrot = findViewById(R.id.btn_powrot);

        btn_powrot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Ladowanie.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        new CountDownTimer(60000, 1000) {
            int czas = 0;

            @Override
            public void onTick(long l) {
                String formattedTime = String.format("0:%02d", czas);
                tv_czas.setText(formattedTime);
                czas++;
            }

            @Override
            public void onFinish() {
                tv_czas.setText("Nie znalazło graczy");
            }
        }.start();
    }
}

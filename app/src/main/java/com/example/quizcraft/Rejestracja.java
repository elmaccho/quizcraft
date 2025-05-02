package com.example.quizcraft;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizcraft.api.Api;
import com.example.quizcraft.api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rejestracja extends AppCompatActivity {

    private EditText etName, etNickname, etEmail, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rejestracja);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.a2_et_name);
        etNickname = findViewById(R.id.a2_et_nickname);
        etEmail = findViewById(R.id.a2_et_email);
        etPassword = findViewById(R.id.a2_et_password);
        btnRegister = findViewById(R.id.a2_btn_register);

        TextView a2_tv_login = findViewById(R.id.a2_tv_login);
        a2_tv_login.setOnClickListener(view -> {
           finish();
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etNickname.getText().toString();
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Rejestracja.this, "Wszystkie pola muszą być wypełnione!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(username, name, email, password);

                Api apiService = RetrofitClient.getRetrofitInstance().create(Api.class);

                Call<ResponseBody> call = apiService.registerUser(
                        user.getUsername(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        ""
                );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Rejestracja.this, "Rejestracja zakończona sukcesem!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Rejestracja.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                            System.out.println("HTTP error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(Rejestracja.this, "Błąd połączenia z serwerem", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}
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

import com.example.quizcraft.api.ApiResponse;
import com.example.quizcraft.api.RejestracjaApi;
import com.example.quizcraft.api.RetrofitClient;
import com.google.gson.Gson;

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
        a2_tv_login.setOnClickListener(view -> finish());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etNickname.getText().toString();
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    CustomToast.showToast(Rejestracja.this, "Wypełnij wszystkie pola!", R.drawable.logo, Toast.LENGTH_LONG);
                    return;
                }

                User user = new User(username, name, email, password);

                RejestracjaApi apiService = RetrofitClient.getRetrofitInstance().create(RejestracjaApi.class);

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
                            try {
                                // Sprawdzamy, czy odpowiedź nie jest pusta
                                if (response.body() != null) {
                                    String responseBody = response.body().string();

                                    // Logujemy odpowiedź, aby upewnić się, że dostajemy prawidłowy JSON
                                    Log.d("ResponseBody", responseBody);

                                    // Parsowanie JSON na obiekt ApiResponse
                                    Gson gson = new Gson();
                                    ApiResponse apiResponse = gson.fromJson(responseBody, ApiResponse.class);

                                    // Sprawdzamy, czy komunikat jest prawidłowy
                                    if (apiResponse != null && apiResponse.message != null) {
                                        // Wyświetlamy komunikat użytkownikowi
                                        CustomToast.showToast(Rejestracja.this, apiResponse.message, R.drawable.logo, Toast.LENGTH_LONG);
                                    } else {
                                        CustomToast.showToast(Rejestracja.this, "Błąd rejestracji: Nieznany błąd.", R.drawable.logo, Toast.LENGTH_LONG);
                                    }
                                } else {
                                    CustomToast.showToast(Rejestracja.this, "Błąd odpowiedzi z serwera.", R.drawable.logo, Toast.LENGTH_LONG);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                CustomToast.showToast(Rejestracja.this, "Błąd w trakcie przetwarzania odpowiedzi.", R.drawable.logo, Toast.LENGTH_LONG);
                            }
                        } else {
                            // Logujemy kod odpowiedzi, aby wiedzieć, jaki błąd wystąpił
                            Log.e("HTTP Error", "Error Code: " + response.code());
                            CustomToast.showToast(Rejestracja.this, "Błąd rejestracji. Kod błędu: " + response.code(), R.drawable.logo, Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        CustomToast.showToast(Rejestracja.this, "Błąd połączenia z serwerem.", R.drawable.logo, Toast.LENGTH_LONG);
                        t.printStackTrace();
                    }
                });
            }
        });
    }
}
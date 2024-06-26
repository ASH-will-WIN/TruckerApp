package com.example.truckerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneEditText, passwordEditText;
    private Button loginButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").build();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = db.userDao().findByPhoneNumber(phone);
                        if (user != null && user.password.equals(password)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    passwordEditText.setError("Invalid phone number or password");
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}

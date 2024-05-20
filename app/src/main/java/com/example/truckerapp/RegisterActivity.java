package com.example.truckerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, passwordEditText;
    private Button registerButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").build();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString().trim();
                final String phone = phoneEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User(name, phone, password, 0, 0);
                        db.userDao().insert(user);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                }).start();
            }
        });
    }
}

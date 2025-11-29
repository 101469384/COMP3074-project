package com.example.moveinn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvCreateAccount;


    private Button btnOpenCompare;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MoveInPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        TextView tvGuestLogin = findViewById(R.id.tvGuestLogin);
        btnOpenCompare = findViewById(R.id.btnOpenCompare);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvGuestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("USER_NAME", "Guest");
                intent.putExtra("USER_TYPE", "Guest");
                startActivity(intent);
            }
        });


        btnOpenCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompareListingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Check MockDatabase first
        for (User user : MockDatabase.getAllUsers()) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                Toast.makeText(this, "Login successful (Test User)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("USER_NAME", user.getName());
                intent.putExtra("USER_TYPE", user.getAccountType());
                startActivity(intent);
                return;
            }
        }

        // 2. Fallback to SharedPreferences (Last registered user)
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, null);
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, null);
        String savedName = sharedPreferences.getString("name", "User"); // Assuming "name" key exists from RegisterActivity
        String savedType = sharedPreferences.getString("account_type", "Unknown");

        if (savedEmail != null && savedPassword != null) {
            if (email.equals(savedEmail) && password.equals(savedPassword)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("USER_NAME", savedName);
                intent.putExtra("USER_TYPE", savedType);
                startActivity(intent);
                return;
            }
        }

        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
    }
}


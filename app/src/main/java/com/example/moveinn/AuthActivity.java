package com.example.moveinn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthActivity extends AppCompatActivity {

    private Button loginChoiceButton;
    private Button signUpChoiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        loginChoiceButton = findViewById(R.id.btnGoToLogin);
        signUpChoiceButton = findViewById(R.id.btnGoToSignUp);

        loginChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginFlow();
            }
        });

        signUpChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginRegistrationFlow();
            }
        });
    }


    private void goToLoginFlow() {
        Intent loginIntent = new Intent(AuthActivity.this, MainActivity.class);

        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        startActivity(loginIntent);
    }


    private void beginRegistrationFlow() {
        Intent registerIntent = new Intent(AuthActivity.this, RegisterActivity.class);

        registerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(registerIntent);
    }
}
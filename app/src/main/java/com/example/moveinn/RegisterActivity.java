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
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterName, etRegisterEmail, etRegisterPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;

    private RadioGroup regAccountType;
    private RadioButton CustomerAccount, VendorAccount;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MoveInPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_ACCOUNT_TYPE= "account_type";

    private TextView VendorNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterName = findViewById(R.id.etRegisterName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        regAccountType= findViewById(R.id.regAccountType);
        CustomerAccount= findViewById(R.id.CustomerAccount);
        VendorAccount= findViewById(R.id.VendorAccount);

        VendorNotice = findViewById(R.id.VendorNotice);

        regAccountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                if(checkedId == VendorAccount.getId()){
                    VendorNotice.setVisibility(View.VISIBLE);
                    } else{
                        VendorNotice.setVisibility(View.GONE);
                    }
                }
            }
        );

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleRegister() {
        String name = etRegisterName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();

        int selectedId= regAccountType.getCheckedRadioButtonId();
        String accountType= "";
        if(selectedId == CustomerAccount.getId()){
            accountType= "I need moving services";
        } else if (selectedId == VendorAccount.getId()){
            accountType= "I am truck owner/ vendor";
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_ACCOUNT_TYPE, accountType);
        editor.apply();

        Toast.makeText(this, "Account created. You can now login.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}



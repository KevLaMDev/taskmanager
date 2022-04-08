package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.kevdev.taskmaster.R;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "Login Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpLoginButton();
        setUpSignUpButton();
    }

    public void setUpLoginButton() {
        Button loginSubmitButton = findViewById(R.id.loginSubmitButton);
        EditText emailEditText = findViewById(R.id.loginEmailEditText);
        EditText passwordEditText = findViewById(R.id.loginPasswordEditText);
        String email = getIntent().getStringExtra(VerifyAccountActivity.VERIFY_EMAIL_TAG);
        if (email != null) emailEditText.setText(email);

        loginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                Amplify.Auth.signIn(emailEditText.getText().toString(),
                        password,
                        success ->
                        {
                            Log.i(TAG, "Login success");
                            Intent goToMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(goToMainActivity);
                        },
                        failure ->
                        {
                            Log.i(TAG, "Login failed");
                            runOnUiThread(() -> {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            });
                        }
                );
            }
        });
    }

    public void setUpSignUpButton() {
        Button goToSignUpActivityButton = findViewById(R.id.goToSignUpButton);
        goToSignUpActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(goToSignUpActivity);
            }
        });
    }
}
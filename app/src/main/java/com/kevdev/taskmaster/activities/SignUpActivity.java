package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.kevdev.taskmaster.R;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUp Activity";
    public static final String SIGNUP_EMAIL_TAG = "Signup_email_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpSignUpButton();
    }

    public void setUpSignUpButton() {
        Button submitSignUp = findViewById(R.id.signUpSubmitButton);
        submitSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailEditText = findViewById(R.id.signUpEmailEditText);
                String email = emailEditText.getText().toString();
                EditText passwordEditText = findViewById(R.id.signUpPasswordEditText);
                String password = passwordEditText.getText().toString();

                Amplify.Auth.signUp(email,
                password,
                AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(), email)
                        .userAttribute(AuthUserAttributeKey.nickname(), "placeholder")
                        .build(),
                    good ->
                    {
                        Log.i(TAG, "Signup success: " + good.toString());
                        Intent goToVerifyAccountActivity = new Intent(SignUpActivity.this, VerifyAccountActivity.class);
                        goToVerifyAccountActivity.putExtra(SIGNUP_EMAIL_TAG, email);
                        startActivity(goToVerifyAccountActivity);
                    },
                    bad ->
                    {
                        Log.i(TAG, "signup failed");
                        runOnUiThread(() -> {
                            Toast.makeText(SignUpActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                        });
                    }
                );
            }
        });
    }
}
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

public class VerifyAccountActivity extends AppCompatActivity {
    public static final String TAG = "VerifyAccountActivity";
    public static final String VERIFY_EMAIL_TAG = "Verify_email_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        setUpVerifyAccountSubmit();
    }

    public void setUpVerifyAccountSubmit() {
        Button verifyAccountSubmit = findViewById(R.id.verifySubmitButton);
        EditText passcodeEditText = findViewById(R.id.VerificationCodeEditText);
        Intent passedData = getIntent();
        String passedEmail = passedData.getStringExtra(SignUpActivity.SIGNUP_EMAIL_TAG);
        EditText emailEditText = findViewById(R.id.verifyEmailEditText);
        emailEditText.setText(passedEmail);
        verifyAccountSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passcodeString = passcodeEditText.getText().toString();
                Amplify.Auth.confirmSignUp(passedEmail,
                    passcodeString,
                    success ->
                    {
                        Log.i(TAG, "Verification success");
                        Intent goToLoginActivity = new Intent(VerifyAccountActivity.this, LoginActivity.class);
                        goToLoginActivity.putExtra(VERIFY_EMAIL_TAG, passedEmail);
                        startActivity(goToLoginActivity);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Verification failed");
                        runOnUiThread(() -> {
                            Toast.makeText(VerifyAccountActivity.this, "Verification Unsuccessful", Toast.LENGTH_SHORT).show();
                        });
                    });
            }
        });
    }
}
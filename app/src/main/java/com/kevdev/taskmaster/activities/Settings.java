package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kevdev.taskmaster.R;

public class Settings extends AppCompatActivity {
    SharedPreferences preferences;
    public static final String USERNAME_TAG = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get preference manager
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(USERNAME_TAG, "");

        if (!username.isEmpty()) {
            EditText usernameEditTest = (EditText) findViewById(R.id.textEditUsername);
            usernameEditTest.setText(username);
        }
        Button saveButton = findViewById(R.id.usernameSubmitButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferencesEditor = preferences.edit();
                EditText usernameEditText = (EditText) findViewById(R.id.textEditUsername);
                String username = usernameEditText.getText().toString();
                preferencesEditor.putString(USERNAME_TAG, username);
                preferencesEditor.apply();
            }
        });
    }
}
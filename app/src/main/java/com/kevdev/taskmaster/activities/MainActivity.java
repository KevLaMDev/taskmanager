package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kevdev.taskmaster.R;

//import com.kevdev.taskmaster.R;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get component ID
        Button task1Button = (Button) findViewById(R.id.task1NavButton);
        Button task2Button = (Button) findViewById(R.id.task2NavButton);
        Button task3Button = (Button) findViewById(R.id.task3NavButton);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsNavButton);

        // Set OnClickListener
        task1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // define on click callback
                Intent goToTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
                String value = "Task 1";
                goToTaskDetail.putExtra(TASK_NAME_EXTRA_TAG, value);
                startActivity(goToTaskDetail);
            }
        });
        task2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
                goToTaskDetail.putExtra(TASK_NAME_EXTRA_TAG, "Task 2");
                startActivity(goToTaskDetail);
            }
        });
        task3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetail = new Intent(MainActivity.this, TaskDetail.class);
                goToTaskDetail.putExtra(TASK_NAME_EXTRA_TAG, "Task 3");
                startActivity(goToTaskDetail);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetail = new Intent(MainActivity.this, Settings.class);
                startActivity(goToTaskDetail);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        String username = preferences.getString(Settings.USERNAME_TAG, "");
        TextView mainPageHeader = findViewById(R.id.mainPageHeader);
        mainPageHeader.setText(username + "\'s tasks");
    }
}
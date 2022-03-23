package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kevdev.taskmaster.R;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        // Access data passed via intent
        Intent passedData = getIntent();
        String taskName;
        if (passedData != null) {
            taskName = passedData.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
            TextView titleTextView = (TextView) findViewById(R.id.taskDetailHeader);
            titleTextView.setText(taskName);
        }
    }
}
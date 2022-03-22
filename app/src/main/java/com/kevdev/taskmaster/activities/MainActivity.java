package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kevdev.taskmaster.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get component ID
        Button addTaskButton = (Button) findViewById(R.id.addTaskButton);
        Button getAllTasksButton = (Button) findViewById(R.id.allTasksButton);
        // Set OnClickListener
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // define on click callback
                Intent goToAddTaskActivity = new Intent(MainActivity.this, AllTasks.class);
                startActivity(goToAddTaskActivity);
            }
        });
        getAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAllTasksActivity = new Intent(MainActivity.this, AllTasks.class);
                startActivity(goToAllTasksActivity);
            }
        });
    }
}
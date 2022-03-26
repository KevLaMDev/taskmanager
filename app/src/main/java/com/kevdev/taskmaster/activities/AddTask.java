package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kevdev.taskmaster.Model.Task;
import com.kevdev.taskmaster.R;
import com.kevdev.taskmaster.database.TaskmasterDatabase;
import com.kevdev.taskmaster.database.TaskmasterDatabase_Impl;

public class AddTask extends AppCompatActivity {

    TaskmasterDatabase taskmasterDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskmasterDatabase = Room.databaseBuilder(getApplicationContext(), TaskmasterDatabase.class, "task_master").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        Button submitButton = (Button) findViewById(R.id.submitNewTaskButton);
        TextView confirmation = (TextView) findViewById(R.id.addTaskTextView);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitle);
                String taskTitle = taskTitleEditText.getText().toString();
                EditText taskBodyEditText = (EditText) findViewById(R.id.taskBody);
                String taskBody = taskBodyEditText.getText().toString();
                Task newTask = new Task(taskTitle, taskBody, Task.State.NEW);
                taskmasterDatabase.taskDAO().insertATask(newTask);
                Intent goToMainActivity = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToMainActivity);
            }
        });
    }
}
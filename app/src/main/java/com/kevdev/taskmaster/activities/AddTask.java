package com.kevdev.taskmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kevdev.taskmaster.enums.State;
import com.kevdev.taskmaster.model.Task;
import com.kevdev.taskmaster.R;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button submitButton = (Button) findViewById(R.id.submitNewTaskButton);
        TextView confirmation = (TextView) findViewById(R.id.addTaskTextView);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitle);
                String taskTitle = taskTitleEditText.getText().toString();
                EditText taskBodyEditText = (EditText) findViewById(R.id.taskBody);
                String taskBody = taskBodyEditText.getText().toString();
                Task newTask = new Task(taskTitle, taskBody, State.NEW);
                //TODO convert to Dynamo query
//                taskmasterDatabase.taskDAO().insertATask(newTask);
                Intent goToMainActivity = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToMainActivity);
            }
        });
    }
}
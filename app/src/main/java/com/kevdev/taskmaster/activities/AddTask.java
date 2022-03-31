package com.kevdev.taskmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.kevdev.taskmaster.R;

public class AddTask extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";

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
                Task newTask = Task.builder()
                        .title(taskTitle)
                        .body(taskBody)
                        .taskState(State.COMPLETE)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(newTask),
                        successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): made a new task successfully"),
                        failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed to make a new task")
                );
                Intent goToMainActivity = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToMainActivity);
            }
        });
    }
}
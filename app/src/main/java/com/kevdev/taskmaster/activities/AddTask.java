package com.kevdev.taskmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.datastore.syncengine.Orchestrator;
import com.kevdev.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AddTask extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    public List<Team> teams = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button submitButton = (Button) findViewById(R.id.submitNewTaskButton);
        TextView confirmation = (TextView) findViewById(R.id.addTaskTextView);
        Spinner taskStateSpinner = findViewById(R.id.addTaskSpinner);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                State.values()));
        Spinner taskTeamSpinner = findViewById(R.id.teamSpinner);
        List<Team> dbTeams = new ArrayList<>();
        List<String> dbTeamNames = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "successful READ of teams from DB");
                    if (success.getData() != null) {
                        for (Team team : success.getData()) {
                            dbTeams.add(team);
                            dbTeamNames.add(team.getName());
                        }
                        runOnUiThread(() ->
                        {
                            taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    dbTeamNames
                            ));
                        });
                    }
                },
                failure -> Log.i(TAG, "failure to READ tasks from DB")
        );


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitle);
                String taskTitle = taskTitleEditText.getText().toString();
                EditText taskBodyEditText = (EditText) findViewById(R.id.taskBody);
                String taskBody = taskBodyEditText.getText().toString();
                Spinner taskStateSpinner = findViewById(R.id.addTaskSpinner);
                String nameOfSelectedTeam = taskTeamSpinner.getSelectedItem().toString();
                Team selectedTeam = dbTeams.stream().filter(team -> team.getName().equals(nameOfSelectedTeam)).findFirst().orElseThrow(RuntimeException::new);
                Task newTask = Task.builder()
                        .title(taskTitle)
                        .body(taskBody)
                        .taskState((State) taskStateSpinner.getSelectedItem())
                        .team(selectedTeam)
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
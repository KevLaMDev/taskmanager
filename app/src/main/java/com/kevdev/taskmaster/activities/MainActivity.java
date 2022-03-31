package com.kevdev.taskmaster.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.kevdev.taskmaster.R;
import com.kevdev.taskmaster.adapters.MainActivityRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    public static final String TASK_BODY_EXTRA_TAG = "taskBody";
    public static final String TAG = "MainActivity";
    SharedPreferences preferences;
    MainActivityRecyclerViewAdapter adapter;
    List<Task> tasks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Get component ID
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsNavButton);
        // DB proof of life:
//        com.amplifyframework.datastore.generated.model.Task testTask =
//                com.amplifyframework.datastore.generated.model.Task.builder()
//                    .title("test task")
//                    .body("test body")
//                    .taskState(com.amplifyframework.datastore.generated.model.State.NEW)
//                    .build();
//        Amplify.API.mutate(ModelMutation.create(testTask),
//                    successResponse -> Log.i(TAG, "MainActivity.onCreate(): made a task successfully"),
//                    failureResponse -> Log.i(TAG, "MainActivity.onCreate(): failed with " + failureResponse)
//                );
        // Set OnClickListener
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetail = new Intent(MainActivity.this, Settings.class);
                startActivity(goToTaskDetail);
            }
        });

        setUpMainActivityRecyclerView();
        setUpAddTaskActivity();
    }

    private void setUpAddTaskActivity() {
        Button addTaskButton = (Button) findViewById(R.id.addTaskNavButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddTaskForm = new Intent(MainActivity.this, AddTask.class);
                startActivity(goToAddTaskForm);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        String username = preferences.getString(Settings.USERNAME_TAG, "");
        TextView mainPageHeader = findViewById(R.id.mainPageHeader);
        List<Task> dbTasks = new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "successful READ of tasks from DB");
                    for (Task dbTask : success.getData()) {
                        dbTasks.add(dbTask);
                    }
                    tasks = dbTasks;
                    runOnUiThread(() ->
                    {
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "failure to READ tasks from DB")
        );
        setUpMainActivityRecyclerView();
        mainPageHeader.setText(username + "\'s tasks");
    }

    private void setUpMainActivityRecyclerView() {
        // Step 1A: Get recycler by Id
        RecyclerView mainActivityRecyclerView = findViewById(R.id.mainPageRecycleView);
        // Step 1B: Set layout manager of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainActivityRecyclerView.setLayoutManager(layoutManager);
        // Step 1C: Create adapter class and import as global instance var
        adapter = new MainActivityRecyclerViewAdapter(tasks, this);
        // set adapter on recyclerView UI element
        mainActivityRecyclerView.setAdapter(adapter);
    }

}
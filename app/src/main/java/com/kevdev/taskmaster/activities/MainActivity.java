package com.kevdev.taskmaster.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kevdev.taskmaster.enums.State;
import com.kevdev.taskmaster.model.Task;
import com.kevdev.taskmaster.R;
import com.kevdev.taskmaster.adapters.MainActivityRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

//import com.kevdev.taskmaster.R;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    public static final String TASK_BODY_EXTRA_TAG = "taskBody";
    SharedPreferences preferences;
    MainActivityRecyclerViewAdapter adapter;

    List<Task> tasks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //TODO convert to dynamo query
//        tasks = taskmasterDatabase.taskDAO().findAll();
        // Get component ID
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsNavButton);

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
        setUpMainActivityRecyclerView();
        mainPageHeader.setText(username + "\'s tasks");
        //TODO convert to Dynamo query
//        tasks = taskmasterDatabase.taskDAO().findAll();
    }

    private void setUpMainActivityRecyclerView() {
        // Step 1A: Get recycler by Id
        RecyclerView mainActivityRecyclerView = findViewById(R.id.mainPageRecycleView);
        // Step 1B: Set layout manager of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainActivityRecyclerView.setLayoutManager(layoutManager);
        // Step 1C: Create adapter class and import as global instance var
        List<Task> defaultTasks = new ArrayList<>();
        defaultTasks.add(new Task("Clean Dishes", "No dishes left pls do asap", State.NEW));
        defaultTasks.add(new Task("Grind leetcode", "It's gonna suck but you gotta do it", State.NEW));
        defaultTasks.add(new Task("test", "test", State.NEW));
        adapter = new MainActivityRecyclerViewAdapter(defaultTasks, this);
        // set adapter on recyclerView UI element
        mainActivityRecyclerView.setAdapter(adapter);
    }

}
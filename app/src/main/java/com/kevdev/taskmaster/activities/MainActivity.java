package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kevdev.taskmaster.Model.Task;
import com.kevdev.taskmaster.R;
import com.kevdev.taskmaster.adapters.MainActivityRecyclerViewAdapter;
import com.kevdev.taskmaster.database.TaskmasterDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

//import com.kevdev.taskmaster.R;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    SharedPreferences preferences;
    MainActivityRecyclerViewAdapter adapter;
    TaskmasterDatabase taskmasterDatabase;
    List<Task> tasks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskmasterDatabase = Room.databaseBuilder(getApplicationContext(), TaskmasterDatabase.class, "task_master").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        tasks = taskmasterDatabase.taskDAO().findAll();
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
        tasks = taskmasterDatabase.taskDAO().findAll();
    }



    private void setUpMainActivityRecyclerView() {
        // Step 1A: Get recycler by Id
        RecyclerView mainActivityRecyclerView = findViewById(R.id.mainPageRecycleView);
        // Step 1B: Set layout manager of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainActivityRecyclerView.setLayoutManager(layoutManager);
        // Step 1C: Create adapter class and import as global instance var
        List<Task> defaultTasks = new ArrayList<>();
        defaultTasks.add(new Task("Clean Dishes", "No dishes left pls do asap", Task.State.NEW));
        defaultTasks.add(new Task("Grind leetcode", "It's gonna suck but you gotta do it", Task.State.NEW));
        defaultTasks.add(new Task("test", "test", Task.State.NEW));
        adapter = new MainActivityRecyclerViewAdapter(tasks, this);
        // set adapter on recyclerView UI element
        mainActivityRecyclerView.setAdapter(adapter);
    }

}
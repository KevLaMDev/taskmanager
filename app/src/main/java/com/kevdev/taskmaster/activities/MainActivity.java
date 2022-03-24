package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kevdev.taskmaster.Model.Task;
import com.kevdev.taskmaster.R;
import com.kevdev.taskmaster.adapters.MainActivityRecyclerViewAdapter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

//import com.kevdev.taskmaster.R;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    SharedPreferences preferences;
    MainActivityRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

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

    }

    @Override
    protected void onResume() {
        super.onResume();

        String username = preferences.getString(Settings.USERNAME_TAG, "");
        TextView mainPageHeader = findViewById(R.id.mainPageHeader);
        mainPageHeader.setText(username + "\'s tasks");
        Task.State taskState = Task.State.ASSIGNED;
        Task newTask = new Task("body", "title", taskState);
        System.out.println(taskState);
    }

    private void setUpMainActivityRecyclerView() {
        // Step 1A: Get recycler by Id
        RecyclerView mainActivityRecyclerView = findViewById(R.id.mainPageRecycleView);
        // Step 1B: Set layout manager of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainActivityRecyclerView.setLayoutManager(layoutManager);
        // Step 1C: Create adapter class and import as global instance var
        List<Task> tasks = new ArrayList<>();
        Task.State taskState = Task.State.NEW;
        Task.State taskState1 = Task.State.NEW;
        tasks.add(new Task("Clean Dishes", "No dishes left pls do asap", taskState));
        tasks.add(new Task("Grind leetcode", "It's gonna suck but you gotta do it", taskState1));
        tasks.add(new Task("test", "test", taskState));
        adapter = new MainActivityRecyclerViewAdapter(tasks, this);
        // set adapter on recyclerView UI element
        mainActivityRecyclerView.setAdapter(adapter);
    }

}
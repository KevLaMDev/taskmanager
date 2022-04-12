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

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.kevdev.taskmaster.R;
import com.kevdev.taskmaster.adapters.MainActivityRecyclerViewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    public static final String TASK_BODY_EXTRA_TAG = "taskBody";
    public static final String TASK_IMAGES3_EXTRA_TAG = "imageS3Key";
    public static final String TAG = "MainActivity";
    SharedPreferences preferences;
    MainActivityRecyclerViewAdapter adapter;
    CompletableFuture<List<Task>> taskCompletableFuture = null;
    List<Task> dbTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        taskCompletableFuture = new CompletableFuture<>();
        String teamName = preferences.getString(Settings.TEAM_TAG, "No team name");
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "successful READ of tasks from DB");
                    if (success.getData() != null) {
                        for (Task dbTask : success.getData()) {
                            if (dbTask.getTeam().getName().equals(teamName)) dbTasks.add(dbTask);
                        }
                        taskCompletableFuture.complete(dbTasks);
                        runOnUiThread(() ->
                        {
                            adapter.notifyDataSetChanged();
                        });
                    }
                },
                failure -> {
                    taskCompletableFuture.complete(null);
                    Log.i(TAG, "failure to READ tasks from DB");
                }
        );

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("openedApp")
                .addProperty("timeOpened", Long.toString(new Date().getTime()))
                .addProperty("eventDescription", "Opened MainActivity")
                .build();
        Amplify.Analytics.recordEvent(event);

        setUpSettingsButtonActivity();
        try {
            setUpMainActivityRecyclerView();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setUpAddTaskActivity();
        setUpLoginButton();
        setUpLogOutButton();
    }


    private void setUpLoginButton() {
        Button goToLoginActivityButton = findViewById(R.id.loginButton);
        goToLoginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(goToLoginActivity);
            }
        });
    }

    private void setUpSettingsButtonActivity() {
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsNavButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTaskDetail = new Intent(MainActivity.this, Settings.class);
                startActivity(goToTaskDetail);
            }
        });
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

    private void setUpLogOutButton() {
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amplify.Auth.signOut(
                        () -> {
                                Log.i(TAG, "Logout success");
                                Intent goToLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(goToLoginActivity);
                              },
                        failure -> {Log.i(TAG, "Logout failed");}
                );
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView mainPageHeader = findViewById(R.id.mainPageHeader);
        String teamName = preferences.getString(Settings.TEAM_TAG, "No team name");
        mainPageHeader.setText(teamName + "\'s tasks");
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        if (Amplify.Auth.getCurrentUser() != null) {
            String username = Amplify.Auth.getCurrentUser().getUsername();
            usernameTextView.setText(username);
        }
        dbTasks.clear();
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "successful READ of tasks from DB");
                    if (success.getData() != null) {
                        for (Task dbTask : success.getData()) {
                            if (dbTask.getTeam().getName().equals(teamName)) dbTasks.add(dbTask);
                        }
                        taskCompletableFuture.complete(dbTasks);
                        runOnUiThread(() ->
                        {
                            adapter.notifyDataSetChanged();
                        });
                    }
                },
                failure -> {
                    taskCompletableFuture.complete(null);
                    Log.i(TAG, "failure to READ tasks from DB");
                }
        );

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("resumedMainActivity")
                .addProperty("timeResumed", Long.toString(new Date().getTime()))
                .addProperty("eventDescription", "Resumed MainActivity")
                .build();
        Amplify.Analytics.recordEvent(event);

        try {
            setUpMainActivityRecyclerView();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setUpMainActivityRecyclerView() throws ExecutionException, InterruptedException {
        // Step 1A: Get recycler by Id
        RecyclerView mainActivityRecyclerView = findViewById(R.id.mainPageRecycleView);
        // Step 1B: Set layout manager of RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainActivityRecyclerView.setLayoutManager(layoutManager);
        List<Task> taskList = taskCompletableFuture.get();
        String teamName = preferences.getString(Settings.TEAM_TAG, "No team name");
        adapter = new MainActivityRecyclerViewAdapter(taskList, this);
        // set adapter on recyclerView UI element
        mainActivityRecyclerView.setAdapter(adapter);
    }

}
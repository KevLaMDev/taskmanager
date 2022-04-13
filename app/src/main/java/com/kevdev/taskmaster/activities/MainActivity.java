package com.kevdev.taskmaster.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
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
    private InterstitialAd mInterstitialAd = null;
    private RewardedAd mRewardedAd = null;
    private int userRewardPoints = 0;

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
        TextView rewardTextView = findViewById(R.id.rewardsTextView);
        rewardTextView.setText("Reward Points: " + userRewardPoints);
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
        setUpAds();
        setUpAddTaskActivity();
        setUpLoginButton();
        setUpLogOutButton();
    }

    private void setUpAds() {

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView bannerAdView = (AdView) findViewById(R.id.bannerAdView);
        AdRequest adR = new AdRequest.Builder().build();
        bannerAdView.loadAd(adR);

        AdRequest adRequestInterstitialAd = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequestInterstitialAd,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
        Button interstitialAdButton = (Button) findViewById(R.id.interstitialAdButton);
        interstitialAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd != null) mInterstitialAd.show(MainActivity.this);
            }
        });

        AdRequest rewardAdRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-5610459448820556/3125702073",
                rewardAdRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });
                    }
                });

        Button rewardAdButton = findViewById(R.id.rewardAdButton);
        rewardAdButton.setOnClickListener(b -> {
            if (mRewardedAd != null) {
                mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        int rewardAmount = rewardItem.getAmount();
                        String rewardType = rewardItem.getType();
                        Log.d(TAG, "The user earned the reward. Amount is: " + rewardAmount + ", and type is: " + rewardType);
                    }
                });
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.");
            }
        });
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
        TextView rewardTextView = findViewById(R.id.rewardsTextView);
        rewardTextView.setText("Reward Points: " + userRewardPoints);
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
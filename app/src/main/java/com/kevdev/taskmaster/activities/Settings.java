package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.kevdev.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {
    SharedPreferences preferences;
    public static final String TEAM_TAG = "team";
    public static final String TAG = "Settings Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get preference manager
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String teamName = preferences.getString(TEAM_TAG, "");

//        if (!teamName.isEmpty()) {
//            EditText usernameEditTest = (EditText) findViewById(R.id.textEditUsername);
//            usernameEditTest.setText(teamName);
//        }

        Spinner teamNameSpinner = findViewById(R.id.teamNameSpinnerSettings);
        List<String> dbTeamNames = new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "successful READ of teams from DB");
                    if (success.getData() != null) {
                        for (Team team : success.getData()) {
                            dbTeamNames.add(team.getName());
                        }
                        runOnUiThread(() ->
                        {
                            teamNameSpinner.setAdapter(new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    dbTeamNames
                            ));
                        });
                    }
                },
                failure -> Log.i(TAG, "failure to READ tasks from DB")
        );

        Button saveButton = findViewById(R.id.usernameSubmitButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor preferencesEditor = preferences.edit();
                String teamName = teamNameSpinner.getSelectedItem().toString();
                preferencesEditor.putString(TEAM_TAG, teamName);
                preferencesEditor.apply();
            }
        });
    }
}
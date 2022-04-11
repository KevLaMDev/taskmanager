package com.kevdev.taskmaster.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.State;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.kevdev.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class AddTask extends AppCompatActivity {
    public static final String TAG = "AddTaskActivity";
    ActivityResultLauncher<Intent> activityResultLauncher;
    String taskImageS3Key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        activityResultLauncher = getImageSelectionActivityResultLauncher();
        setUpAddImageButton();
        Button submitButton = findViewById(R.id.submitNewTaskButton);

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
                EditText taskTitleEditText = findViewById(R.id.taskTitle);
                String taskTitle = taskTitleEditText.getText().toString();
                EditText taskBodyEditText = findViewById(R.id.taskBody);
                String taskBody = taskBodyEditText.getText().toString();
                Spinner taskStateSpinner = findViewById(R.id.addTaskSpinner);
                String nameOfSelectedTeam = taskTeamSpinner.getSelectedItem().toString();
                Team selectedTeam = dbTeams.stream().filter(team -> team.getName().equals(nameOfSelectedTeam)).findFirst().orElseThrow(RuntimeException::new);
                Task newTask = Task.builder()
                        .title(taskTitle)
                        .body(taskBody)
                        .taskState((State) taskStateSpinner.getSelectedItem())
                        .team(selectedTeam)
                        .productImageS3Key(taskImageS3Key)
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

    private void imageSelectionIntentLauncher() {
        Intent imageFileSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFileSelectionIntent.setType("*/*");
        imageFileSelectionIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        activityResultLauncher.launch(imageFileSelectionIntent);
    }

    private void setUpAddImageButton() {
        Button addImageButton = findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelectionIntentLauncher();
            }
        });
    }

    private ActivityResultLauncher<Intent> getImageSelectionActivityResultLauncher() {
        ActivityResultLauncher<Intent> imageSelectionActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (result.getData() != null) {
                                Uri selectedImageUri = result.getData().getData();
                                try {
                                    InputStream selectedImageInputStream = getContentResolver().openInputStream(selectedImageUri);
                                    String selectedImageFileName = getFileNameFromUri(selectedImageUri);
                                    Log.i(TAG, "Succeeded in getting input stream from file");
                                    uploadInputStreamToS3(selectedImageInputStream, selectedImageFileName);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            Log.e(TAG, "Activity result error in ActivityResultLauncher");
                        }
                    }
                }
        );
        return imageSelectionActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream selectedImageInputStream, String selectedImageFilename) {
        Amplify.Storage.uploadInputStream(
                selectedImageFilename, //S3 key
                selectedImageInputStream,
                success -> {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    taskImageS3Key = success.getKey();

                }, failure -> {
                    Log.e(TAG, "Failure to upload file to S3 with filename: " + selectedImageFilename + " with error " + failure.getMessage());
                }
        );
    }

    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
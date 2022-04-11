package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.kevdev.taskmaster.R;

import java.io.File;

public class TaskDetailActivity extends AppCompatActivity {
    public static final String TAG = "TaskDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        // Access data passed via intent
        Intent passedData = getIntent();
        String taskName;
        String taskBody;
        if (passedData != null) {
            taskName = passedData.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
            TextView titleTextView = (TextView) findViewById(R.id.taskDetailHeader);
            titleTextView.setText(taskName);
            taskBody = passedData.getStringExtra(MainActivity.TASK_BODY_EXTRA_TAG);
            TextView bodyTextView = (TextView) findViewById(R.id.taskDetailDescription);
            bodyTextView.setText(taskBody);
            String taskImageS3Key = passedData.getStringExtra(MainActivity.TASK_IMAGES3_EXTRA_TAG);
            ImageView taskImage = findViewById(R.id.taskImage);
            if (!taskImageS3Key.equals("")) {
                Amplify.Storage.downloadFile(
                        taskImageS3Key,
                        new File(getApplication().getFilesDir(), taskImageS3Key),
                        success -> {
                            taskImage.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                        },
                        failure -> {
                            Log.e(TAG, "Unable to get image from S3 for " + taskImageS3Key);
                        }
                );
            }
        }
    }
}
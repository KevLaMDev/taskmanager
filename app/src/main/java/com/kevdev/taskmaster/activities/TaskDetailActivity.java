package com.kevdev.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.kevdev.taskmaster.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskDetailActivity extends AppCompatActivity {
    public static final String TAG = "TaskDetailActivity";
    private MediaPlayer mp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        mp = new MediaPlayer();
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
            if (taskImageS3Key != null && !taskImageS3Key.equals("")) {
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
        setUpSpeakButton();
        setUpTranslateTextButton();
    }

    private void setUpSpeakButton() {
        Button speakButton = findViewById(R.id.speakTaskButton);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passedData = getIntent();
                if (passedData != null) {
                    String taskDescription = passedData.getStringExtra(MainActivity.TASK_BODY_EXTRA_TAG);
                    Amplify.Predictions.convertTextToSpeech(
                            taskDescription,
                            result -> playAudio(result.getAudioData()),
                            error -> Log.e(TAG, "Conversion failed", error)
                    );
                }
            }
        });
    }

    private void setUpTranslateTextButton() {
        Button translateButton = findViewById(R.id.translateButton);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passedData = getIntent();
                String taskTitle = passedData.getStringExtra(MainActivity.TASK_NAME_EXTRA_TAG);
                TextView taskHeader = findViewById(R.id.taskDetailHeader);
                Amplify.Predictions.translateText(taskTitle,
                        result -> {
                            Log.i(TAG, result.getTranslatedText());
                            taskHeader.setText(result.getTranslatedText());
                        },
                        error -> Log.e(TAG, "Translation failed", error)
                );
                String taskDescription = passedData.getStringExtra(MainActivity.TASK_BODY_EXTRA_TAG);
                TextView taskBody = findViewById(R.id.taskDetailDescription);
                Amplify.Predictions.translateText(taskDescription,
                        result -> {
                            Log.i(TAG, result.getTranslatedText());
                            taskBody.setText(result.getTranslatedText());
                        },
                        error -> Log.e(TAG, "Translation failed", error)
                );
            }
        });
    }

    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio.mp3");

        try (OutputStream out = new FileOutputStream(mp3File)) {
            byte[] buffer = new byte[8 * 1_024];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            mp.reset();
            mp.setOnPreparedListener(MediaPlayer::start);
            mp.setDataSource(new FileInputStream(mp3File).getFD());
            mp.prepareAsync();
        } catch (IOException error) {
            Log.e(TAG, "Error writing audio file", error);
        }
    }

}
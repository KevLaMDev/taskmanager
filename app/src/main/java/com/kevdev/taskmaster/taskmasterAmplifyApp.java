package com.kevdev.taskmaster;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.core.Amplify;

public class taskmasterAmplifyApp extends Application
{
    public static final String TAG = "taskmasterApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae) {
            Log.e(TAG, "Error initializing Amplify: " + ae.getMessage(), ae);
        }
    }
}

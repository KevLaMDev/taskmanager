package com.kevdev.taskmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.amplifyframework.datastore.generated.model.Task;
import com.kevdev.taskmaster.R;
import com.kevdev.taskmaster.activities.TaskDetailActivity;

import java.util.List;

public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter

{
    List<Task> tasks;
    Context callingActivity;
    public static final String TASK_NAME_EXTRA_TAG = "taskName";
    public static final String TASK_BODY_EXTRA_TAG = "taskBody";
    public static final String TASK_IMAGES3_EXTRA_TAG = "imageS3Key";

    public MainActivityRecyclerViewAdapter() {}

    public MainActivityRecyclerViewAdapter(List<Task> tasks, Context callingActivity) {
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate fragment
        View mainActivityFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_activity, parent, false);

        // Attach fragment to ViewHolder
        return new MainActivityViewHolder(mainActivityFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.mainActivityRecyclerViewFragment);
        String taskName = tasks.get(position).getTitle();
        String taskBody = tasks.get(position).getBody();
        String taskImageS3Key = tasks.get(position).getProductImageS3Key();
        taskFragmentTextView.setText(taskName);
        View TaskViewHolder = holder.itemView;
        TaskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetail = new Intent(callingActivity, TaskDetailActivity.class);
            goToTaskDetail.putExtra(TASK_NAME_EXTRA_TAG, taskName);
            goToTaskDetail.putExtra(TASK_BODY_EXTRA_TAG, taskBody);
            goToTaskDetail.putExtra(TASK_IMAGES3_EXTRA_TAG, taskImageS3Key);
            callingActivity.startActivity(goToTaskDetail);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class MainActivityViewHolder extends RecyclerView.ViewHolder {

        public MainActivityViewHolder(View itemView) {
            super(itemView);
        }

    }
}

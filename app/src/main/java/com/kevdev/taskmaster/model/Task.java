package com.kevdev.taskmaster.model;


import androidx.annotation.NonNull;

import com.kevdev.taskmaster.enums.State;

public class Task {

    public Long id;
    String title;
    String body;
    public State taskState;



    public Task(String title, String body, State s) {
        this.title = title;
        this.body = body;
        this.taskState = s;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Task() {

    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public State getTaskState() {
        return taskState;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", taskState=" + taskState +
                '}';
    }
}

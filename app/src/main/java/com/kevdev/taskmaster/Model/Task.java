package com.kevdev.taskmaster.Model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    String title;
    String body;
    public  State taskState;

    public enum State {
        NEW("New"),
        ASSIGNED("Assigned"),
        COMPLETE("Complete");

        String state;

        State(String s){
            state = s;
        }

        public static State fromString(String inputState) {
            for (State taskState : State.values()) {
                if (taskState.state.equals(inputState)) return taskState;
            }
            return null;
        }

        @NonNull
        @Override
        public String toString() {
            if (state == null) {
                return "";
            }
            return state;
        }

    }

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

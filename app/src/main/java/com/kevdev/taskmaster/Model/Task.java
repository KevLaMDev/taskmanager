package com.kevdev.taskmaster.Model;

public class Task {
    String title;
    String body;
    State taskState;

    public enum State {
        NEW("New"),
        ASSIGNED("Assigned"),
        COMPLETE("Complete");

        String state;

        State(String s){
            state = s;
        }

        @Override
        public String toString() {
            return state;
        }
    }

    public Task(String title, String body, State S) {
        this.title = title;
        this.body = body;
        this.taskState = S;
    }

    public String getTitle() {
        return title;
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
}

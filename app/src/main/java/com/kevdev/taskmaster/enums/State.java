package com.kevdev.taskmaster.enums;

import androidx.annotation.NonNull;

import com.kevdev.taskmaster.model.Task;

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
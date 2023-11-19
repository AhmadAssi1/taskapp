package com.example.managingapp;

public class Task {
    private String taskName;
    private String due;
    private boolean isDone;

    public Task(String taskName, String due) {
        this.taskName = taskName;
        this.due = due;
        this.isDone = false; // By default, a new task is not done
    }
    public String getTaskName() {
        return taskName;
    }

    public String getDue() {
        return due;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return taskName;
    }
}

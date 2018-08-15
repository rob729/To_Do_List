package com.example.robin.minimaltodo;

public class Task {

    String task, time;
    long tm;

    public Task(String task, String time, long tm) {
        this.task = task;
        this.time = time;
        this.tm = tm;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTm() {
        return tm;
    }

    public void setTm(long tm) {
        this.tm = tm;
    }
}

package com.sample.drawer;


public class TaskList {
    public TaskList(int id, String name, int dateTime) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
    }

    public int getID() {
        return id;
    }

    public int getDateTime() {
        return dateTime;
    }

    public String getName() {
        return name;
    }

    public void setDateTime(int dateTime) {
        this.dateTime = dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    private int id, dateTime;
    private String name;
}

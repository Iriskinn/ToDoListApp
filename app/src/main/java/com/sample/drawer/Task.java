package com.sample.drawer;


public class Task {
    public Task(String name, String description, String type, boolean status, int dateTime,
                int rate, int doneTime, int listID, int mvt) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.dateTime = dateTime;

        this.rate = rate;
        this.doneTime = doneTime;
        this.listID = listID;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() { return status; }

    public String getDescription() { return description; }

    public String getType() { return type; }

    public int getDateTime() { return dateTime; }

    public int getDoneTime() { return doneTime; }

    public int getRate() { return rate; }

    public int getListID() { return listID; }

    public void setStatus(boolean status) {
        this.status = status;
    }


    private int dateTime, doneTime, rate, listID, mvt;
    private String name, description, type;
    private boolean status = false;
}

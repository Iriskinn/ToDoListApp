package com.sample.drawer;


import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.Date;

public class Task {
    public Task(String name, String description, String type, boolean status, int dateTime) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getDateTime() {
        return dateTime;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public View getView(Context context) {
        EditText result = new EditText(context);
        result.setText(name);
        return result;
    }

    public String toXml() {
        return " ";
    }


    private int dateTime;
    private String name, description, type;
    private boolean status = false;
}

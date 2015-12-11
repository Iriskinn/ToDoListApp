package com.sample.drawer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.sample.drawer.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.SortedSet;


public class MainActivity extends ActionBarActivity {

    public Drawer.Result drawerResult = null;
    private AccountHeader.Result headerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDrawer);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init Drawer & Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerResult = Utils.createCommonDrawer(MainActivity.this, toolbar, headerResult);
        drawerResult.setSelectionByIdentifier(2, true);

        Data.receive(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setAddTaskListener() {
        this.findViewById(R.id.btn_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtTaskName = ((EditText) findViewById(R.id.edit_task_name));
                EditText edtTaskDesc = ((EditText) findViewById(R.id.edit_task_description));
                CheckBox cbTaskWeekly = ((CheckBox) findViewById(R.id.checkbox_week_task));
                String type;

                if (cbTaskWeekly.isChecked()) {
                    type = "Week";
                } else {
                    type = "ToDo";
                }

                Task newTask = new Task(edtTaskName.getText().toString(),
                        edtTaskDesc.getText().toString(),
                        type,
                        false,
                        (int) new Date().getTime());

                Data.addTask(newTask, getApplicationContext());

                if (cbTaskWeekly.isChecked()) {
                    drawerResult.setSelectionByIdentifier(3, true);
                } else {
                    drawerResult.setSelectionByIdentifier(2, true);
                }
            }
        });

    }

    public void setDeleteDoneListener() {
        this.findViewById(R.id.btn_delete_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.deleteDoneTasks(getApplicationContext());
                Data.doneList.clear();
            }
        });
    }

    public void showTasks(String type) {
        Context context = getApplicationContext();
        if (type.equals("ToDo")) {
            TaskAdapter taskAdapter = new TaskAdapter(context, new ArrayList<>(Data.todoList));

            ListView layout = (ListView) findViewById(R.id.todo_layout);
            layout.setAdapter(taskAdapter);

        } else if (type.equals("Week")) {
            TaskAdapter taskAdapter = new TaskAdapter(context, new ArrayList<>(Data.weekList));

            ListView layout = (ListView) findViewById(R.id.week_layout);
            layout.setAdapter(taskAdapter);

        } else if (type.equals("Done")) {
            TaskAdapter taskAdapter = new TaskAdapter(context, new ArrayList<>(Data.doneList));

            ListView layout = (ListView) findViewById(R.id.done_layout);
            layout.setAdapter(taskAdapter);
        }
    }
}
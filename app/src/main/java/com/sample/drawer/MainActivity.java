package com.sample.drawer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.sample.drawer.fragments.AddTaskFragment;
import com.sample.drawer.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;


public class MainActivity extends ActionBarActivity {

    public Drawer.Result drawerResult = null;
    private AccountHeader.Result headerResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDrawer);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data.receive(this);

        // init Drawer & Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerResult = Utils.createCommonDrawer(MainActivity.this, toolbar, headerResult);
        drawerResult.setSelectionByIdentifier(5, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                TextView tvListID = ((TextView) findViewById(R.id.tv_list_id));
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
                        (int) new Date().getTime(),
                        0, 0, Integer.parseInt(tvListID.getText().toString()), 0);

                Data.addTask(newTask, getApplicationContext());

                Data.taskList.add(newTask);

                drawerResult.setSelectionByIdentifier(
                        Integer.parseInt(tvListID.getText().toString()) + 100, true);
            }
        });
    }

    public void setDeleteDoneListener() {
        this.findViewById(R.id.btn_delete_done).setOnClickListener(new DeleteDoneListener(this));
    }

    public void showTasks(int listID) {
        Context context = getApplicationContext();
        LinkedList<Task> todoList = new LinkedList<>();
        for (Task task : Data.taskList) {
            if (task.getListID() == listID && !task.getStatus()) {
                todoList.add(task);
            }
        }
        TaskAdapter taskAdapter = new TaskAdapter(context, new ArrayList<>(todoList));

        ListView layout = (ListView) findViewById(R.id.list_layout);
        layout.setAdapter(taskAdapter);
    }

    public void showDoneTasks() {
        Context context = getApplicationContext();
        LinkedList<Task> todoList = new LinkedList<>();
        for (Task task : Data.taskList) {
            if (task.getStatus()) {
                todoList.add(task);
            }
        }
        TaskAdapter taskAdapter = new TaskAdapter(context, new ArrayList<>(todoList));

        ListView layout = (ListView) findViewById(R.id.done_layout);
        layout.setAdapter(taskAdapter);
    }

    public void setMenu(int listID) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.getMenu().clear();
        getMenuInflater().inflate(R.menu.main, toolbar.getMenu());

        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            toolbar.getMenu().getItem(i).setVisible(true);
        }

        MenuListener menuListener = new MenuListener();
        menuListener.setListID(listID);
        toolbar.setOnMenuItemClickListener(menuListener);
    }

    public void resetMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.getMenu().clear();

        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            toolbar.getMenu().getItem(i).setVisible(false);
        }
    }

    public void setAddListListener() {
        this.findViewById(R.id.btn_add_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtListName = ((EditText) findViewById(R.id.edit_list_name));

                TaskList newList = new TaskList(Data.lists.size(), edtListName.getText().toString(),
                        (int) new Date().getTime());

                drawerResult.addItem(
                        new PrimaryDrawerItem().withName(newList.getName()).withIcon(GoogleMaterial.Icon.gmd_list)
                                .withIdentifier(newList.getID() + 100),
                        Data.lists.size() + 2
                );

                Data.addList(newList, getApplicationContext());

                drawerResult.setSelectionByIdentifier(newList.getID() + 100, true);

                Log.d("Log: ", "list id = " + (newList.getID() + 100));
            }
        });
    }

    class MenuListener implements Toolbar.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Log.d("Log: ", item.getTitle().toString() + " " + item.getItemId());

            if (item.getTitle().toString().equals(getString(R.string.add_task))) {
                AddTaskFragment frag = new AddTaskFragment();
                frag.setListID(listID);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, frag).commit();
                setTitle(R.string.add_task);
            } else if (item.getTitle().toString().equals(getString(R.string.delete_list))) {
                drawerResult.removeItem(listID + 2);
                Data.deleteList(listID, getApplicationContext());
                drawerResult.setSelectionByIdentifier(5, true);
            }
            return false;
        }


        public void setListID(int listID) {
            this.listID = listID;
        }

        private int listID;
    }

    class DeleteDoneListener implements View.OnClickListener {
        private Activity activity;

        public DeleteDoneListener(Activity activity) {
            this.activity = activity;
        }

        public void onClick(View v) {
            Data.deleteDoneTasks(getApplicationContext());
        }
    }
}
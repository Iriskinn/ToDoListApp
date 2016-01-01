package com.sample.drawer;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<Task> objects;

    public TaskAdapter(Context context, ArrayList<Task> products) {
        this.context = context;
        objects = products;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.task, parent, false);
        }

        Task task = getTask(position);
        boolean done = task.getStatus();

        ((TextView) view.findViewById(R.id.taskName)).setText(task.getName());
        ((TextView) view.findViewById(R.id.taskDesc)).setText(task.getDescription());
        CheckBox cbBox = (CheckBox) view.findViewById(R.id.cbBox);
        cbBox.setOnCheckedChangeListener(myCheckChangList);
        cbBox.setTag(position);
        cbBox.setChecked(done);
        ImageButton delBut = (ImageButton) view.findViewById(R.id.deleteButton);
        delBut.setOnClickListener(new DelTaskListener(task));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView descView = (TextView) view.findViewById(R.id.taskDesc);
                ImageButton delBut = (ImageButton) view.findViewById(R.id.deleteButton);
                if (descView.getVisibility() == View.GONE) {
                    descView.setVisibility(View.VISIBLE);
                    delBut.setVisibility(View.VISIBLE);
                } else {
                    descView.setVisibility(View.GONE);
                    delBut.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    Task getTask(int position) {
        return ((Task) getItem(position));
    }


    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            Task task = getTask((Integer) buttonView.getTag());
            Data.changeTask(task, isChecked, context);
        }
    };


    class DelTaskListener implements View.OnClickListener {

        private Task task;

        public DelTaskListener(Task task) {
            this.task = task;
        }

        public void onClick(View v) {
            Data.delTask(this.task, context);
        }
    }
}
package com.sample.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(done);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView descView = (TextView) view.findViewById(R.id.taskDesc);
                if (descView.getVisibility() == View.GONE) {
                    descView.setVisibility(View.VISIBLE);
                } else {
                    descView.setVisibility(View.GONE);
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

}
package com.sample.drawer.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sample.drawer.Data;
import com.sample.drawer.MainActivity;
import com.sample.drawer.R;
import com.sample.drawer.Task;


public class TodoListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).showTasks("ToDo");
    }
}

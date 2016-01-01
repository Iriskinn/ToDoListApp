package com.sample.drawer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.drawer.MainActivity;
import com.sample.drawer.R;


public class AddTaskFragment extends Fragment {
    public void setListID(int listID) {
        this.listID = listID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_task, null);
        return v;
    }

    public void onStart() {
        super.onStart();

        TextView tvListID = (TextView) getActivity().findViewById(R.id.tv_list_id);
        tvListID.setText(Integer.toString(listID));

        ((MainActivity) getActivity()).resetMenu();
        ((MainActivity) getActivity()).setAddTaskListener();
    }


    private int listID;
}

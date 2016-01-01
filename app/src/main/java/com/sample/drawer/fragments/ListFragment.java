package com.sample.drawer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sample.drawer.MainActivity;
import com.sample.drawer.R;


public class ListFragment extends Fragment {
    public ListFragment(int id) {
        super();
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).setMenu(id);
    }

    public void onStart() {
        super.onStart();

        ((MainActivity) getActivity()).showTasks(id);
    }


    private int id = 0;
}

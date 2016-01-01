package com.sample.drawer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sample.drawer.MainActivity;
import com.sample.drawer.R;


public class DoneListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_done_list, container, false);
    }

    public void onStart() {
        super.onStart();

        ((MainActivity) getActivity()).resetMenu();
        ((MainActivity) getActivity()).showDoneTasks();
    }
}

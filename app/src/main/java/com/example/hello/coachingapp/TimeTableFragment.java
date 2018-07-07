package com.example.hello.coachingapp;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


public class TimeTableFragment extends Fragment {

    private View view;
    RecyclerView rv;
    RecyclerViewAdapter adapter;

    public TimeTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view, container, false);
        //dbr.keepSynced(true);
        rv = (RecyclerView) view.findViewById(R.id.RecyclerView2);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        adapter=new RecyclerViewAdapter(getActivity(), "TimeTableView");
        rv.setAdapter(adapter);
        return view;
    }

}

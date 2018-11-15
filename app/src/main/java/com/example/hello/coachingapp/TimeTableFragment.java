package com.example.hello.coachingapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TimeTableFragment extends android.support.v4.app.Fragment{


    DatabaseReference dbr;
    RecyclerView rv;
    TimeTableAdapter adapter;
    View view;
    private Context context;
    private ArrayList<TimeData> data;
    private ProgressBar mProgressCircle;
    private ProgressDialog progress;
    private RecyclerView.LayoutManager layoutManager;

    public TimeTableFragment setDBR(String reference)
    {
        TimeTableFragment newFrag = new TimeTableFragment();
        newFrag.dbr= FirebaseDatabase.getInstance().getReference(reference);
        return newFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view, container, false);
        //dbr.keepSynced(true);
        mProgressCircle = (ProgressBar) view.findViewById(R.id.progress_bar);
        rv = (RecyclerView) view.findViewById(R.id.recyclerView1);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        GetDataFirebase();
        return view;
    }

    void GetDataFirebase(){
        data = new ArrayList<TimeData>();
        //dbr = FirebaseDatabase.getInstance().getReference("ToppersData");
        mProgressCircle.setVisibility(view.VISIBLE);
        ValueEventListener valueEventListener = dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot classes : dataSnapshot.getChildren()) {
                    for(DataSnapshot subjects:classes.getChildren())
                    {
                        TimeData d = subjects.getValue(TimeData.class);
                        data.add(d);
                    }
                }
                adapter=new TimeTableAdapter(data, getActivity(), dbr, new MainActivity.MyAdapterListener() {
                    @Override
                    public void FromClickListner(View v, int position) {
                        TextView From = rv.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.From);
                        ((MainActivity)getActivity()).onButtonClicked(From);
                    }

                    @Override
                    public void FromClickListner(View v) {

                    }

                    @Override
                    public void ToOnClickListner(View v, int position) {
                        TextView To = rv.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.To);
                        ((MainActivity)getActivity()).onButtonClicked(To);
                    }

                    @Override
                    public void ToOnClickListner(View v) {

                    }
                });
                rv.setAdapter(adapter);
                mProgressCircle.setVisibility(view.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressCircle.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}

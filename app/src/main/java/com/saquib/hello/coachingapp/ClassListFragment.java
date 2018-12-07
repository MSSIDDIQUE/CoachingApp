package com.saquib.hello.coachingapp;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassListFragment extends android.support.v4.app.Fragment {

    String d;
    private View view;
    private RecyclerView rv;
    private ClassListAdapter adapter;
    private ArrayList<String>data;
    private ProgressBar pb;

    public ClassListFragment() {
        // Required empty public constructor
    }

    public ClassListFragment setDbr(String d)
    {
        ClassListFragment newFrag = new ClassListFragment();
        newFrag.d = d;
        return newFrag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view,container,false);
        rv = view.findViewById(R.id.recyclerView1);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        pb = view.findViewById(R.id.progress_bar);
        pb.setVisibility(View.GONE);
        GetDataFirebase(d);
        return view;
    }

    void GetDataFirebase(final String d){
        data = new ArrayList<String>();
        pb.setVisibility(View.VISIBLE);
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("StudyMaterial").child(d);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String s = ds.getKey().toString();
                    data.add(d+"/"+s);
                }
                adapter=new ClassListAdapter(data,getContext());
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pb.setVisibility(View.GONE);
            }
        });

    }

}

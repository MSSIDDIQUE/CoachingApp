package com.example.hello.coachingapp;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ToppersFragment extends android.support.v4.app.Fragment{
    DatabaseReference dbr;
    RecyclerView rv;
    RecyclerViewAdapter adapter;
    View view;
    private Context context;
    private ArrayList<ToppersData> data;
    private ProgressBar mProgressCircle;
    ProgressDialog progress;

    public ToppersFragment setDBR(String reference)
    {
        ToppersFragment newFrag = new ToppersFragment();
        newFrag.dbr=FirebaseDatabase.getInstance().getReference(reference);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        GetDataFirebase();
        return view;
    }

    void GetDataFirebase(){
        data = new ArrayList<ToppersData>();
        //dbr = FirebaseDatabase.getInstance().getReference("ToppersData");
        mProgressCircle.setVisibility(view.VISIBLE);
        ValueEventListener valueEventListener = dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ToppersData d = ds.getValue(ToppersData.class);
                    data.add(d);
                }
                adapter=new RecyclerViewAdapter(data,getActivity(), "ToppersView");
                adapter.notifyDataSetChanged();
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

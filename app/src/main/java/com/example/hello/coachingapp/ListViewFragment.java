package com.example.hello.coachingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListViewFragment extends android.support.v4.app.Fragment {
    String d;

    private View view;
    private RecyclerView rv;
    private ListViewAdapter adapter;
    private ArrayList<StudyMaterialData> data;
    private ProgressBar pb;

    public ListViewFragment() {
    }

    public ListViewFragment setDbr(int position, ArrayList<String>data, Context context)
    {
        ListViewFragment newFrag = new ListViewFragment();
        newFrag.d = data.get(position);
        return newFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view,container,false);
        pb = view.findViewById(R.id.progress_bar);
        pb.setVisibility(View.GONE);
        rv = view.findViewById(R.id.recyclerView1);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        GetDataFirebase();
        return view;
    }

    void GetDataFirebase(){
        data = new ArrayList<StudyMaterialData>();
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("StudyMaterial").child(d);
        ValueEventListener valueEventListener = dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StudyMaterialData sd = ds.getValue(StudyMaterialData.class);
                    data.add(sd);//loading the material type
                }
                adapter=new ListViewAdapter(data,getContext());
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });

    }
}

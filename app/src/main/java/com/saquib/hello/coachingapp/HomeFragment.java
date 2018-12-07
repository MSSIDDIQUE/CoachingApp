package com.saquib.hello.coachingapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends android.support.v4.app.Fragment {

    public android.support.v4.view.ViewPager vp;
    public CircleIndicator indicator;
    public ProgressBar pb1,pb2;
    private StorageReference str;
    private DatabaseReference dbr;
    private static int currentPage = 0;
    private RecyclerView rv;
    TeachersAdapter adapter;
    private ArrayList<TeachersData>data;
    private ArrayList<String>urls;
    private ArrayList<ArrayList<TeachersData>>td;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        vp = view.findViewById(R.id.ImageSlider);
        indicator = view.findViewById(R.id.Indicator);
        rv = (RecyclerView) view.findViewById(R.id.RecyclerView);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(layoutManager);
        pb1 = view.findViewById(R.id.progressBar6);
        pb2 = view.findViewById(R.id.pb);
        pb1.setVisibility(View.VISIBLE);
        pb1.setVisibility(View.VISIBLE);
        if(((MainActivity)getActivity()).isConnected())
        {
            LoadImages();
            getData();
        }
        else
        {
            Toast.makeText(getContext(),"Please Check your Network Connection", Toast.LENGTH_LONG).show();
            pb1.setVisibility(View.GONE);
            pb2.setVisibility(View.GONE);
        }
        return view;
    }

    public void LoadImages()
    {
        urls = new ArrayList<String>();
        str = FirebaseStorage.getInstance().getReference("SliderImages");
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int n=0;
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().toString().equals("SliderImageCount"))
                    {
                        n = Integer.parseInt(ds.getValue(String.class));
                        for(int i=0;i<n;i++)
                        {
                            str.child(String.valueOf(i+1)+".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    urls.add(task.getResult().toString());
                                    pb1.setVisibility(View.GONE);
                                    PagerAdapter adapter = new SliderAdapterFragment(getContext(),urls);
                                    vp.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    indicator.setViewPager(vp);
                                }

                            });
                        }
                    }
                }
                RunSlider();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getData() {
        dbr = FirebaseDatabase.getInstance().getReference().child("Teachers");

        td= new ArrayList<ArrayList<TeachersData>>();
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.hasChild("Education"))
                    {
                        data = new ArrayList<TeachersData>();
                        TeachersData d1 = ds.child("Education").child("About Teaching").getValue(TeachersData.class);
                        TeachersData d2 = ds.child("Education").child("Graduation").getValue(TeachersData.class);
                        TeachersData d3 = ds.child("Education").child("High School").getValue(TeachersData.class);
                        TeachersData d4 = ds.child("Education").child("What are you doing Currently").getValue(TeachersData.class);
                        TeachersData d5 = new TeachersData();
                        d5.setEmail((String) ds.child("email").getValue());
                        d5.setPassword((String) ds.child("password").getValue());
                        d5.setName((String) ds.child("name").getValue());
                        d5.setImgurl((String) ds.child("imgurl").getValue());
                        d5.setType((String) ds.child("type").getValue());
                        d5.setTokenId((String) ds.child("tokenId").getValue());
                        data.add(d1);
                        data.add(d2);
                        data.add(d3);
                        data.add(d4);
                        data.add(d5);
                        td.add(data);
                    }

                }
                adapter = new TeachersAdapter(td, getActivity());
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                final int speedScroll = 5000;
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    int count = 0;
                    boolean flag = true;
                    @Override
                    public void run() {
                        if(count < adapter.getItemCount()){
                            if(count==adapter.getItemCount()-1){
                                flag = false;
                            }else if(count == 0){
                                flag = true;
                            }
                            if(flag) count++;
                            else count--;

                            rv.smoothScrollToPosition(count);
                            handler.postDelayed(this,speedScroll);
                        }
                    }
                };

                handler.postDelayed(runnable,speedScroll);
                pb2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pb2.setVisibility(View.GONE);
            }
        });
    }

    private void RunSlider()
    {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == urls.size()) {
                    currentPage = 0;
                }
                vp.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 7500);
    }
}

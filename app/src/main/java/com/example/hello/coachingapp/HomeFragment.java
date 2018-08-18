package com.example.hello.coachingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends android.support.v4.app.Fragment {

    public android.support.v4.view.ViewPager vp;
    public CircleIndicator indicator;
    public ProgressBar pb;
    private StorageReference str;
    private DatabaseReference dbr;
    private static int currentPage = 0;
    ArrayList<String>urls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, container, false);
        vp = view.findViewById(R.id.ImageSlider);
        indicator = view.findViewById(R.id.Indicator);
        pb = view.findViewById(R.id.progressBar6);
        pb.setVisibility(View.VISIBLE);
        LoadImages();
        RunSlider();
        return view;
    }

    public void LoadImages()
    {
        urls = new ArrayList<String>();
        str = FirebaseStorage.getInstance().getReference("SliderImages");
        for(int i=0;i<5;i++)
        {
            str.child(String.valueOf(i+1)+".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    urls.add(task.getResult().toString());
                    pb.setVisibility(View.GONE);
                    PagerAdapter adapter = new SliderAdapterFragment(getContext(),urls);
                    vp.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    indicator.setViewPager(vp);
                }

            });
        }
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
        }, 2500, 2500);
    }
}

package com.saquib.hello.coachingapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class ListViewFragment extends android.support.v4.app.Fragment {
    String d;
    private View view;
    private RecyclerView rv;
    private DownloadManager dm;
    private ListViewAdapter adapter;
    private ArrayList<StudyMaterialData> data;
    private ProgressBar pb;
    private static final int PERMISSIONS_CODE =1000;
    private static int p;

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
                adapter=new ListViewAdapter(data, getContext(), new MainActivity.MyAdapterListener() {
                    @Override
                    public void FromClickListner(View v, int position) {

                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                            {
                                String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions,PERMISSIONS_CODE);
                                p = position;
                            }
                            else
                            {
                                StartDownloading(position);
                            }
                        }

                        else
                        {
                            StartDownloading(position);
                        }

                    }

                    @Override
                    public void FromClickListner(View v) {

                    }

                    @Override
                    public void ToOnClickListner(View v, int position) {
                        File book = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+data.get(position).getTitle()).getAbsolutePath());
                        if(!book.exists())
                        {
                            Toast.makeText(getContext(), "Please download the file first", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            StartDownloading(position);
                        }
                    }

                    @Override
                    public void ToOnClickListner(View v) {

                    }
                });
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });

    }

    public  void StartDownloading(int position)
    {
        File book = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+data.get(position).getTitle()).getAbsolutePath());
        if(!book.exists())
        {
            dm = (DownloadManager)getContext().getSystemService(getContext().DOWNLOAD_SERVICE);
            Uri bookurl = Uri.parse(data.get(position).getPdfurl());
            DownloadManager.Request request = new DownloadManager.Request(bookurl);
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,data.get(position).getTitle());
            dm.enqueue(request);
            Toast.makeText(getContext(),"Your pdf is Downloading See Notifications", Toast.LENGTH_LONG).show();
        }
        else
        {
            book = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/"+data.get(position).getTitle().toString()).getAbsolutePath());
            if(book.exists())
            {
                Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                getContext().startActivity(intent);
            }
            else
            {
                Toast.makeText(getContext(),"This book is not downloaded yet, Please Download it first",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case PERMISSIONS_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    StartDownloading(p);
                }
                else
                {
                    Toast.makeText(getContext(),"Sorry, permissions Denied",Toast.LENGTH_LONG).show();
                }
        }
    }
}

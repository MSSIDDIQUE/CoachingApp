package com.example.hello.coachingapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v13.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<StudyMaterialData> data;
    private Context context;
    private View view;
    private CardView download;
    private TextView bookTitle;
    private DownloadManager dm;
    private int REQUEST_CODE = 1;

    public ListViewAdapter(ArrayList<StudyMaterialData>data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.book_item_layout,null);
        download = view.findViewById(R.id.Download);
        bookTitle = view.findViewById(R.id.bookTitle);
        bookTitle.setText(data.get(position).getTitle());
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dm = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
                Uri bookurl = Uri.parse(data.get(position).getPdfurl());
                DownloadManager.Request request = new DownloadManager.Request(bookurl);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,data.get(position).getTitle());
                Long reference = dm.enqueue(request);
                Toast.makeText(context,"Your pdf is Downloading See Notifications", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
